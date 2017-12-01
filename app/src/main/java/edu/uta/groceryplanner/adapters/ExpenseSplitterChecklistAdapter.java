package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.ListBean;
import edu.uta.groceryplanner.beans.ProductBean;
import edu.uta.groceryplanner.beans.StatisticsBean;

/**
 * Created by Prakhar on 11/28/2017.
 */

public class ExpenseSplitterChecklistAdapter extends RecyclerView.Adapter<ExpenseSplitterChecklistAdapter.ViewHolder>{
    private List<ProductBean> productBeanList;
    private Context context;
    private FirebaseAuth firebaseAuth;


    public ExpenseSplitterChecklistAdapter(List<ProductBean> productBeanList, Context context, FirebaseAuth firebaseAuth) {
        this.productBeanList = productBeanList;
        this.context = context;
        this.firebaseAuth=firebaseAuth;
    }

    @Override
    public ExpenseSplitterChecklistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpenseSplitterChecklistAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_expense_checklist,parent,false));
    }

    @Override
    public void onBindViewHolder(ExpenseSplitterChecklistAdapter.ViewHolder holder, final int position) {
        ProductBean productBean = productBeanList.get(position);
        holder.textViewProductName.setText(productBean.getProductName());
        holder.textViewCount.setText(productBean.getQuantity());
        holder.textViewCost.setText(Double.toString(productBean.getCost())+"$");
    }
    @Override
    public int getItemCount() {
        return productBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewProductName;
        public TextView textViewCost;
        public TextView textViewCount;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewCount = itemView.findViewById(R.id.textViewCount);
            textViewProductName= itemView.findViewById(R.id.textViewProductName);
            textViewCost= itemView.findViewById(R.id.textViewCost);
        }
    }




}
