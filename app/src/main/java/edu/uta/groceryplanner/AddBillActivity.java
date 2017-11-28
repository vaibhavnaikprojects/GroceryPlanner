package edu.uta.groceryplanner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.GroupBean;
import edu.uta.groceryplanner.beans.ListBean;

public class AddBillActivity extends AppCompatActivity {

    private AutoCompleteTextView autoTextPickName;
    private AutoCompleteTextView autoTextPaidBy;
    private AutoCompleteTextView autoTextSplitBetween;
    private DatabaseReference databaseReference;
    List<String> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        autoTextPickName = findViewById(R.id.autoTextPickName);
        autoTextPaidBy = findViewById(R.id.autoTextPaidBy);
        autoTextSplitBetween = findViewById(R.id.autoTextSplitBetween);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("friends");
        friendsList = new ArrayList<String>();
        fetchAllFriends();
        createAutoCompleteView();

    }

    public void fetchAllFriends(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap: dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnap1: dataSnap.getChildren()){
                        FriendsBean friendsBean = dataSnap1.getValue(FriendsBean.class);
                        friendsList.add(friendsBean.getFriendName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR","Exception while fetching all friends :"+databaseError.getMessage());
            }
        });
    }

    public void createAutoCompleteView(){
        ArrayAdapter<String> searchContactAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        AutoCompleteTextView searchContactView = (AutoCompleteTextView)
                findViewById(R.id.autoTextPickName);
        searchContactView.setAdapter(searchContactAdapter);

        ArrayAdapter<String> paidByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        AutoCompleteTextView paidByView = (AutoCompleteTextView)
                findViewById(R.id.autoTextPaidBy);
        paidByView.setAdapter(paidByAdapter);

        ArrayAdapter<String> splitBetweenAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        AutoCompleteTextView splitBetweenView = (AutoCompleteTextView)
                findViewById(R.id.autoTextSplitBetween);
        splitBetweenView.setAdapter(splitBetweenAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ok_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Saved Bill", Toast.LENGTH_SHORT).show();
        finish();
        return true;
    }
}
