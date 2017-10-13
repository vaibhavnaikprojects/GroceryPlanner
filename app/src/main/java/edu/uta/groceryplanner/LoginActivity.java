package edu.uta.groceryplanner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignin;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        textViewSignin=(TextView) findViewById(R.id.textViewSignin);
        progressBar=(ProgressBar) findViewById(R.id.progressBarRegister);

        buttonLogin.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==buttonLogin){
            signinUser(view);
        }
        else{
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }
    private void signinUser(final View view) {
        String email=editTextEmail.getText().toString().trim();
        String pass=editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            Snackbar.make(view, "Enter Email", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Snackbar.make(view,"Enter Password", Snackbar.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Snackbar.make(view, "Signin Successfully: " + task.getResult().getUser(), Snackbar.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else {
                            Snackbar.make(view, "Signin Failed: "+task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);

    }
}

