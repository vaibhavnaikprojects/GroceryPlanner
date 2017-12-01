package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uta.groceryplanner.adapters.ProductAdapter;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;

public class CompleteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ProductBean> productBeans;
    private FirebaseAuth firebaseAuth;
    private TextView mTextListName;
    DatabaseReference listRef,productsRef;
    ListBean listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        Intent intent = getIntent();
        listBean = (ListBean) intent.getSerializableExtra("list");
        setTitle(listBean.getListName());
        listRef= FirebaseDatabase.getInstance().getReference("Lists").child(firebaseAuth.getCurrentUser().getUid());
        productsRef = FirebaseDatabase.getInstance().getReference("Products").child(listBean.getListId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextListName = findViewById(R.id.textListName);
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        productBeans = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ready, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                openDialog();
                break;
            case R.id.menu_check:
                finish();
                break;
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.ready_list_menu, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        TextView listMenuTitle = view.findViewById(R.id.menuTitle);
        TextView listMenuDraft = view.findViewById(R.id.listMenuDraft);
        TextView listMenuDelete = view.findViewById(R.id.listMenuDelete);
        listMenuTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listMenuDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBean.setUpdatedDate(getCurrentDate());
                listBean.setListState("draft");
                listRef.child(listBean.getListId()).setValue(listBean);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        listMenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listRef.child(listBean.getListId()).removeValue();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        productListener();
        super.onStart();
    }

    private void productListener() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productBeans.clear();
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    ProductBean productBean = dataSnap.getValue(ProductBean.class);
                    productBeans.add(productBean);
                }
                adapter = new ProductAdapter(productBeans, getApplicationContext(),null);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String getCurrentDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
