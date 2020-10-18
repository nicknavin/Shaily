package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.model.SubscriptionGroup;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SubscriptionGroupAdapters extends RecyclerView.Adapter<SubscriptionGroupAdapters.ViewHolder> {


    Context context;
    ArrayList<SubscriptionGroup> subCategoryArrayList;
    ApiItemCallback apiCallback;

    public SubscriptionGroupAdapters(Context context, ArrayList<SubscriptionGroup> list, ApiItemCallback apiCallback) {
        this.context = context;
        subCategoryArrayList = list;
        this.apiCallback = apiCallback;

    }


    @Override
    public SubscriptionGroupAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SubscriptionGroupAdapters.ViewHolder holder, final int position) {

        SubscriptionGroup group = subCategoryArrayList.get(position);
        System.out.println("imageurl "+position+": "+Urls.BASE_IMAGES_URL +group.getGroup_url_image_address());
        if (group.getGroup_url_image_address() != null && !group.getGroup_url_image_address().isEmpty()) {

            String url =Urls.BASE_IMAGES_URL +group.getGroup_url_image_address();
           // Picasso.with(context).load(url).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.imgGroup);

            Glide.with(context) //1
                    .load(url)
                    .placeholder(R.mipmap.profile_image)

                    .into(holder.imgGroup);

//            Picasso.with(context)
//                    .load(url)
//                    .memoryPolicy(MemoryPolicy.NO_STORE)
//                    .into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            holder.imgGroup.setImageBitmap(bitmap);
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Drawable errorDrawable) {
//                            holder.imgGroup.setImageResource(R.mipmap.profile_image);
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//                            holder.imgGroup.setImageResource(R.mipmap.profile_image);
//                        }
//                    });
            //Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).error(R.mipmap.profile_image).placeholder(R.mipmap.profile_large).into(imageView);

//
//            Glide.with(context)
//                    .load(url)
//                    .diskCacheStrategy( DiskCacheStrategy.ALL )
//                    .placeholder(R.mipmap.profile_large)
//                    .into(holder.imgGroup);




//            Picasso.with(context).load(Urls.BASE_IMAGES_URL +group.getGroup_url_image_address()).placeholder(R.mipmap.profile_large).into(holder.imgGroup);
        }
        holder.txtGroupName.setText(group.getGroup_name());
        holder.layGroup.setTag(group);
        holder.layGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallback.result( position);
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {

        return subCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtGroupName;
        ImageView imgGroup;
        LinearLayout layGroup;


        public ViewHolder(View view) {
            super(view);
            txtGroupName = (CustomTextView) view.findViewById(R.id.txtGroupName);
            imgGroup = (ImageView) view.findViewById(R.id.imgGroup);
            layGroup = (LinearLayout) view.findViewById(R.id.layGroup);


        }

    }


}
