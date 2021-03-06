package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.DefaultCallback;
import com.app.session.data.model.Category;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<Category> mArrayList;
    ArrayList<Category> languageArrayList;
    DefaultCallback apiCallback;

    public SearchCategoryAdapter(Context context, ArrayList<Category> list, DefaultCallback callback) {
        this.context = context;
        languageArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchCategoryAdapter.ViewHolder holder, final int position)
    {
        Category category = languageArrayList.get(position);
        if(category.getCategoryName().equals("Add New Category"))
        {
            holder.txtItemName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.plus_icon, 0, 0, 0);
            holder.txtItemName.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else
        {
            holder.txtItemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        holder.txtItemName.setText(category.getCategoryName());

        holder.txtItemName.setTag(category);
        holder.txtItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Category category1=(Category)v.getTag();
               apiCallback.getCategory(category1);
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    @Override
    public int getItemCount() {

        return languageArrayList.size();
    }

    @Override
    public Filter getFilter()
    {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    languageArrayList = mArrayList;
                } else {

                    ArrayList<Category> filteredList = new ArrayList<>();

                    for (Category category : mArrayList) {

                        if (category.getCategoryName().toLowerCase().contains(charString) || category.getCategoryName().toLowerCase().contains(charString) || category.getCategoryName().toLowerCase().contains(charString)) {

                            filteredList.add(category);
                        }
                    }

                    languageArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = languageArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                languageArrayList = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtItemName;

        public ViewHolder(View view) {
            super(view);
            txtItemName = (CustomTextView) view.findViewById(R.id.txtItemName);




        }

    }


}
