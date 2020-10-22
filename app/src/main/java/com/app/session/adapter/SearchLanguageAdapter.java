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
import com.app.session.model.Language;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchLanguageAdapter extends RecyclerView.Adapter<SearchLanguageAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<Language> mArrayList;
    ArrayList<Language> languageArrayList;
    DefaultCallback apiCallback;

    public SearchLanguageAdapter(Context context, ArrayList<Language> list, DefaultCallback callback) {
        this.context = context;
        languageArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchLanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchLanguageAdapter.ViewHolder holder, final int position)
    {
        Language language = languageArrayList.get(position);
        holder.txtItemName.setText(language.getName());

holder.txtItemName.setTag(language);
        holder.txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Language language1=(Language)v.getTag();
                apiCallback.getLanguage(language1);
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

                    ArrayList<Language> filteredList = new ArrayList<>();

                    for (Language androidVersion : mArrayList) {

                        if (androidVersion.getName().toLowerCase().contains(charString))
                        {
                            filteredList.add(androidVersion);
                        }
                    }

                    languageArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = languageArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                languageArrayList = (ArrayList<Language>) filterResults.values;

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
