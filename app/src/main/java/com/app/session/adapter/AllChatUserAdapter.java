package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.Category;
import com.app.session.data.model.ChatedBody;
import com.app.session.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllChatUserAdapter extends RecyclerView.Adapter<AllChatUserAdapter.ChatUserHolder> implements Filterable {

    ArrayList<ChatedBody> chatedPersonsList;
    ArrayList<ChatedBody> mArrayList;
    Context context;
    String userId;
    ApiItemCallback apiItemCallback;
    String typingUser = "";

    public AllChatUserAdapter(Context context, ArrayList<ChatedBody> list, String id, ApiItemCallback apiItemCallback) {
        userId = id;
        this.context = context;
        chatedPersonsList = list;
        mArrayList = list;
        this.apiItemCallback = apiItemCallback;
    }


    @NonNull
    @Override
    public AllChatUserAdapter.ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_chat_user_layout, parent, false);
        return new ChatUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChatUserAdapter.ChatUserHolder holder, int position) {
        ChatedBody chatedBody = chatedPersonsList.get(position);
        System.out.println("chat url " + chatedBody.getChatedPersonsBody().getReciverProfileUrl());
        if (userId.equals(chatedBody.getChatedPersonsBody().getSenderId()))
        {
            holder.txtUserName.setText(chatedBody.getChatedPersonsBody().getReciverName());
            Glide.with(context)
                    .load(chatedBody.getChatedPersonsBody().getReciverProfileUrl())
                    .placeholder(R.mipmap.profile_icon)
                    .error(R.mipmap.profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            if (!typingUser.isEmpty() && typingUser.equals(chatedBody.getChatedPersonsBody().getReciverId())) {
                holder.txtChatMsg.setVisibility(View.GONE);
                holder.txtTyping.setVisibility(View.VISIBLE);
                holder.txtTyping.setText("typing...");
            } else {
                holder.txtChatMsg.setVisibility(View.VISIBLE);
                holder.txtTyping.setVisibility(View.GONE);
            }


        } else {
            holder.txtUserName.setText(chatedBody.getChatedPersonsBody().getSenderName());
            Glide.with(context)
                    .load(chatedBody.getChatedPersonsBody().getSenderProfileUrl())
                    .placeholder(R.mipmap.profile_icon)
                    .error(R.mipmap.profile_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);

            if (!typingUser.isEmpty() && typingUser.equals(chatedBody.getChatedPersonsBody().getSenderId())) {
                holder.txtChatMsg.setVisibility(View.GONE);
                holder.txtTyping.setVisibility(View.VISIBLE);
                holder.txtTyping.setText("typing...");
            } else {
                holder.txtChatMsg.setVisibility(View.VISIBLE);
                holder.txtTyping.setVisibility(View.GONE);
            }

        }
        String time = chatedBody.getChatedPersonsBody().getCreatedAt();

        holder.txtTimeStamp.setText(
                Utility.getTimeSlot(time)
        );
        if (chatedBody.getNotification() != null && !chatedBody.getNotification().isEmpty()&&!chatedBody.getNotification().equals("0"))
        {
            holder.layNotification.setVisibility(View.VISIBLE);
            holder.txtMsgCount.setText(chatedBody.getNotification());
        } else {
            holder.layNotification.setVisibility(View.GONE);
        }

        holder.layChatUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiItemCallback.result(position);
            }
        });


        if(chatedBody.getChatedPersonsBody().getMessageType().equals("image"))
        {
            holder.layFile.setVisibility(View.VISIBLE);
            holder.txtChatMsg.setVisibility(View.GONE);
            holder.txtChatMsgType.setText("photo");
        }
        if(chatedBody.getChatedPersonsBody().getMessageType().equals("docs"))
        {
            holder.layFile.setVisibility(View.VISIBLE);
            holder.txtChatMsg.setVisibility(View.GONE);
            holder.imgType.setImageResource(R.mipmap.docs_story);
            holder.txtChatMsgType.setText(chatedBody.getChatedPersonsBody().getDisplayFileName());
        }
        if(chatedBody.getChatedPersonsBody().getMessageType().equals("audio"))
        {

            holder.layFile.setVisibility(View.VISIBLE);
            holder.txtChatMsg.setVisibility(View.GONE);
            holder.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.mic));
            holder.txtChatMsgType.setText(chatedBody.getChatedPersonsBody().getDisplayFileName());
        }
        else if(chatedBody.getChatedPersonsBody().getMessageType().equals("text"))
        {
            holder.layFile.setVisibility(View.GONE);
            holder.txtChatMsg.setVisibility(View.VISIBLE);
            holder.txtChatMsg.setText(chatedBody.getChatedPersonsBody().getMessage());
        }



    }

    public void onTyping(String name) {
        typingUser = name;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return chatedPersonsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    chatedPersonsList = mArrayList;
                } else {

                    ArrayList<ChatedBody> filteredList = new ArrayList<>();

                    for (ChatedBody chatedBody : mArrayList) {

                        if (chatedBody.getChatedPersonsBody().getReciverId().toLowerCase().contains(charString)) {

                            filteredList.add(chatedBody);
                        }
                    }

                    chatedPersonsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chatedPersonsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chatedPersonsList = (ArrayList<ChatedBody>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ChatUserHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtUserName, txtChatMsg, txtChatMsgType, txtTyping, txtTimeStamp, txtMsgCount;
        CircleImageView imgProfile;
        ImageView imgType;
        RelativeLayout layChatUser, layNotification;
        LinearLayout layFile;

        public ChatUserHolder(View itemView) {
            super(itemView);
            layFile = (LinearLayout) itemView.findViewById(R.id.layFile);
            txtTyping = (CustomTextView) itemView.findViewById(R.id.txtTyping);
            txtChatMsgType = (CustomTextView) itemView.findViewById(R.id.txtChatMsgType);
            txtMsgCount = (CustomTextView) itemView.findViewById(R.id.txtMsgCount);
            txtTimeStamp = (CustomTextView) itemView.findViewById(R.id.txtTimeStamp);
            txtChatMsg = (CustomTextView) itemView.findViewById(R.id.txtChatMsg);
            txtUserName = (CustomTextView) itemView.findViewById(R.id.txtUserName);
            imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            layChatUser = (RelativeLayout) itemView.findViewById(R.id.layChatUser);
            layNotification = (RelativeLayout) itemView.findViewById(R.id.layNotification);
            imgType=(ImageView)itemView.findViewById(R.id.imgType);
        }
    }


}
