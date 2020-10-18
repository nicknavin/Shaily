package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.ChatedBody;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllChatUserAdapter extends RecyclerView.Adapter<AllChatUserAdapter.ChatUserHolder> {

    ArrayList<ChatedBody> chatedPersonsList =new ArrayList<>();
    Context context;
    String userId;
    ApiItemCallback apiItemCallback;
    public AllChatUserAdapter(Context context, ArrayList<ChatedBody> list,String id,ApiItemCallback apiItemCallback)
    {
        userId=id;
        this.context=context;
        chatedPersonsList=list;
        this.apiItemCallback=apiItemCallback;
    }



    @NonNull
    @Override
    public AllChatUserAdapter.ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.all_chat_user_layout, parent, false);
        return new ChatUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllChatUserAdapter.ChatUserHolder holder, int position)
    {
      ChatedBody chatedBody = chatedPersonsList.get(position);
      if(userId.equals(chatedBody.getChatedPersonsBody().getSenderId()))
      {
          holder.txtUserName.setText(chatedBody.getChatedPersonsBody().getReciverName());
          Glide.with(context)
                  .load(chatedBody.getChatedPersonsBody().getReciverProfileUrl())
                  .placeholder(R.mipmap.profile_image)
                  .error(R.mipmap.profile_image)
                  .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .into(holder.imgProfile);
      }
      else
      {
          holder.txtUserName.setText(chatedBody.getChatedPersonsBody().getSenderName());
          Glide.with(context)
                  .load(chatedBody.getChatedPersonsBody().getSenderProfileUrl())
                  .placeholder(R.mipmap.profile_image)
                  .error(R.mipmap.profile_image)
                  .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .into(holder.imgProfile);
      }

      holder.txtChatMsg.setText(chatedBody.getChatedPersonsBody().getMessage());
holder.layChatUser.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        apiItemCallback.result(position);
    }
});


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

    class ChatUserHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtUserName,txtChatMsg,txtTimeStamp;
        CircleImageView imgProfile;
        RelativeLayout layChatUser;
        public ChatUserHolder(View itemView)
        {
            super(itemView);
            txtTimeStamp = (CustomTextView) itemView.findViewById(R.id.txtTimeStamp);
            txtChatMsg = (CustomTextView) itemView.findViewById(R.id.txtChatMsg);
            txtUserName = (CustomTextView) itemView.findViewById(R.id.txtUserName);
            imgProfile=(CircleImageView)itemView.findViewById(R.id.imgProfile);
            layChatUser=(RelativeLayout)itemView.findViewById(R.id.layChatUser);
        }
    }


}
