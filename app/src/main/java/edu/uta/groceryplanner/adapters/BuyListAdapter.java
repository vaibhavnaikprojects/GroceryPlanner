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
import edu.uta.groceryplanner.beans.ProductBean;

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
        private TextView cardTitleName,listType,noOfItems,createdDate,updatedDate;
        public ViewHolder(View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.card_title);
            cardTitleImage= itemView.findViewById(R.id.card_title_image);
            cardTitleName= itemView.findViewById(R.id.card_title_text);
            listType= itemView.findViewById(R.id.list_type);
            noOfItems=itemView.findViewById(R.id.noOfItems);
            createdDate=itemView.findViewById(R.id.created_date);
            updatedDate=itemView.findViewById(R.id.update_date);
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
        List<ProductBean> productBeans= listBean.getProductBeans();
        if(productBeans!=null)
            holder.noOfItems.setText("Total Count: "+productBeans.size());
        else
            holder.noOfItems.setText("Total Count: 0");
        holder.createdDate.setText("Created Date: "+listBean.getCreatedDate());
        holder.updatedDate.setText("Last Updated Date: "+listBean.getUpdatedDate());
        if("Personal".equalsIgnoreCase(listBean.getListType())) {
            holder.listType.setText(listBean.getListType()+" list");
            holder.cardTitleImage.setImageResource(R.drawable.ic_person_white_24dp);
        }else {
            holder.listType.setText("Group Name: "+listBean.getListGroupName());
            holder.cardTitleImage.setImageResource(R.drawable.ic_group_white_24dp);
        }
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
