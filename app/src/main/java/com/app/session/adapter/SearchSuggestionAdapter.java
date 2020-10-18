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

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<String> mArrayList;
    ArrayList<String> suggestionArrayList;
    DefaultCallback apiCallback;

    public SearchSuggestionAdapter(Context context, ArrayList<String> list, DefaultCallback callback) {
        this.context = context;
        suggestionArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchSuggestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchSuggestionAdapter.ViewHolder holder, final int position)
    {
        String category = suggestionArrayList.get(position);
        holder.txtItemName.setText(category);


        holder.txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             apiCallback.result(position);
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

        return suggestionArrayList.size();
    }

    @Override
    public Filter getFilter()
    {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    suggestionArrayList = mArrayList;
                } else {

                    ArrayList<String> filteredList = new ArrayList<>();

                    for (String category : mArrayList) {

                        if (category.toLowerCase().contains(charString) || category.toLowerCase().contains(charString) || category.toLowerCase().contains(charString)) {

                            filteredList.add(category);
                        }
                    }

                    suggestionArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestionArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                suggestionArrayList = (ArrayList<String>) filterResults.values;
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
