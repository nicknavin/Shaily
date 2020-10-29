package com.app.session.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.rey.material.widget.ProgressView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;

    Context context;
    String senderID = "";
    LinkedList<ChatMessage> chatMessageArrayList;

    public ChatAdapter(Context ctxt, LinkedList<ChatMessage> list, String senderID) {
        this.context = ctxt;
        this.senderID = senderID;
        chatMessageArrayList = list;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemMessageFriendHolder) {

            ((ItemMessageFriendHolder) holder).txtContent.setText(chatMessageArrayList.get(position).getMessage());
            if (chatMessageArrayList.get(position).getCreatedAt() != null && !chatMessageArrayList.get(position).getCreatedAt().isEmpty()) {
                if (chatMessageArrayList.get(position).getCreatedAt().contains("T")) {
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
            if (chatMessageArrayList.get(position).getMessageType() != null && chatMessageArrayList.get(position).getMessageType().equals("image")) {
                ((ItemMessageFriendHolder) holder).imgChat.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder) holder).txtContent.setVisibility(View.GONE);

                Glide.with(context)
                        .load(chatMessageArrayList.get(position).getFile())
                        .transform(new RoundedCorners(16))
                        .placeholder(R.mipmap.profile_icon)
                        .error(R.mipmap.profile_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemMessageFriendHolder) holder).imgChat);
            } else {
                ((ItemMessageFriendHolder) holder).imgChat.setVisibility(View.GONE);
                ((ItemMessageFriendHolder) holder).txtContent.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof ItemMessageUserHolder) {

            ((ItemMessageUserHolder) holder).txtContent.setText(chatMessageArrayList.get(position).getMessage());
            if (chatMessageArrayList.get(position).getCreatedAt() != null && !chatMessageArrayList.get(position).getCreatedAt().isEmpty()) {
                if (chatMessageArrayList.get(position).getCreatedAt().contains("T")) {
                    ((ItemMessageUserHolder) holder).textMSgTime.setText(Utility.getTimeSlot(chatMessageArrayList.get(position).getCreatedAt()));
                }
            }
            ((ItemMessageUserHolder) holder).rey_loading.setVisibility(View.GONE);
            if (chatMessageArrayList.get(position).getMessageType() != null && chatMessageArrayList.get(position).getMessageType().equals("image"))
            {
                ((ItemMessageUserHolder) holder).txtContent.setVisibility(View.GONE);
                ((ItemMessageUserHolder) holder).layImage.setVisibility(View.VISIBLE);
                if (chatMessageArrayList.get(position).getPath() != null && !chatMessageArrayList.get(position).getPath().isEmpty())
                {
                        Uri resultUri = Uri.parse(chatMessageArrayList.get(position).getPath());
                        Glide.with(context)
                                .load(new File(resultUri.getPath()))
                                .transform(new RoundedCorners(16))
                                .placeholder(R.mipmap.place_holder)
                                .error(R.mipmap.place_holder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(((ItemMessageUserHolder) holder).imgChat);

                        if(!chatMessageArrayList.get(position).isUpload())
                        {
                            ((ItemMessageUserHolder) holder).rey_loading.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ((ItemMessageUserHolder) holder).rey_loading.setVisibility(View.GONE);
                        }
                }
                else {
                    Glide.with(context)
                            .load(chatMessageArrayList.get(position).getFile())
                            .transform(new RoundedCorners(16))
                            .placeholder(R.mipmap.place_holder)
                            .error(R.mipmap.place_holder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((ItemMessageUserHolder) holder).imgChat);
                }
            }
            else {
                ((ItemMessageUserHolder) holder).layImage.setVisibility(View.GONE);
                ((ItemMessageUserHolder) holder).txtContent.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return chatMessageArrayList.get(position).getSenderId().equals(senderID) ? VIEW_TYPE_USER_MESSAGE : VIEW_TYPE_FRIEND_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return chatMessageArrayList.size();
    }

    class ItemMessageUserHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent, textMSgTime;
        public ImageView imgChat;
        RelativeLayout layImage;
        ProgressView rey_loading;

        public ItemMessageUserHolder(View itemView) {
            super(itemView);
            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentUser);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
            layImage = (RelativeLayout) itemView.findViewById(R.id.layImage);
            rey_loading=(ProgressView)itemView.findViewById(R.id.rey_loading);
        }
    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtContent, textMSgTime;
        CircleImageView imgFriend;
        public ImageView imgChat;


        public ItemMessageFriendHolder(View itemView) {
            super(itemView);

            txtContent = (CustomTextView) itemView.findViewById(R.id.textContentFriend);
            textMSgTime = (CustomTextView) itemView.findViewById(R.id.textMSgTime);
            imgFriend = (CircleImageView) itemView.findViewById(R.id.imgFriend);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
        }
    }

}
