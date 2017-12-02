package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.ProductBean;

/**
 * Created by Nikhil on 11/30/2017.
 */

public class ReadyChecklistAdapter extends RecyclerView.Adapter<ReadyChecklistAdapter.ViewHolder> {

    private List<ProductBean> productBeans;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private ProductAdapter.OnItemClickListener mItemClickListener;

    public ReadyChecklistAdapter(List<ProductBean> productBeans, Context context){
        this.productBeans=productBeans;
        this.context=context;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout draftProductItem;
        private ImageView imageViewInfoItem;
        private TextView productName, productQuantity, productCost;
        private CheckBox productCheck;
        public ViewHolder(View itemView) {
            super(itemView);
            draftProductItem= itemView.findViewById(R.id.draftProductItem);
            imageViewInfoItem= itemView.findViewById(R.id.imageViewInfoItem);
            productName= itemView.findViewById(R.id.textViewInfoItem);
            productQuantity= itemView.findViewById(R.id.quantity);
            productCost = itemView.findViewById(R.id.cost);
            productCheck = itemView.findViewById(R.id.readOnlyCheckBox);
            draftProductItem.setOnClickListener(this);
            productName.setEnabled(true);
            productQuantity.setEnabled(true);
            productCost.setEnabled(true);
            productCheck.setEnabled(true);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }

    @Override
    public ReadyChecklistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReadyChecklistAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ready_checklist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ReadyChecklistAdapter.ViewHolder holder, int position) {
        ProductBean productBean=productBeans.get(position);
        holder.productName.setText(productBean.getProductName());
        holder.productQuantity.setText(productBean.getQuantity()+"");
        holder.productCost.setText(productBean.getCost()+"");
        if("uncheck".equalsIgnoreCase(productBean.getStatus())){
            holder.productCheck.setChecked(false);
        }else{
            holder.productCheck.setChecked(true);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return productBeans.size();
    }
}
