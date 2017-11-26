package edu.uta.groceryplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;

public class InviteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView editTextEmail;
    private Button btnSendInvite;
    private FirebaseAuth firebaseAuth;
    private static final int REQUEST_INVITE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        btnSendInvite = findViewById(R.id.btnSendInvite);

        btnSendInvite.setOnClickListener(this);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(homeIntent, 0);
        return true;

    }

    @Override
    public void onClick(View view) {
        if(view == btnSendInvite){
            sendInvite();
        }
    }

    public void sendInvite(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setEmailHtmlContent(getString(R.string.invitation_html_content))
                .setEmailSubject(getString(R.string.invitation_email_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("TAG", "onActivityResult: sent invitation " + id);
                }
            } else {
                Toast.makeText(getApplicationContext(),"Failed to send invite",Toast.LENGTH_LONG).show();
            }
        }
    }
}
