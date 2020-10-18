package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.model.SubCategoryModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SubCategoryAdapters extends RecyclerView.Adapter<SubCategoryAdapters.ViewHolder> {


    Context context;
    ArrayList<SubCategoryModel> subCategoryArrayList;
    Typeface tf;

    public SubCategoryAdapters(Context context, ArrayList<SubCategoryModel> list) {
        this.context = context;
        subCategoryArrayList = list;
        tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Regular.ttf");  }


    @Override
    public SubCategoryAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SubCategoryAdapters.ViewHolder holder, final int position) {
        SubCategoryModel subCategory=subCategoryArrayList.get(position);
        holder.txt.setText(subCategory.getSubCategoryName());
        holder.checkbox.setChecked(subCategory.isChecked());
        holder.checkbox.setText(subCategory.getSubCategoryName());
        holder.checkbox.setTypeface(tf);
        holder.checkbox.setTag(subCategory);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                SubCategoryModel subCategoryModel = (SubCategoryModel) checkBox.getTag();
                subCategoryModel.setChecked(checkBox.isChecked());
                notifyItemChanged(position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return subCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt;
        CheckBox checkbox;
        LinearLayout lay;


        public ViewHolder(View view) {
            super(view);
            txt = (CustomTextView) view.findViewById(R.id.txtLang);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            lay = (LinearLayout) view.findViewById(R.id.lay);


        }

    }


}
