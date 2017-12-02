package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.GroupBean;

/**
 * Created by Vaibhav's Console on 12/1/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<GroupBean> beanList;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private GroupAdapter.OnItemClickListener mItemClickListener;

    public GroupAdapter(List<GroupBean> beanList, Context context, FirebaseAuth firebaseAuth, GroupAdapter.OnItemClickListener mItemClickListener) {
        this.beanList = beanList;
        this.context = context;
        this.firebaseAuth = firebaseAuth;
        this.mItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView title;
        private TextView groupName, groupCount;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            groupName = itemView.findViewById(R.id.groupName);
            groupCount = itemView.findViewById(R.id.groupCount);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView, getPosition());
        }
    }

    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        GroupBean groupBean = beanList.get(position);
        if(groupBean!=null) {
            holder.groupName.setText(groupBean.getGroupName());
            holder.groupCount.setText("People Count: " + groupBean.getPeopleCount());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

}
