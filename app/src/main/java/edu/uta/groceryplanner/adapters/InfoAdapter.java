package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.InfoItemBean;

/**
 * Created by Vaibhav's Console on 10/11/2017.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<InfoItemBean> infoItemBeanList;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageViewInfoItem);
            textView= itemView.findViewById(R.id.textViewInfoItem);
            textView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }

    public InfoAdapter(List<InfoItemBean> infoItemBeanList, Context context,FirebaseAuth firebaseAuth,OnItemClickListener onItemClickListener) {
        this.infoItemBeanList = infoItemBeanList;
        this.context = context;
        this.firebaseAuth=firebaseAuth;
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        InfoItemBean infoItemBean= infoItemBeanList.get(position);
        holder.imageView.setImageResource(infoItemBean.getImageViewId());
        holder.textView.setText(infoItemBean.getTextViewTitle());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return infoItemBeanList.size();
    }

}
