package edu.uta.groceryplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName,editTextEmail,editTextPassword,editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewSignin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText) findViewById(R.id.editTextConfirmPassword);
        buttonRegister=(Button) findViewById(R.id.buttonRegister);
        textViewSignin=(TextView) findViewById(R.id.textViewSignin);
        progressBar=(ProgressBar) findViewById(R.id.progressBarRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
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
        String email=editTextEmail.getText().toString().trim();
        String pass=editTextPassword.getText().toString().trim();
        String confirmPass=editTextConfirmPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            Snackbar.make(view, "Enter Email", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Snackbar.make(view,"Enter Password", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(!pass.equalsIgnoreCase(confirmPass)){
            Snackbar.make(view,"Password does not match", Snackbar.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(editTextName.getText().toString().trim())
                                    .build();
                            user.updateProfile(profileUpdate);
                            Snackbar.make(view, "Registered Successfully", Snackbar.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else {
                            Snackbar.make(view, "Registeration Failed: "+task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);

    }
}
