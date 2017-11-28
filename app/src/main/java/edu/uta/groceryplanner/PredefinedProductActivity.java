package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.PredefinedCategoryAdapter;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.PredefinedCategory;

public class PredefinedProductActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    DatabaseReference predifinedCategoriesRef;
    ListBean listBean;
    List<PredefinedCategory> productCategories;
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
        Intent intent = getIntent();
        listBean = (ListBean) intent.getSerializableExtra("listBean");
        predifinedCategoriesRef=FirebaseDatabase.getInstance().getReference("Product Categories");
        productCategories=new ArrayList<>();
        recyclerView = findViewById(R.id.predefinedCategoryRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }


    @Override
    public void onStart() {
        predifinedCategoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productCategories.clear();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    String predefinedCategory = (String)dataSnap.child("Product Category").getValue();
                    productCategories.add(new PredefinedCategory(dataSnap.getKey(),predefinedCategory));
                }
                adapter = new PredefinedCategoryAdapter(productCategories, getApplicationContext(),onItemClickListener);
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
        public void onItemClick(View view, int position) {
            PredefinedCategory predefinedCategory=productCategories.get(position);
            Toast.makeText(PredefinedProductActivity.this, predefinedCategory.getCategoryName(), Toast.LENGTH_SHORT).show();
            /*Intent intent=new Intent(getApplicationContext(),PredefinedItemActivity.class);
            intent.putExtra("category",predefinedCategory);
            intent.putExtra("listId",listBean.getListId());
            intent.putExtra("nextCategory",productCategories.size()==(position+1)?null:productCategories.get(position+1));
            finish();
            startActivity(intent);*/
        }
    };

}
