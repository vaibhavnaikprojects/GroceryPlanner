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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.BuyListAdapter;
import edu.uta.groceryplanner.beans.ListBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class DraftFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListBean> beanList;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton mainFab,personalFab,groupFab,createGroupFab;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference draftListRef = database.getReference("draftList");
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_draft, container, false);
        recyclerView=view.findViewById(R.id.draftRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        beanList=new ArrayList<ListBean>();
        draftListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<ListBean>> listBeanIndicator =new GenericTypeIndicator<List<ListBean>>(){};
                beanList=dataSnapshot.getValue(listBeanIndicator);
                Log.d("test","true"+beanList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("test","false");
            }
        });
        adapter=new BuyListAdapter(beanList,getContext(),firebaseAuth);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        mainFab = view.findViewById(R.id.mainFab);
        personalFab = view.findViewById(R.id.personalFab);
        groupFab = view.findViewById(R.id.groupFab);
        createGroupFab=view.findViewById(R.id.createGroupFab);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_backward);
        mainFab.setOnClickListener(this);
        personalFab.setOnClickListener(this);
        groupFab.setOnClickListener(this);
        createGroupFab.setOnClickListener(this);
        //adapter.setOnItemClickListener(onItemClickListener);
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
            case R.id.createGroupFab:
                break;
        }
    }
    public void animateFAB() {

        if (isFabOpen) {
            mainFab.startAnimation(rotate_backward);
            personalFab.startAnimation(fab_close);
            groupFab.startAnimation(fab_close);
            createGroupFab.startAnimation(fab_close);
            personalFab.setClickable(false);
            groupFab.setClickable(false);
            createGroupFab.setClickable(false);
            isFabOpen = false;
        } else {
            mainFab.startAnimation(rotate_forward);
            personalFab.startAnimation(fab_open);
            groupFab.startAnimation(fab_open);
            createGroupFab.startAnimation(fab_open);
            personalFab.setClickable(true);
            groupFab.setClickable(true);
            createGroupFab.setClickable(true);
            isFabOpen = true;
        }
    }


    /*adapter.OnItemClickListener onItemClickListener=new adapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {

        }
    };*/

    /*private List<ListBean> buildList(){
        beanList=new ArrayList<ListBean>();
        beanList.add(new ListBean(1,"September Group 1 List","09/01/2017","09/05/2017","vaibhavsnaik09","Group",1,"UTA Roomies","Banana\nCilantro"));
        beanList.add(new ListBean(2,"October Group 1 List","10/01/2017","10/07/2017","vaibhavsnaik09","Group",1,"UTA Roomies","Mushrooms\nCucumber\nTomato"));
        beanList.add(new ListBean(3,"October Personal List","10/04/2017","10/10/2017","vaibhavsnaik09","Personal",0,"","Jam\nBread"));
        beanList.add(new ListBean(4,"August Personal List","08/02/2017","08/02/2017","vaibhavsnaik09","Personal",0,"","Oreo Cereal"));
        beanList.add(new ListBean(5,"September Personal List","09/01/2017","09/05/2017","vaibhavsnaik09","Personal",0,"","Pepsi\nMaggi\nPasta"));
        beanList.add(new ListBean(7,"September Group 2 List","09/02/2017","09/08/2017","vaibhavsnaik09","Group",2,"Meadow","Bread"));
        beanList.add(new ListBean(8,"October Group 2 List","10/10/2017","10/15/2017","vaibhavsnaik09","Group",2,"Meadow","Bread\nJam\nKetchup"));
        beanList.add(new ListBean(9,"August Group 2 List","08/01/2017","08/02/2017","vaibhavsnaik09","Group",2,"Meadow","Onion\nPotato\nBread"));
        return beanList;
    }*/
}
