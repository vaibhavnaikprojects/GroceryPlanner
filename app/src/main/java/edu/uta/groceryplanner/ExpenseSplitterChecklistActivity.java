package edu.uta.groceryplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.ExpenseSplitterChecklistAdapter;
import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;

public class ExpenseSplitterChecklistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private FirebaseAuth firebaseAuth;
    private List<ProductBean> productBeanList;
    private TextView textViewIndividualAmount;
    private TextView textViewTotalAmount;
    private TextView textViewListName;
    private DatabaseReference productRef;
    private DatabaseReference friendsRef;
    private String listId;
    private ListBean listBean;
    private Double totalProductCost = 0.00;
    private List<String> friendsList;
    private CardView cardView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_splitter);
        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        Intent intent=getIntent();
        listBean=(ListBean)intent.getSerializableExtra("list");
        productRef = FirebaseDatabase.getInstance().getReference("Products");
        friendsRef = FirebaseDatabase.getInstance().getReference("friends");
        textViewListName = findViewById(R.id.textViewListName);
        textViewListName.setText(listBean.getListName());
        textViewIndividualAmount = findViewById(R.id.textViewIndividualAmount);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        recyclerView= findViewById(R.id.recyclerViewExpenseChecklist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productBeanList = new ArrayList<>();
        friendsList = new ArrayList<>();
        cardView2 = findViewById(R.id.cardView2);

        if(listBean.getListType().equalsIgnoreCase("personal")) {
                textViewIndividualAmount.setVisibility(View.GONE);
                textViewTotalAmount.setPadding(50,50,50,50);
                cardView2.setMinimumHeight(150);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        productRef.child(listBean.getListId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                    ProductBean productBean = dataSnap.getValue(ProductBean.class);
                    if(productBean.getStatus().equalsIgnoreCase("Check")) {
                        Log.d("PRODUCT NAME","PROUCT NAME:"+productBean.getProductName());
                        totalProductCost += productBean.getCost();
                        productBeanList.add(productBean);
                    }
                }
                textViewIndividualAmount.setText("Individual Expense: "+Math.round(totalProductCost/5.0)+"$");
                textViewTotalAmount.setText("Total Expense: "+Math.round(totalProductCost)+"$");
                getFriends();
                adapter=new ExpenseSplitterChecklistAdapter(productBeanList,getApplicationContext(),firebaseAuth);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getFriends(){
        friendsRef.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                    FriendsBean friendsBean = dataSnap.getValue(FriendsBean.class);
                    friendsList.add(friendsBean.getFriendName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "Exception while fetching all friends :" + databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ok_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Saved expenses", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        return true;
    }
}
