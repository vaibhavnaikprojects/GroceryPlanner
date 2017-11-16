package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    DatabaseReference draftListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth == null) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        setContentView(R.layout.activity_personal_list);
        draftListRef = FirebaseDatabase.getInstance().getReference("draftList");
        setTitle("New List");
        getSupportActionBar().setIcon(R.drawable.ic_person_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextListName = (EditText) findViewById(R.id.textListName);
        recyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        productBeans = new ArrayList<ProductBean>();
        adapter = new ProductAdapter(productBeans, getBaseContext(), firebaseAuth);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ok_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test",""+item.getItemId());
        switch (item.getItemId()){
            case R.id.menu_check:
                if (mTextListName.getText() == null || "".equalsIgnoreCase(String.valueOf(mTextListName.getText())))
                    Toast.makeText(getBaseContext(), "Please enter list name", Toast.LENGTH_LONG);
                else{
                    String id=draftListRef.push().getKey();
                    String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    ListBean listBean=new ListBean(id,String.valueOf(mTextListName.getText()), date, date, firebaseAuth.getCurrentUser().getEmail(), "Personal",0,null);
                    listBean.setProductBeans(productBeans);
                    draftListRef.child(id).setValue(listBean);
                }
                break;
        }
        finish();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return super.onOptionsItemSelected(item);
    }
}

