package com.example.ruchirshetye.awschat.recyclerview;

/**
 * Created by Ruchir Shetye on 11-Apr-18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruchirshetye.awschat.R;
import com.example.ruchirshetye.awschat.controllers.ChatManager;
import com.example.ruchirshetye.awschat.interfaces.RecyclerViewHolderListener;
import com.example.ruchirshetye.awschat.models.User;

public class PotentialFriendListAdapter  extends RecyclerView.Adapter<PotentialFriendListAdapter.ViewHolder>  {

    private Context mContext;
    private RecyclerViewHolderListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView emailAddress;
        public int itemIndex;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.friend_username);
            emailAddress = (TextView) view.findViewById(R.id.friend_emailaddress);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.didTapOnRowAtIndex(itemIndex);
                }
            });
        }
    }

    public PotentialFriendListAdapter(Context context, RecyclerViewHolderListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ChatManager chatManager = ChatManager.getInstance(mContext);

        User user = chatManager.potentialFriendList.get(position);

        holder.username.setText(user.getUsername());
        holder.emailAddress.setText(user.getEmail_address());
        holder.itemIndex = position;
    }

    @Override
    public int getItemCount() {
        ChatManager chatManager = ChatManager.getInstance(mContext);
        return chatManager.potentialFriendList.size();
    }
}
