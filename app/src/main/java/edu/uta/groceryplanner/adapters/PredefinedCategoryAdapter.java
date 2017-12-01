package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uta.groceryplanner.R;

/**
 * Created by Vaibhav's Console on 11/27/2017.
 */

public class PredefinedCategoryAdapter extends RecyclerView.Adapter<PredefinedCategoryAdapter.ViewHolder> {
    private Context context;
    private List<String> predefinedCategories;
    private PredefinedCategoryAdapter.OnItemClickListener mItemClickListener;

    public PredefinedCategoryAdapter(Context context, List<String> predefinedCategories,PredefinedCategoryAdapter.OnItemClickListener mItemClickListener) {
        this.context = context;
        this.predefinedCategories = predefinedCategories;
        this.mItemClickListener=mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.productCategory);
            textView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }

    @Override
    public PredefinedCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PredefinedCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.predefined_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String predefinedCategory=predefinedCategories.get(position);
        holder.textView.setText(predefinedCategory);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    @Override
    public int getItemCount() {
        return predefinedCategories.size();
    }
}
