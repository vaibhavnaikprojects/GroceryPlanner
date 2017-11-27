package edu.uta.groceryplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.BuyListAdapter;
import edu.uta.groceryplanner.beans.ListBean;

public class ReadyFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListBean> beanList;
    private FirebaseAuth firebaseAuth;
    DatabaseReference listRef;
    public ReadyFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_ready, container, false);
        recyclerView=view.findViewById(R.id.readyRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        beanList=new ArrayList<ListBean>();

        return view;
    }
    BuyListAdapter.OnItemClickListener onItemClickListener=new BuyListAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {
            ListBean listBean=beanList.get(position);
            Intent intent = new Intent(getContext(), ReadyListActivity.class);
            intent.putExtra("list", listBean);
            startActivity(intent);
        }
    };
    @Override
    public void onStart() {
        listRef.orderByChild("listState").equalTo("ready").addValueEventListener(new ValueEventListener() {
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
}
