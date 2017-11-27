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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uta.groceryplanner.beans.ListBean;

public class AddBillActivity extends AppCompatActivity {

    private AutoCompleteTextView autoTextPickName;
    private AutoCompleteTextView autoTextPaidBy;
    private AutoCompleteTextView autoTextSplitBetween;

    String[] names={"Prakhar","Vaibhav","Abhijit","Nikhil","Reva"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        autoTextPickName = findViewById(R.id.autoTextPickName);
        autoTextPaidBy = findViewById(R.id.autoTextPaidBy);
        autoTextSplitBetween = findViewById(R.id.autoTextSplitBetween);

        createAutoCompleteView();

    }

    public void createAutoCompleteView(){
        ArrayAdapter<String> searchContactAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        AutoCompleteTextView searchContactView = (AutoCompleteTextView)
                findViewById(R.id.autoTextPickName);
        searchContactView.setAdapter(searchContactAdapter);

        ArrayAdapter<String> paidByAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
        AutoCompleteTextView paidByView = (AutoCompleteTextView)
                findViewById(R.id.autoTextPaidBy);
        paidByView.setAdapter(paidByAdapter);

        ArrayAdapter<String> splitBetweenAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, names);
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
