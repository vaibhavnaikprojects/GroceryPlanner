package edu.uta.groceryplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.uta.groceryplanner.R;
import edu.uta.groceryplanner.beans.FriendsBean;

/**
 * Created by Vaibhav's Console on 11/24/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private List<FriendsBean> friends;
    private Context context;
    private OnItemClickListener mItemClickListener;

    public FriendsAdapter(List<FriendsBean> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout friendCard;
        private TextView textViewUserName,oweMessage,owePrice;
        public ViewHolder(View itemView) {
            super(itemView);
            friendCard= itemView.findViewById(R.id.friendCard);
            textViewUserName= itemView.findViewById(R.id.textViewUserName);
            oweMessage= itemView.findViewById(R.id.oweMessage);
            owePrice= itemView.findViewById(R.id.owePrice);
            friendCard.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getPosition());
        }
    }
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FriendsBean friendsBean=friends.get(position);
        holder.textViewUserName.setText(friendsBean.getFriendName());
        holder.oweMessage.setText(friendsBean.getOweStatus());
        if(!"resolved".equalsIgnoreCase(friendsBean.getOweStatus()))
            holder.owePrice.setText(friendsBean.getOwePrice());
        else
            holder.owePrice.setText("");
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(FriendsAdapter.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return friends.size();
    }

}
