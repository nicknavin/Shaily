package com.app.session.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
            if(chatMessageArrayList.get(position).getCreatedAt()!=null&&!chatMessageArrayList.get(position).getCreatedAt().isEmpty())
            {
                if(chatMessageArrayList.get(position).getCreatedAt().contains("T")) {
                    ((ItemMessageFriendHolder) holder).textMSgTime.setText(Utility.getTimeSlot(chatMessageArrayList.get(position).getCreatedAt()));
                }


            }
            Glide.with(context)
                    .load(chatMessageArrayList.get(position).getReciverProfileUrl())
                    .placeholder(R.mipmap.profile_icon)
                    .error(R.mipmap.profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemMessageFriendHolder) holder).imgFriend);
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "HelveticaNeueMed.ttf");
            ((ItemMessageFriendHolder) holder).txtContent.setTypeface(face);
            ((ItemMessageFriendHolder) holder).textMSgTime.setTypeface(face);
        }
         else if (holder instanceof ItemMessageUserHolder)
        {
            ((ItemMessageUserHolder) holder).txtContent.setText(chatMessageArrayList.get(position).getMessage());
            if(chatMessageArrayList.get(position).getCreatedAt()!=null&&!chatMessageArrayList.get(position).getCreatedAt().isEmpty()) {
                if(chatMessageArrayList.get(position).getCreatedAt().contains("T")) {
                    ((ItemMessageUserHolder) holder).textMSgTime.setText(Utility.getTimeSlot(chatMessageArrayList.get(position).getCreatedAt()));
                }
            }
            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "HelveticaNeueMed.ttf");
            ((ItemMessageUserHolder) holder).txtContent.setTypeface(face);
            ((ItemMessageUserHolder) holder).textMSgTime.setTypeface(face);
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
        public CustomTextView txtContent,textMSgTime;


        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentUser);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);

        }
    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent,textMSgTime;
        CircleImageView imgFriend;
        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentFriend);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);
            imgFriend=(CircleImageView)itemView.findViewById(R.id.imgFriend);

        }
    }

}
