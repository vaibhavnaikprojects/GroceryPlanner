package edu.uta.groceryplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.adapters.BuyListAdapter;
import edu.uta.groceryplanner.beans.ListBean;


/**
 * A simple {@link Fragment} subclass.
 */
public class DraftFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListBean> beanList;
    private FirebaseAuth firebaseAuth;

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
        adapter=new BuyListAdapter(beanList,getContext(),firebaseAuth);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //adapter.setOnItemClickListener(onItemClickListener);
        return view;
    }
    /*adapter.OnItemClickListener onItemClickListener=new adapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view, int position) {

        }
    };*/

}
