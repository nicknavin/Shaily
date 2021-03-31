package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.SubCategoryModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class CategoryEditAdapter extends RecyclerView.Adapter<CategoryEditAdapter.ViewHolder> {


    Context context;
    ArrayList<SubCategoryModel> languageArrayList;

    public CategoryEditAdapter(Context context, ArrayList<SubCategoryModel> list) {
        this.context = context;
        languageArrayList = list;
    }


    @Override
    public CategoryEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_language, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(CategoryEditAdapter.ViewHolder holder, final int position) {
        SubCategoryModel language = languageArrayList.get(position);
        holder.edt_selectLang.setText(""+ (position+1) +"."+language.getSubCategoryName());


    }


    @Override
    public int getItemCount() {

        return languageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView edt_selectLang;


        ImageView imgCross;


        public ViewHolder(View view) {
            super(view);
            edt_selectLang = (CustomTextView) view.findViewById(R.id.edt_selectLang);


            imgCross=(ImageView)view.findViewById(R.id.imgCross);


        }

    }


}
