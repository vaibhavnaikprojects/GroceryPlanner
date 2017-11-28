package edu.uta.groceryplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.ExpenseSplitterChecklistAdapter;
import edu.uta.groceryplanner.beans.ProductBean;

public class ExpenseSplitterChecklistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private List<ProductBean> productBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_splitter);
        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        recyclerView= findViewById(R.id.recyclerViewExpenseChecklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productBeanList=new ArrayList<ProductBean>();
        productBeanList.add(new ProductBean("1","Milk","1","2",10,20,"Available"));
        productBeanList.add(new ProductBean("2","Eggs","1","10",20,5,"Available"));
        productBeanList.add(new ProductBean("3","Spinach","1","1",10,40,"Available"));
        productBeanList.add(new ProductBean("4","Battery","1","6",10,10,"Available"));
        productBeanList.add(new ProductBean("5","Chicken","1","4",10,54,"Available"));
        productBeanList.add(new ProductBean("6","Pizza","1","1",10,15,"Available"));
        productBeanList.add(new ProductBean("7","Cheese","1","2",10,25,"Available"));

        adapter=new ExpenseSplitterChecklistAdapter(productBeanList,getApplicationContext(),firebaseAuth);
        recyclerView.setAdapter(adapter);

    }

}
