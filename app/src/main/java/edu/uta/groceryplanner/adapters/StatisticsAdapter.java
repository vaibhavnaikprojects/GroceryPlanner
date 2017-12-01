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
import edu.uta.groceryplanner.beans.StatisticsBean;

/**
 * Created by Prakhar on 11/17/2017.
 */

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder>{

    private List<StatisticsBean> statisticsBeanList;
    private Context context;
    private FirebaseAuth firebaseAuth;
    public StatisticsAdapter(List<StatisticsBean> statisticsBeanList, Context context, FirebaseAuth firebaseAuth) {
        this.statisticsBeanList = statisticsBeanList;
        this.context = context;
        this.firebaseAuth=firebaseAuth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_details,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        StatisticsBean statisticsBean = statisticsBeanList.get(position);
        holder.textViewProductName.setText(statisticsBean.getProductName());
        holder.textViewCost.setText(statisticsBean.getCost()+"$");
        holder.textViewPercentage.setText(statisticsBean.getPercentage()+"%");
    }
    @Override
    public int getItemCount() {
        return statisticsBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewProductName;
        public TextView textViewCost;
        public TextView textViewPercentage;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewProductName= itemView.findViewById(R.id.textViewProductName);
            textViewCost= itemView.findViewById(R.id.textViewCost);
            textViewPercentage= itemView.findViewById(R.id.textViewPercentage);
        }
    }



}
