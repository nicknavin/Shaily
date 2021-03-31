package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.RegisterLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ITEGRITYPRO on 25-05-2018.
 */

public class AutoCompleteLangAdapter extends ArrayAdapter<RegisterLanguage> {
    ArrayList<RegisterLanguage> languageArrayList ,tempLanguage ,suggestions ,resultlist;

    public AutoCompleteLangAdapter(Context context, ArrayList<RegisterLanguage> languages) {
        super(context, android.R.layout.simple_list_item_1, languages);
        this.languageArrayList=languages;
        this.tempLanguage=new ArrayList<>(languages);
        this.suggestions=new ArrayList<>(languages);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegisterLanguage lang = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_language_preference, parent, false);
        }
        CustomTextView txtLang=(CustomTextView)convertView.findViewById(R.id.txtLang);
        txtLang.setTag(lang);
        txtLang.setText(lang.getLanguage_name());
        return convertView;
    }
    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            RegisterLanguage customer = (RegisterLanguage) resultValue;
            return customer.getLanguage_name();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                HashMap<String, RegisterLanguage> hashMap= new HashMap<>();
                for (RegisterLanguage people : tempLanguage) {
                    if (people.getLanguage_name().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        hashMap.put(people.getLanguage_cd(),people);
                    }
                }

                for(Map.Entry<String, RegisterLanguage> entry:hashMap.entrySet())
                {
                    suggestions.add(entry.getValue());
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<RegisterLanguage> c = (ArrayList<RegisterLanguage>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (RegisterLanguage cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
