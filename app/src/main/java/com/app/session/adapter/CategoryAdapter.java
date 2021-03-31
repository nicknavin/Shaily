package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Category;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    Context context;
    private ArrayList<Category> categoryList = new ArrayList<>();
private ApiCallback apiCallback;
    public CategoryAdapter(Context context, ArrayList<Category> list, ApiCallback apiCallback) {
        this.context = context;
        categoryList = list;
        this.apiCallback=apiCallback;
    }


    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {
        Category category = categoryList.get(position);
        holder.txtCategory.setText(category.getCategoryName());
        holder.txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallback.result(""+position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtCategory;

      CardView card_view;


        public ViewHolder(View view) {
            super(view);
            txtCategory = (CustomTextView) view.findViewById(R.id.txtLang);
            card_view = (CardView) view.findViewById(R.id.card_view);




        }

    }


}
