package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.Category;
import com.app.session.data.model.CategoryBody;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class ProfileCategoryNewAdapter extends RecyclerView.Adapter<ProfileCategoryNewAdapter.ViewHolder> {


    Context context;
    ArrayList<CategoryBody> categoryArrayList;
    ApiItemCallback apiItemCallback;
    boolean flag;



    public ProfileCategoryNewAdapter(Context context, ArrayList<CategoryBody> list, boolean flag, ApiItemCallback callback)
    {
        this.context = context;
        categoryArrayList = list;
        this.apiItemCallback=callback;
        this.flag=flag;
    }


    @Override
    public ProfileCategoryNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile_category_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ProfileCategoryNewAdapter.ViewHolder holder, final int position)
    {
        CategoryBody category = categoryArrayList.get(position);
        holder.txtCategory.setText(category.getCategoryName());
        if(flag)
        {
            holder.imgCross.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgCross.setVisibility(View.GONE);
        }
        holder.imgCross.setTag(category);
        holder.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                apiItemCallback.result(position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtCategory;
        ImageView imgCross;



        public ViewHolder(View view) {
            super(view);
            txtCategory = (CustomTextView) view.findViewById(R.id.txtCategory);
            imgCross=(ImageView)view.findViewById(R.id.imgCross);


        }

    }


}
