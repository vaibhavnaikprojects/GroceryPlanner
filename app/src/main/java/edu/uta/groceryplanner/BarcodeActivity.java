/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uta.groceryplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;
import edu.uta.groceryplanner.walmart.ManageProduct;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class BarcodeActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    private DatabaseReference mbase;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeActivity";
    private static ListBean list;
    private static List<ProductBean> productList;
    private static Barcode barcode;
    private static ProductBean product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);
        Intent readyListIntent = getIntent();
        list = (ListBean) readyListIntent.getSerializableExtra("listBean");
        productList = (List<ProductBean>) readyListIntent.getSerializableExtra("productList");
        statusMessage.setText("Scan products for List:"+list.getListName());
        findViewById(R.id.read_barcode).setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        product = new ProductBean();
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    barcodeValue.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);

                    Thread restThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                product = ManageProduct.getProduct(barcode.displayValue);
                            } catch (ParseException e) {
                                Log.d("ParseException",e.getMessage());
                            }
                        }
                    });
                    restThread.start();
                    try {
                        restThread.join();
                    } catch (InterruptedException e) {
                        Log.d("Interrupt Exception",e.getMessage());
                    }
                    if(product != null) {
                        mbase = FirebaseDatabase.getInstance().getReference("Products");
                        //Log.d("product",productList.get(i).getProductName());
                        mbase.child(list.getListId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    if ("uncheck".equalsIgnoreCase(d.child("status").getValue().toString())) {
                                        String pname = d.child("productName").getValue().toString();
                                        String[] pwords = pname.split(" ");
                                        int flag = 0;
                                        for (String s : pwords) {
                                            if (!product.getProductName().toLowerCase().contains(s.toLowerCase())) {
                                                flag = 1;
                                                break;
                                            }
                                        }
                                        if (flag == 0) {
                                            Map<String, Object> m = new HashMap<String, Object>();
                                            m.put("status", "check");
                                            m.put("cost", (product.getCost() * Double.parseDouble((String) d.child("quantity").getValue())));
                                            m.put("rate", product.getCost());
                                            mbase.child(list.getListId()).child(d.getKey().toString()).updateChildren(m);
                                            break;
                                        }
                                    }
                                }
                                finish();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        finish();
                    }
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}