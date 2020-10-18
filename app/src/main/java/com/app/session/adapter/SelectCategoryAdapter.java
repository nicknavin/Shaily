package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.model.Category;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder> {


    Context context;
    ArrayList<Category> categoryArrayList;


    public SelectCategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        categoryArrayList = list;
    }


    @Override
    public SelectCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selected_category_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SelectCategoryAdapter.ViewHolder holder, final int position)
    {
        Category category = categoryArrayList.get(position);
        holder.txtCategory.setText(category.getCategoryName());


    }


    @Override
    public int getItemCount() {

        return categoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtCategory;




        public ViewHolder(View view) {
            super(view);
            txtCategory = (CustomTextView) view.findViewById(R.id.txtCategory);



        }

    }


}
