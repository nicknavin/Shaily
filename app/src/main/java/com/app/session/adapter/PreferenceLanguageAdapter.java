package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.model.Language;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class PreferenceLanguageAdapter extends RecyclerView.Adapter<PreferenceLanguageAdapter.ViewHolder> {


    Context context;
    ArrayList<Language> languageArrayList;
    ApiCallback apiCallback;

    public PreferenceLanguageAdapter(Context context, ArrayList<Language> list, ApiCallback callback) {
        this.context = context;
        languageArrayList = list;
        apiCallback = callback;
    }


    @Override
    public PreferenceLanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language_preference, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(final PreferenceLanguageAdapter.ViewHolder holder, final int position)
    {
        Language language = languageArrayList.get(position);

        holder.txt.setText(language.getNative_name());

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiCallback.result("" + position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return languageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt;



        public ViewHolder(View view) {
            super(view);
            txt = (CustomTextView) view.findViewById(R.id.txtLang);



        }

    }


}
