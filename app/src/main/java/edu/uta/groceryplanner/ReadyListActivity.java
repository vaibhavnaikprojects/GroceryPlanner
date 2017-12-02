package edu.uta.groceryplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.uta.groceryplanner.adapters.ReadyChecklistAdapter;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;

public class ReadyListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ProductBean> productBeans;
    private FirebaseAuth firebaseAuth;
    private TextView mTextListName;
    DatabaseReference listRef,productsRef;
    ListBean listBean;
    private FloatingActionButton mainFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_list);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error"+Thread.currentThread().getStackTrace()[2],paramThrowable.getLocalizedMessage());
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        Intent intent = getIntent();
        listBean = (ListBean) intent.getSerializableExtra("list");
        setTitle(listBean.getListName());
        listRef=FirebaseDatabase.getInstance().getReference("Lists").child(firebaseAuth.getCurrentUser().getUid());
        productsRef = FirebaseDatabase.getInstance().getReference("Products").child(listBean.getListId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextListName = findViewById(R.id.textListName);
        mainFab = findViewById(R.id.mainFab);
        mTextListName.setText(listBean.getListName());
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        productBeans = new ArrayList<>();
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // from here calls to barcode scanner activity goes.
                Log.i("ReadyListActivity","inside listener");
                Intent barcodeScannerIntent = new Intent(ReadyListActivity.this, BarcodeActivity.class);
                barcodeScannerIntent.putExtra("listBean",(Serializable) listBean);
                barcodeScannerIntent.putExtra("productList", (Serializable) productBeans);
                startActivityForResult(barcodeScannerIntent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("came abck to ready list","here in ready list from barcode activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ready, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test", "" + item.getItemId());
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
        TextView listMenuDone = view.findViewById(R.id.listMenuDone);
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
        listMenuDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBean.setUpdatedDate(getCurrentDate());
                listBean.setListState("draft");
                listRef.child(listBean.getListId()).setValue(listBean);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        listMenuDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBean.setUpdatedDate(getCurrentDate());
                listBean.setListState("complete");
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
                adapter = new ReadyChecklistAdapter(productBeans, getApplicationContext(),onItemClickListener);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ReadyChecklistAdapter.OnItemClickListener onItemClickListener = new ReadyChecklistAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            final ProductBean productBean = productBeans.get(position);
            View modal = getLayoutInflater().inflate(R.layout.product_ready_modal, null);
            final TextView title=modal.findViewById(R.id.textView2);
            final EditText rate=modal.findViewById(R.id.rate);
            AlertDialog dialog = new AlertDialog.Builder(ReadyListActivity.this)
                    .setView(modal)
                    .setCancelable(false)
                    .setPositiveButton("Add", null)
                    .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            productBean.setRate(0);
                            productBean.setCost(0);
                            productBean.setStatus("uncheck");
                            productsRef.child(productBean.getProductId()).setValue(productBean);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    title.setText(productBean.getProductName());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(rate.getText().toString().equalsIgnoreCase(""))
                                rate.setError("Enter Rate");
                            else{
                                productBean.setRate(Double.parseDouble(rate.getText().toString()));
                                productBean.setCost(Double.parseDouble(rate.getText().toString())*Double.parseDouble(productBean.getQuantity()));
                                productBean.setStatus("check");
                                productsRef.child(productBean.getProductId()).setValue(productBean);
                                dialog.dismiss();
                            }
                        }
                    });
                }
            });
            dialog.show();
        }
    };
    private String getCurrentDate() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
