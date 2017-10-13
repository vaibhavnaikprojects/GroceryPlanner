package edu.uta.groceryplanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.InfoAdapter;
import edu.uta.groceryplanner.beans.InfoItemBean;

public class InfoFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<InfoItemBean> infoItemBeanList;
    private FirebaseAuth firebaseAuth;
    public InfoFragment() {
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
        View view= inflater.inflate(R.layout.fragment_info, container, false);
        recyclerView=view.findViewById(R.id.infoRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        infoItemBeanList=new ArrayList<>();
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_share_black_24dp,"Invite People"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_password_color_black_24dp,"Reset Password"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_help_black_24dp,"Help"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_about_information_black_24dp,"About"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_signout_new_black_24dp,"Sign out"));
        adapter=new InfoAdapter(infoItemBeanList,getContext(),firebaseAuth);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
