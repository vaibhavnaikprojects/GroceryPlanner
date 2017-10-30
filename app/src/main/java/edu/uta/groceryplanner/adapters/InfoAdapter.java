package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.InviteActivity;
import edu.uta.groceryplanner.LoginActivity;
import edu.uta.groceryplanner.R;
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
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        Toast.makeText(context, "signing out", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        break;
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
