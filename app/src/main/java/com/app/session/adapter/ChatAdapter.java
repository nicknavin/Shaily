package com.app.session.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.session.R;
import com.app.session.activity.ChatActivity;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.model.ChatMessage;
import com.app.session.model.Conversation;

import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    Context context;

    String senderID="";
    LinkedList<ChatMessage> chatMessageArrayList;
    public ChatAdapter(Context ctxt,LinkedList<ChatMessage> list ,String senderID)
    {
        this.context=ctxt;

        this.senderID=senderID;
        chatMessageArrayList=list;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_FRIEND_MESSAGE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ItemMessageFriendHolder)
        {
            ((ItemMessageFriendHolder) holder).txtContent.setText(chatMessageArrayList.get(position).getMessage());

        }
         else if (holder instanceof ItemMessageUserHolder)
        {
            ((ItemMessageUserHolder) holder).txtContent.setText(chatMessageArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return chatMessageArrayList.get(position).getSenderId().equals(senderID)?VIEW_TYPE_USER_MESSAGE:VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }




    class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent;


        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentUser);

        }
    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent;


        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentFriend);

        }
    }

}
