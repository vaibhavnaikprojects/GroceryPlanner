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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab,personalFab,billFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    DatabaseReference friendsRef;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<FriendsBean> friends;

    public FriendsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
        friendsRef = FirebaseDatabase.getInstance().getReference("friends").child(firebaseAuth.getCurrentUser().getUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView=view.findViewById(R.id.friendsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friends=new ArrayList<>();
        mainFab = view.findViewById(R.id.mainFab);
        personalFab = view.findViewById(R.id.personalFab);
        billFab = view.findViewById(R.id.billFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        personalFab.setOnClickListener(this);
        billFab.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.mainFab:
                animateFAB();
                break;
            case R.id.personalFab:
                animateFAB();
                LayoutInflater li = LayoutInflater.from(getContext());
                View modal = li.inflate(R.layout.friend_modal, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(modal);
                final EditText textFriendEmail= modal.findViewById(R.id.TextFriendEmail);
                //Button button= modal.findViewById(R.id.btnAddFriend);
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(getContext(),textFriendEmail.getText(),Toast.LENGTH_LONG).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.billFab:
                break;
        }
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
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    FriendsBean friendsBean=dataSnap.getValue(FriendsBean.class);
                    friends.add(friendsBean);
                }
                adapter=new FriendsAdapter(friends,getContext());
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
