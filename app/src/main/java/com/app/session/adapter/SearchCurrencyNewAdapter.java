package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ObjectCallback;
import com.app.session.data.model.CurrencyBody;
import com.app.session.data.model.CurrencyRef;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchCurrencyNewAdapter extends RecyclerView.Adapter<SearchCurrencyNewAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<CurrencyBody> mArrayList;
    ArrayList<CurrencyBody> languageArrayList;
    ObjectCallback apiCallback;



    public SearchCurrencyNewAdapter(Context context, ArrayList<CurrencyBody> list, ObjectCallback callback) {
        this.context = context;
        languageArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchCurrencyNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchCurrencyNewAdapter.ViewHolder holder, final int position)
    {
        CurrencyBody CurrencyRef = languageArrayList.get(position);





        holder.txtItemName.setText(CurrencyRef.getCurrency_name());

        holder.txtItemName.setTag(CurrencyRef);
        holder.txtItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CurrencyBody body=(CurrencyBody)v.getTag();
               apiCallback.getObject(body,position,v);
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

                    ArrayList<CurrencyBody> filteredList = new ArrayList<>();

                    for (CurrencyBody CurrencyRef : mArrayList) {

                        if (CurrencyRef.getCurrency_name().toLowerCase().contains(charString) ) {

                            filteredList.add(CurrencyRef);
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
                languageArrayList = (ArrayList<CurrencyBody>) filterResults.values;
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
