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
import com.app.session.model.Language;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {


    Context context;
    ArrayList<Language> languageArrayList;
    Typeface tf;

    public LanguageAdapter(Context context, ArrayList<Language> list) {
        this.context = context;
        languageArrayList = list;

    }


    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder holder, final int position) {

        Language language = languageArrayList.get(position);
        holder.txt.setText(""+(position+1)+"."+language.getNative_name());
        holder.txt.setVisibility(View.VISIBLE);
        holder.checkbox.setVisibility(View.GONE);
            }


    @Override
    public int getItemCount() {

        return languageArrayList.size();
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
