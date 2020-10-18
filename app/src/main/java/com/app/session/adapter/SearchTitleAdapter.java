package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.TitleCallback;
import com.app.session.model.TitleRef;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchTitleAdapter extends RecyclerView.Adapter<SearchTitleAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<TitleRef> mArrayList;
    ArrayList<TitleRef> languageArrayList;
    TitleCallback apiCallback;


    public SearchTitleAdapter(Context context, ArrayList<TitleRef> list, TitleCallback callback) {
        this.context = context;
        languageArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchTitleAdapter.ViewHolder holder, final int position)
    {
        TitleRef titleRef = languageArrayList.get(position);

        if(titleRef.getTitle_name().equals("Add New Title"))
        {
            holder.txtItemName.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.plus_icon, 0, 0, 0);
            holder.txtItemName.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else
        {
            holder.txtItemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }



        holder.txtItemName.setText(titleRef.getTitle_name());

        holder.txtItemName.setTag(titleRef);
        holder.txtItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TitleRef titleRef1=(TitleRef)v.getTag();
               apiCallback.getTitle(titleRef1);
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

                    ArrayList<TitleRef> filteredList = new ArrayList<>();

                    for (TitleRef titleRef : mArrayList) {

                        if (titleRef.getTitle_name().toLowerCase().contains(charString) || titleRef.getTitle_name().toLowerCase().contains(charString) || titleRef.getTitle_name().toLowerCase().contains(charString)) {

                            filteredList.add(titleRef);
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
                languageArrayList = (ArrayList<TitleRef>) filterResults.values;
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
