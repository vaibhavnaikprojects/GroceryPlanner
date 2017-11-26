package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.uta.groceryplanner.beans.UserBean;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignin;
    private ProgressBar progressBar;
    private SignInButton buttonGoogleSignIn;
    private LoginButton buttonFacebookSignIn;
    private FirebaseAuth firebaseAuth;
    private int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private CallbackManager mCallbackManager;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        firebaseAuth = FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference("users");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        };

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignin =findViewById(R.id.textViewSignin);
        progressBar = findViewById(R.id.progressBarRegister);
        buttonGoogleSignIn = findViewById(R.id.buttonGoogleSignIn);
        buttonFacebookSignIn = findViewById(R.id.buttonFacebookSignIn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Something went wrong." + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        buttonGoogleSignIn.setOnClickListener(this);
        buttonFacebookSignIn.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            signInUser(view);
        }
        if (view == buttonGoogleSignIn) {
            googleSignIn();
        }
        if(view == buttonFacebookSignIn){
            facebookSignIn();
        }
        if(view == textViewSignin) {
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

    //Sign in with username and password
    private void signInUser(final View view) {
        String email = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter Email",getResources().getDrawable(R.drawable.ic_warning_black_24dp));
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            editTextPassword.setError("Enter Password",getResources().getDrawable(R.drawable.ic_warning_black_24dp));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Signin Successfully: " + task.getResult().getUser(), Snackbar.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            Snackbar.make(view, "Signin Failed: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
        progressBar.setVisibility(View.INVISIBLE);

    }

    //Facebook sign in
    public void facebookSignIn(){
    mCallbackManager =CallbackManager.Factory.create();
        buttonFacebookSignIn.setReadPermissions("email","public_profile");
        buttonFacebookSignIn.registerCallback(mCallbackManager,new FacebookCallback<LoginResult>()

    {
        @Override
        public void onSuccess (LoginResult loginResult){
        Log.d("TAG", "facebook:onSuccess:" + loginResult);
        handleFacebookAccessToken(loginResult.getAccessToken());

    }

        @Override
        public void onCancel () {
        Log.d("TAG", "facebook:onCancel");
    }

        @Override
        public void onError (FacebookException error){
        Log.d("TAG", "facebook:onError", error);
    }
    });
}


    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getApplicationContext(), "Authentication error." + result.getStatus(), Toast.LENGTH_LONG).show();
            }
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            userRef.orderByChild("emailId").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                    }else{
                                        UserBean userBean=new UserBean(user.getUid(),user.getDisplayName(),user.getEmail(),"active");
                                        userRef.child(user.getUid()).setValue(userBean);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            userRef.orderByChild("emailId").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child(user.getUid()).exists()) {
                                    }else{
                                        UserBean userBean=new UserBean(user.getUid(),user.getDisplayName(),user.getEmail(),"active");
                                        userRef.child(user.getUid()).setValue(userBean);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed."+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

