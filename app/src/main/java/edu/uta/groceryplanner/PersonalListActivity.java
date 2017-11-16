package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.ProductAdapter;
import edu.uta.groceryplanner.beans.ProductBean;

public class PersonalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ProductBean> beanList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getBaseContext(),LoginActivity.class));
        }
        setContentView(R.layout.activity_personal_list);
        recyclerView= (RecyclerView) findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        beanList=new ArrayList<ProductBean>();/*
        beanList.add(new ProductBean(1,"Milk",1,"2"));
        beanList.add(new ProductBean(1,"Onion",1,"2 lbs"));
        beanList.add(new ProductBean(1,"Cabbage",1,"4"));
        beanList.add(new ProductBean(1,"Apple",1,"10"));
        beanList.add(new ProductBean(1,"Potato Wedges",1,"1"));
        beanList.add(new ProductBean(1,"Pepsi",1,"32 oz"));
        beanList.add(new ProductBean(1,"Vegetable Oil",1,"64 oz"));
        beanList.add(new ProductBean(1,"Potato",2,"5 lbs"));

        beanList.add(new ProductBean(1,"Milk",1,"2"));
        beanList.add(new ProductBean(1,"Onion",1,"2 lbs"));
        beanList.add(new ProductBean(1,"Cabbage",1,"4"));
        beanList.add(new ProductBean(1,"Apple",1,"10"));
        beanList.add(new ProductBean(1,"Potato Wedges",1,"1"));
        beanList.add(new ProductBean(1,"Pepsi",1,"32 oz"));
        beanList.add(new ProductBean(1,"Vegetable Oil",1,"64 oz"));
        beanList.add(new ProductBean(1,"Potato",2,"5 lbs"));

        beanList.add(new ProductBean(1,"Milk",1,"2"));
        beanList.add(new ProductBean(1,"Onion",1,"2 lbs"));
        beanList.add(new ProductBean(1,"Cabbage",1,"4"));
        beanList.add(new ProductBean(1,"Apple",1,"10"));
        beanList.add(new ProductBean(1,"Potato Wedges",1,"1"));
        beanList.add(new ProductBean(1,"Pepsi",1,"32 oz"));
        beanList.add(new ProductBean(1,"Vegetable Oil",1,"64 oz"));
        beanList.add(new ProductBean(1,"Potato",2,"5 lbs"));*/

        adapter=new ProductAdapter(beanList,getBaseContext(),firebaseAuth);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}
