package edu.uta.groceryplanner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.service.autofill.Dataset;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

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
import java.util.Map;

import edu.uta.groceryplanner.beans.FriendsBean;
import edu.uta.groceryplanner.beans.GroupBean;
import edu.uta.groceryplanner.beans.ListBean;

public class AddBillActivity extends AppCompatActivity {

    private MultiAutoCompleteTextView autoTextPickName;
    private AutoCompleteTextView autoTextPaidBy;
    private MultiAutoCompleteTextView autoTextSplitBetween;
    private EditText editTextDescription;
    private EditText editTextExpense;
    private DatabaseReference databaseReference, friendsRef;
    private FirebaseAuth firebaseAuth;
    private List<String> friendsList;
    double owePrice,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Bill");
        setContentView(R.layout.activity_add_bill);
        autoTextPickName = findViewById(R.id.autoTextPickName);
        autoTextPaidBy = findViewById(R.id.autoTextPaidBy);
        autoTextSplitBetween = findViewById(R.id.autoTextSplitBetween);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextExpense = findViewById(R.id.editTextExpense);
        firebaseAuth = FirebaseAuth.getInstance();
        friendsList = new ArrayList<String>();
        databaseReference = FirebaseDatabase.getInstance().getReference("friends").child(firebaseAuth.getCurrentUser().getUid());
        friendsRef = FirebaseDatabase.getInstance().getReference("friends");

        fetchAllFriends();
        createAutoCompleteView();
    }

    public void addExpenseToFriends(){

        final String[] friendNameList = autoTextPickName.getText().toString().split(",");
        String description = editTextDescription.getText().toString();
        final double expense = Double.valueOf(editTextExpense.getText().toString());
        number = friendNameList.length;
        final double individual=expense/number;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    final FriendsBean friendsBean = snap.getValue(FriendsBean.class);
                    for(final String friendName: friendNameList) {
                        if (friendsBean.getFriendName().equalsIgnoreCase(friendName.trim())) {
                            if(friendsBean.getOweStatus().equalsIgnoreCase("Owes You")){
                                double actual=friendsBean.getOwePrice()+individual;
                                if(actual>0){
                                    friendsBean.setOwePrice(actual);
                                    friendsBean.setOweStatus("Owes You");
                                }
                            }
                            else if(friendsBean.getOweStatus().equalsIgnoreCase("You Owe")){
                                double actual=friendsBean.getOwePrice()-individual;
                                if(actual<0) {
                                    friendsBean.setOwePrice(Math.abs(actual));
                                    friendsBean.setOweStatus("Owes You");
                                }else if(actual>0){
                                    friendsBean.setOwePrice(actual);
                                    friendsBean.setOweStatus("You Owe");
                                }
                                else{
                                    friendsBean.setOwePrice(0);
                                    friendsBean.setOweStatus("resolved");
                                }
                            }
                            else{
                                double actual=friendsBean.getOwePrice()+individual;
                                if(actual>0){
                                    friendsBean.setOwePrice(actual);
                                    friendsBean.setOweStatus("Owes You");
                                }
                            }
                            databaseReference.child(friendsBean.getFriendId()).setValue(friendsBean);
                            friendsRef.child(friendsBean.getFriendId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        FriendsBean friendsBean1 = dataSnapshot1.getValue(FriendsBean.class);
                                        if(friendsBean1.getFriendId().equals(firebaseAuth.getCurrentUser().getUid())){
                                            if(friendsBean1.getOweStatus().equalsIgnoreCase("Owes You")){
                                                double actual=friendsBean1.getOwePrice()-individual;
                                                Log.d("owe prcie","price:"+actual);
                                                if(actual<0.0) {
                                                    friendsBean1.setOwePrice(Math.abs(actual));
                                                    friendsBean1.setOweStatus("You Owe");
                                                }else if(actual>0.0){
                                                    friendsBean1.setOwePrice(actual);
                                                    friendsBean1.setOweStatus("Owes you");
                                                }
                                                else{
                                                    friendsBean1.setOwePrice(0);
                                                    friendsBean1.setOweStatus("resolved");
                                                }
                                            }
                                            else if(friendsBean1.getOweStatus().equalsIgnoreCase("You Owe")){
                                                double actual=friendsBean1.getOwePrice()+individual;
                                                if(actual<0) {
                                                    friendsBean1.setOwePrice(Math.abs(actual));
                                                    friendsBean1.setOweStatus("You Owe");
                                                }else if(actual>0){
                                                    friendsBean1.setOwePrice(actual);
                                                    friendsBean1.setOweStatus("Owes you");
                                                }
                                                else{
                                                    friendsBean1.setOwePrice(0);
                                                    friendsBean1.setOweStatus("resolved");
                                                }

                                            }
                                            else{
                                                double actual=friendsBean1.getOwePrice()+individual;
                                                if(actual>0){
                                                    friendsBean.setOwePrice(actual);
                                                    friendsBean.setOweStatus("You Owe");
                                                }
                                            }
                                            friendsBean1.setOwePrice(friendsBean1.getOwePrice() + (expense/number));
                                            friendsBean1.setOweStatus("You Owe");
                                            friendsRef.child(friendsBean.getFriendId()).child(friendsBean1.getFriendId()).setValue(friendsBean1);
                                            break;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void fetchAllFriends() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

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

    public void createAutoCompleteView() {

        ArrayAdapter<String> searchContactAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        MultiAutoCompleteTextView searchContactView = findViewById(R.id.autoTextPickName);
        searchContactView.setAdapter(searchContactAdapter);
        searchContactView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        ArrayAdapter<String> paidByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        AutoCompleteTextView paidByView = findViewById(R.id.autoTextPaidBy);
        paidByView.setAdapter(paidByAdapter);


        ArrayAdapter<String> splitBetweenAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, friendsList);
        MultiAutoCompleteTextView splitBetweenView = findViewById(R.id.autoTextSplitBetween);
        splitBetweenView.setAdapter(splitBetweenAdapter);
        splitBetweenView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ok_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addExpenseToFriends();
        Toast.makeText(this, "Saved Bill", Toast.LENGTH_SHORT).show();
        finish();
        return true;
    }
}
