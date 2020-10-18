package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.CurrencyCallback;
import com.app.session.model.CurrencyRef;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SearchCurrencyAdapter extends RecyclerView.Adapter<SearchCurrencyAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<CurrencyRef> mArrayList;
    ArrayList<CurrencyRef> languageArrayList;
    CurrencyCallback apiCallback;



    public SearchCurrencyAdapter(Context context, ArrayList<CurrencyRef> list, CurrencyCallback callback) {
        this.context = context;
        languageArrayList = list;
        mArrayList=list;
        apiCallback = callback;
    }


    @Override
    public SearchCurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final SearchCurrencyAdapter.ViewHolder holder, final int position)
    {
        CurrencyRef CurrencyRef = languageArrayList.get(position);





        holder.txtItemName.setText(CurrencyRef.getCurrency_name_en());

        holder.txtItemName.setTag(CurrencyRef);
        holder.txtItemName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CurrencyRef CurrencyRef1=(CurrencyRef)v.getTag();
               apiCallback.getTitle(CurrencyRef1);
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

                    ArrayList<CurrencyRef> filteredList = new ArrayList<>();

                    for (CurrencyRef CurrencyRef : mArrayList) {

                        if (CurrencyRef.getCurrency_name_en().toLowerCase().contains(charString) || CurrencyRef.getCurrency_name_en().toLowerCase().contains(charString) || CurrencyRef.getCurrency_name_en().toLowerCase().contains(charString)) {

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
                languageArrayList = (ArrayList<CurrencyRef>) filterResults.values;
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
