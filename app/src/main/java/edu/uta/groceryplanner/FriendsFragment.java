package edu.uta.groceryplanner;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.FriendsAdapter;
import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.UserBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab, personalFab, billFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;
    DatabaseReference friendsRef, usersRef;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FriendsBean> friends;
    private static final int REQUEST_INVITE = 0;
    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(firebaseAuth.getCurrentUser().getUid());
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = view.findViewById(R.id.friendsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friends = new ArrayList<>();
        mainFab = view.findViewById(R.id.mainFab);
        personalFab = view.findViewById(R.id.personalFab);
        billFab = view.findViewById(R.id.billFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        personalFab.setOnClickListener(this);
        billFab.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mainFab:
                animateFAB();
                break;
            case R.id.personalFab:
                animateFAB();
                LayoutInflater li = LayoutInflater.from(getContext());
                View modal = li.inflate(R.layout.friend_modal, null);
                final EditText textFriendEmail = modal.findViewById(R.id.TextFriendEmail);
                final Button btnFriendInvite=modal.findViewById(R.id.btnFriendInvite);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(modal)
                        .setCancelable(false)
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        animateFAB();
                                        dialog.cancel();
                                    }
                                }).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                String friendEmail = textFriendEmail.getText().toString();
                                Log.v("friend",friendEmail);
                                if (TextUtils.isEmpty(friendEmail)) {
                                    textFriendEmail.setError("EmailId cannot be empty", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                                }
                                else if(!friendEmail.contains("@")){
                                    textFriendEmail.setError("EmailId not valid", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                                }
                                else if (friendEmail.equalsIgnoreCase(firebaseAuth.getCurrentUser().getEmail())) {
                                    textFriendEmail.setError("EmailId and User Email cannot be same", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                                }
                                else {
                                    usersRef.orderByChild("emailId").equalTo(textFriendEmail.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    UserBean userBean = dataSnapshot.getValue(UserBean.class);
                                                    if("active".equalsIgnoreCase(userBean.getStatus())) {
                                                        friendsRef.child(userBean.getUserId()).setValue(new FriendsBean(userBean.getUserId(), userBean.getUserName(), userBean.getEmailId(), "resolved", 0));
                                                        dialog.dismiss();
                                                    }
                                                    else
                                                        textFriendEmail.setError("your friend is inactive, Contact Admin", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                                                }
                                            } else {
                                                textFriendEmail.setError("No Email Id found, Do you want to invite?", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                                                btnFriendInvite.setVisibility(View.VISIBLE);
                                                btnFriendInvite.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                        btnFriendInvite.setVisibility(View.GONE);
                                                        animateFAB();
                                                        sendInvite();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            dialog.dismiss();
                                            animateFAB();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.billFab:
                startActivity(new Intent(getContext(),AddBillActivity.class));
                break;
        }
    }
    public void sendInvite(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setEmailHtmlContent(getString(R.string.invitation_html_content))
                .setEmailSubject(getString(R.string.invitation_email_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    public void animateFAB() {

        if (isFabOpen) {
            mainFab.startAnimation(rotate_backward);
            personalFab.startAnimation(fab_close);
            billFab.startAnimation(fab_close);
            personalFab.setClickable(false);
            billFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotate_forward);
            personalFab.startAnimation(fab_open);
            billFab.startAnimation(fab_open);
            personalFab.setClickable(true);
            billFab.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onStart() {
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends.clear();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    FriendsBean friendsBean = dataSnap.getValue(FriendsBean.class);
                    friends.add(friendsBean);
                }
                adapter = new FriendsAdapter(friends, getContext());
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }
}
