package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.uta.groceryplanner.beans.UserBean;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewSignin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName= findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextConfirmPassword=findViewById(R.id.editTextConfirmPassword);
        buttonRegister=findViewById(R.id.buttonRegister);
        textViewSignin=findViewById(R.id.textViewSignin);
        progressBar=findViewById(R.id.progressBarRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        userRef=FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onClick(View view) {
        if(view==buttonRegister){
            registerUser(view);
        }
        else{
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
    private void registerUser(final View view) {
        final String name=editTextName.getText().toString().trim();
        String email=editTextEmail.getText().toString().trim();
        String pass=editTextPassword.getText().toString().trim();
        String confirmPass=editTextConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            Snackbar.make(view, "Enter Email", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(pass)){
            Snackbar.make(view,"Enter Password", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else if(!pass.equalsIgnoreCase(confirmPass)){
            Snackbar.make(view,"Password does not match", Snackbar.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(name)){
            Snackbar.make(view,"Enter Name", Snackbar.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            final FirebaseUser user=firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(editTextName.getText().toString().trim())
                                    .build();
                            if(user!=null)
                                user.updateProfile(profileUpdate);
                            Snackbar.make(view, "Registered Successfully", Snackbar.LENGTH_SHORT).show();
                            final UserBean userBean=new UserBean(user.getUid(),name,user.getEmail(),"active");
                            userRef.orderByChild("emailId").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child(user.getUid()).exists()) {
                                    }else{
                                        userRef.child(user.getUid()).setValue(userBean);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else {
                            Snackbar.make(view, "Registeration Failed: "+task.getException()!=null?task.getException().getMessage():"", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);

    }
}
