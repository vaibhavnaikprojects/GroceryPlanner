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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.BuyListAdapter;
import edu.uta.groceryplanner.beans.GroupBean;
import edu.uta.groceryplanner.beans.ListBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class DraftFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListBean> beanList;
    private List<GroupBean> groupBeans;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab,personalFab,groupFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    DatabaseReference listRef,groupRef;
    public DraftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
        listRef = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseAuth.getCurrentUser().getUid());
        groupRef=FirebaseDatabase.getInstance().getReference("Groups");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_draft, container, false);
        recyclerView=view.findViewById(R.id.draftRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        beanList=new ArrayList<>();
        mainFab = view.findViewById(R.id.mainFab);
        personalFab = view.findViewById(R.id.personalFab);
        groupFab = view.findViewById(R.id.groupFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        personalFab.setOnClickListener(this);
        groupFab.setOnClickListener(this);
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
                startActivity(new Intent(getContext(),PersonalListActivity.class));
                break;
            case R.id.groupFab:
                break;
        }
    }
    public void animateFAB() {

        if (isFabOpen) {
            mainFab.startAnimation(rotate_backward);
            personalFab.startAnimation(fab_close);
            groupFab.startAnimation(fab_close);
            personalFab.setClickable(false);
            groupFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotate_forward);
            personalFab.startAnimation(fab_open);
            groupFab.startAnimation(fab_open);
            personalFab.setClickable(true);
            groupFab.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onStart() {

        listRef.orderByChild("listState").equalTo("draft").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                beanList.clear();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    ListBean listBean=dataSnap.getValue(ListBean.class);
                    Log.v("listBean",listBean.toString());
                    beanList.add(listBean);
                }
                adapter=new BuyListAdapter(beanList,getContext(),firebaseAuth,onItemClickListener);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    BuyListAdapter.OnItemClickListener onItemClickListener=new BuyListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ListBean listBean=beanList.get(position);
            if("personal".equalsIgnoreCase(listBean.getListType())) {
                Intent intent = new Intent(getContext(), PersonalListActivity.class);
                intent.putExtra("list", listBean);
                startActivity(intent);
            }
            else{
                /*Intent intent = new Intent(getContext(), GroupListActivity.class);
                intent.putExtra("list", listBean);
                startActivity(intent);*/
            }
        }
    };
}
