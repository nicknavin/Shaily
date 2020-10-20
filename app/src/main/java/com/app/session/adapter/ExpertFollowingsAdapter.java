package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.BodyFollowers;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpertFollowingsAdapter extends RecyclerView.Adapter<ExpertFollowingsAdapter.FollowingHolder>
{
    ArrayList<BodyFollowers> followersArrayList;
    ApiItemCallback callback;
    Context context;
  public ExpertFollowingsAdapter (Context context, ArrayList<BodyFollowers> list, ApiItemCallback apiItemCallback)
   {
       this.context=context;
       followersArrayList=list;
       callback=apiItemCallback;
   }


    @NonNull
    @Override
    public FollowingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expertfollowingslayout, parent, false);
        return new FollowingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingHolder holder, int position)
    {
        BodyFollowers followers=followersArrayList.get(position);
        holder.txtUserName.setText(followers.getFollowerUsers().getUser_name());
        Glide.with(context)
                .load(Urls.BASE_IMAGES_URL +followers.getFollowerUsers().getImageUrl())
                .placeholder(R.mipmap.profile_image)
                .error(R.mipmap.profile_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgProfile);
        holder.layChatUser.setTag(followers);
        holder.layChatUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                callback.result(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return followersArrayList.size();
    }

    class FollowingHolder extends RecyclerView.ViewHolder {
        public CustomTextView txtUserName;
        CircleImageView imgProfile;
        RelativeLayout layChatUser;
        public FollowingHolder(View itemView)
        {
            super(itemView);

            txtUserName = (CustomTextView) itemView.findViewById(R.id.txtUserName);
            imgProfile=(CircleImageView)itemView.findViewById(R.id.imgProfile);
            layChatUser=(RelativeLayout)itemView.findViewById(R.id.layChatUser);
        }
    }
}
