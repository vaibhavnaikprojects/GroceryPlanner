package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.ListBean;

/**
 * Created by Vaibhav's Console on 10/29/2017.
 */

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> {
    private List<ListBean> beanList;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private OnItemClickListener mItemClickListener;

    public BuyListAdapter(List<ListBean> beanList, Context context,FirebaseAuth firebaseAuth) {
        this.beanList = beanList;
        this.context = context;
        this.firebaseAuth=firebaseAuth;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout title;
        private ImageView cardTitleImage;
        private TextView cardTitleName;
        private TextView productNames;
        public ViewHolder(View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.card_title);
            cardTitleImage= itemView.findViewById(R.id.card_title_image);
            cardTitleName= itemView.findViewById(R.id.card_title_text);
            productNames= itemView.findViewById(R.id.product_name);
            title.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }

    @Override
    public BuyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuyListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_list_item,parent,false));
    }
    @Override
    public void onBindViewHolder(BuyListAdapter.ViewHolder holder, int position) {
        ListBean listBean=beanList.get(position);
        holder.cardTitleName.setText(listBean.getListName());
        holder.productNames.setText(listBean.getIntro());
        if("Personal".equalsIgnoreCase(listBean.getListType()))
            holder.cardTitleImage.setImageResource(R.drawable.common_full_open_on_phone);
        else
            holder.cardTitleImage.setImageResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return beanList.size();
    }
}
