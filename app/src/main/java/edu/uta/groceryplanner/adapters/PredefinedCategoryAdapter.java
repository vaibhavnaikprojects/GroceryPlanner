package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.PredefinedCategory;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class PredefinedCategoryAdapter extends RecyclerView.Adapter<PredefinedCategoryAdapter.ViewHolder> {
    private List<PredefinedCategory> predefinedCategories;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private PredefinedCategoryAdapter.OnItemClickListener mItemClickListener;
    public PredefinedCategoryAdapter(List<PredefinedCategory> predefinedCategories,Context context,PredefinedCategoryAdapter.OnItemClickListener mItemClickListener){
        this.predefinedCategories=predefinedCategories;
        this.context=context;
        this.mItemClickListener=mItemClickListener;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView productName;
        public ViewHolder(View itemView) {
            super(itemView);
            productName= itemView.findViewById(R.id.textViewItem);
            productName.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }
    @Override
    public PredefinedCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PredefinedCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.predefined_item,parent,false));
    }

    @Override
    public void onBindViewHolder(PredefinedCategoryAdapter.ViewHolder holder, int position) {
        PredefinedCategory predefinedCategory=predefinedCategories.get(position);
        holder.productName.setText(predefinedCategory.getCategoryName());
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    @Override
    public int getItemCount() {
        return predefinedCategories.size();
    }
}
