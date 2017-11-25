package edu.uta.groceryplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.InviteActivity;
import edu.uta.groceryplanner.LoginActivity;
import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.RegisterActivity;
import edu.uta.groceryplanner.beans.InfoItemBean;

/**
 * Created by Vaibhav's Console on 10/11/2017.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<InfoItemBean> infoItemBeanList;
    private Context context;
    private FirebaseAuth firebaseAuth;
    public InfoAdapter(List<InfoItemBean> infoItemBeanList, Context context,FirebaseAuth firebaseAuth) {
        this.infoItemBeanList = infoItemBeanList;
        this.context = context;
        this.firebaseAuth=firebaseAuth;
    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(InfoAdapter.ViewHolder holder, final int position) {
        InfoItemBean infoItemBean= infoItemBeanList.get(position);
        holder.imageView.setImageResource(infoItemBean.getImageViewId());
        holder.textView.setText(infoItemBean.getTextViewTitle());
        holder.textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        context.startActivity(new Intent(context, InviteActivity.class));
                        break;
                    case 1:
                        sendResetPasswordEmail(firebaseAuth.getCurrentUser().getEmail());
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        Toast.makeText(context, "Signing out", Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();
                            firebaseAuth.signOut();
                            ((Activity)context).finish();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            break;
                }
            }
        });
    }

    public void sendResetPasswordEmail(String email)
    {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                            Toast.makeText(context,"Email Sent.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return infoItemBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageViewInfoItem);
            textView= itemView.findViewById(R.id.textViewInfoItem);

        }
    }
}
