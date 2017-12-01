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
import edu.uta.groceryplanner.beans.ProductBean;

/**
 * Created by Vaibhav's Console on 10/30/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<ProductBean> productBeans;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private ProductAdapter.OnItemClickListener mItemClickListener;

    public ProductAdapter(List<ProductBean> productBeans, Context context,ProductAdapter.OnItemClickListener mItemClickListener){
        this.productBeans=productBeans;
        this.context=context;
        this.mItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout draftProductItem;
        private ImageView imageViewInfoItem;
        private TextView productName, productQuantity;
        public ViewHolder(View itemView) {
            super(itemView);
            draftProductItem= itemView.findViewById(R.id.draftProductItem);
            imageViewInfoItem= itemView.findViewById(R.id.imageViewInfoItem);
            productName= itemView.findViewById(R.id.textViewInfoItem);
            productQuantity= itemView.findViewById(R.id.quantity);
            draftProductItem.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_product_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        ProductBean productBean=productBeans.get(position);
        holder.productName.setText(productBean.getProductName());
        holder.productQuantity.setText(productBean.getQuantity());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return productBeans.size();
    }
}
