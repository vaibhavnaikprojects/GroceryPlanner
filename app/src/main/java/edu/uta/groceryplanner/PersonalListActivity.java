package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class PersonalListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ProductBean> productBeans;
    private FirebaseAuth firebaseAuth;
    private EditText mTextListName;
    DatabaseReference listRef, productsRef;
    private ImageButton imageButtonMenu, imageButtonAddPredifined;

    ListBean listBean;
    String listId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        setContentView(R.layout.activity_personal_list);
        listRef = FirebaseDatabase.getInstance().getReference("Lists").child(firebaseAuth.getCurrentUser().getUid());
        Intent intent = getIntent();
        listBean = (ListBean) intent.getSerializableExtra("list");
        if (listBean == null) {
            setTitle("New List");
            listId = listRef.push().getKey();
            listBean = new ListBean(listId, "", getCurrentDate(), getCurrentDate(), firebaseAuth.getCurrentUser().getUid(), "Personal", "draft", 0, null, 0);
            listRef.child(listId).setValue(listBean);
            productsRef = FirebaseDatabase.getInstance().getReference("Products").child(listId);
        } else {
            setTitle(listBean.getListName());
            listId = listBean.getListId();
            productsRef = FirebaseDatabase.getInstance().getReference("Products").child(listBean.getListId());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextListName = findViewById(R.id.textListName);
        if (!"".equalsIgnoreCase(listBean.getListName()))
            mTextListName.setText(listBean.getListName());
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        productBeans = new ArrayList<>();
        imageButtonMenu = findViewById(R.id.imageButtonMenu);
        imageButtonAddPredifined = findViewById(R.id.imageButtonAddPredifined);
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        imageButtonAddPredifined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.list_menu, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        TextView listMenuTitle = view.findViewById(R.id.menuTitle);
        TextView listMenuReady = view.findViewById(R.id.listMenuReady);
        TextView listMenuShare = view.findViewById(R.id.listMenuShare);
        TextView listMenuDelete = view.findViewById(R.id.listMenuDelete);
        listMenuTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listMenuReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBean.setUpdatedDate(getCurrentDate());
                listBean.setListState("ready");
                listRef.child(listId).setValue(listBean);
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        listMenuShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listMenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listRef.child(listId).removeValue();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ok_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test", "" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu_check:
                if (mTextListName.getText() == null || "".equalsIgnoreCase(String.valueOf(mTextListName.getText()))){
                    mTextListName.setError("Please enter list name", getResources().getDrawable(R.drawable.ic_warning_black_24dp));
                    return super.onOptionsItemSelected(item);
                }else {
                    if ("".contentEquals(listBean.getListName())) {
                        listBean.setListName(mTextListName.getText().toString());
                        listBean.setUpdatedDate(getCurrentDate());

                    } else {
                        listBean.setUpdatedDate(getCurrentDate());
                    }
                    listRef.child(listId).setValue(listBean);
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                break;
            default:
                if ("".contentEquals(listBean.getListName())) {
                    listBean.setListName("New List");
                    listBean.setUpdatedDate(getCurrentDate());
                    listRef.child(listId).setValue(listBean);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    @Override
    public void onStart() {
        if (productsRef != null) {
            productListener();
        }
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
                adapter = new ProductAdapter(productBeans, getApplicationContext());
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*PersonalListActivity.OnItemClickListener onItemClickListener=new PersonalListActivity.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ListBean listBean=beanList.get(position);
            if("personal".equalsIgnoreCase(listBean.getListType())) {
                Intent intent = new Intent(getContext(), PersonalListActivity.class);
                intent.putExtra("list", listBean);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getContext(), GroupListActivity.class);
                intent.putExtra("list", listBean);
                startActivity(intent);
            }
        }
    };*/
}

