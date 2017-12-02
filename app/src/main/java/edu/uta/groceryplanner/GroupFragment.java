package edu.uta.groceryplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.GroupAdapter;
import edu.uta.groceryplanner.beans.GroupBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab, groupFab, billFab;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GroupBean> groupListBeans;
    DatabaseReference groupsRef, groupUsersRef;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups");
        groupUsersRef = FirebaseDatabase.getInstance().getReference("GroupUsers");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupListBeans = new ArrayList<>();
        adapter = new GroupAdapter(groupListBeans, getContext(), firebaseAuth, onItemClickListener);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        mainFab = view.findViewById(R.id.mainFab);
        groupFab = view.findViewById(R.id.groupFab);
        billFab = view.findViewById(R.id.billFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        groupFab.setOnClickListener(this);
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
            case R.id.groupFab:
                startActivity(new Intent(getContext(), GroupActivity.class));
                break;
            case R.id.billFab:
                startActivity(new Intent(getContext(), AddBillActivity.class));
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {
            mainFab.startAnimation(rotate_backward);
            groupFab.startAnimation(fab_close);
            billFab.startAnimation(fab_close);
            groupFab.setClickable(false);
            billFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotate_forward);
            groupFab.startAnimation(fab_open);
            billFab.startAnimation(fab_open);
            groupFab.setClickable(true);
            billFab.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onStart() {
        groupUsersRef.orderByChild(firebaseAuth.getCurrentUser().getUid()).equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String groupId = dataSnapshot.getKey();
                Log.v("groupId", groupId);
                groupsRef.child(groupId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GroupBean groupBean = dataSnapshot.getValue(GroupBean.class);
                        if(!groupBean.getGroupName().equalsIgnoreCase("") && !groupListBeans.contains(groupBean)) {
                            groupListBeans.add(groupBean);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    GroupAdapter.OnItemClickListener onItemClickListener = new GroupAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(getContext(), GroupActivity.class);
            intent.putExtra("groupBean", groupListBeans.get(position));
            startActivity(intent);
        }
    };
}