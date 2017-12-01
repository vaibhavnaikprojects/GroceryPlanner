package edu.uta.groceryplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uta.groceryplanner.adapters.PredefinedCategoryAdapter;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;

public class PredefinedProductActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference predefinedItemRef,productsRef;
    private ListBean listBean;
    private List<ProductBean> productBeans;
    private List<String> productCategories;
    private HashMap<Integer, List<String>> predefinedCategoryListMap;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_product);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Select Products");
        Intent intent = getIntent();
        listBean = (ListBean) intent.getSerializableExtra("listBean");
        productBeans = (List<ProductBean>) intent.getSerializableExtra("productBeans");
        if (productBeans == null)
            productBeans = new ArrayList<>();
        productsRef = FirebaseDatabase.getInstance().getReference("Products").child(listBean.getListId());
        predefinedItemRef = FirebaseDatabase.getInstance().getReference("Product Items");
        productCategories = new ArrayList<>();
        predefinedCategoryListMap = new HashMap<>();
        recyclerView = findViewById(R.id.predefinedCategoryRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        predefinedItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productCategories.clear();
                predefinedCategoryListMap.clear();
                int i=0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    productCategories.add(data.getKey());
                    final List<String> predefinedItems = new ArrayList<>();
                    for(DataSnapshot data1 : data.getChildren()) {
                        predefinedItems.add(data1.child("Product Item").getValue().toString());
                    }
                    predefinedCategoryListMap.put(i++, predefinedItems);
                }
                adapter = new PredefinedCategoryAdapter(getApplicationContext(),productCategories,onItemClickListener);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        super.onStart();
    }
    PredefinedCategoryAdapter.OnItemClickListener onItemClickListener=new PredefinedCategoryAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final View view, final int position) {
            List<String> strings=predefinedCategoryListMap.get(position);
            View modal = getLayoutInflater().inflate(R.layout.predefined_products, null);
            final ListView listView =modal.findViewById(R.id.lv);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_multiple_choice,strings);
            listView.setAdapter(arrayAdapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            AlertDialog dialog = new AlertDialog.Builder(PredefinedProductActivity.this)
                    .setTitle(productCategories.get(position))
                    .setView(modal)
                    .setCancelable(false)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int arg) {
                            SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                            if (checkedItems != null) {
                                for (int i=0; i<checkedItems.size(); i++) {
                                    if (checkedItems.valueAt(i)) {
                                        String item = listView.getAdapter().getItem(
                                                checkedItems.keyAt(i)).toString();
                                        if(!productBeans.contains(new ProductBean(item))) {
                                            String id = productsRef.push().getKey();
                                            ProductBean productBean = new ProductBean(id, item, productCategories.get(position), "1", "uncheck");
                                            productsRef.child(id).setValue(productBean);
                                        }
                                    }
                                }
                                Snackbar.make(view,"Products Added", Snackbar.LENGTH_SHORT);
                            }
                        }
                    })
                    .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
        }
    };
}
