package edu.uta.groceryplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import edu.uta.groceryplanner.adapters.InfoAdapter;
import edu.uta.groceryplanner.beans.InfoItemBean;

public class InfoFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<InfoItemBean> infoItemBeanList;
    private FirebaseAuth firebaseAuth;
    private static final int REQUEST_INVITE = 0;
    private static final int RESULT_OK = 0;

    public InfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth==null){
            startActivity(new Intent(getContext(),LoginActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_info, container, false);
        recyclerView=view.findViewById(R.id.infoRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        infoItemBeanList=new ArrayList<>();
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_share_black_24dp,"Invite People"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_password_color_black_24dp,"Reset Password"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_help_black_24dp,"Help"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_about_information_black_24dp,"About"));
        infoItemBeanList.add(new InfoItemBean(R.drawable.ic_signout_new_black_24dp,"Sign out"));
        adapter=new InfoAdapter(infoItemBeanList,getContext(),firebaseAuth,onItemClickListener);
        recyclerView.setAdapter(adapter);
        return view;
    }

    InfoAdapter.OnItemClickListener onItemClickListener=new InfoAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    sendInvite();
                    break;
                case 1:
                    sendResetPasswordEmail(firebaseAuth.getCurrentUser().getEmail());
                    break;
                case 2:
                    startActivity(new Intent(getContext(),HelpActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getContext(),AboutActivity.class));
                    break;
                case 4:
                    Toast.makeText(getContext(), "Signing out", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    firebaseAuth.signOut();
                    (getActivity()).finish();
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    break;
            }
        }
    };
    public void sendResetPasswordEmail(String email)
    {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            Toast.makeText(getContext(),"Email Sent.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void sendInvite(){
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setEmailHtmlContent(getString(R.string.invitation_html_content))
                .setEmailSubject(getString(R.string.invitation_email_subject))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

}
