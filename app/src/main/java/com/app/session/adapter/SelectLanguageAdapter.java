package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.activity.SelectLangaugeActivity;
import com.app.session.customview.CustomTextView;
import com.app.session.model.Language;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SelectLanguageAdapter extends RecyclerView.Adapter<SelectLanguageAdapter.ViewHolder> {


    Context context;
    ArrayList<Language> languageArrayList;
    Typeface tf;

    public SelectLanguageAdapter(Context context, ArrayList<Language> list) {
        this.context = context;
        languageArrayList = list;
        tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Regular.ttf");
    }


    @Override
    public SelectLanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(SelectLanguageAdapter.ViewHolder holder, final int position) {
        Language language = languageArrayList.get(position);
        holder.txt.setText(language.getNative_name());
        holder.checkbox.setChecked(language.isChecked());

        holder.checkbox.setText(language.getNative_name());
        holder.checkbox.setTypeface(tf);
        holder.checkbox.setTag(language);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox checkBox = (CheckBox) v;
                Language lang = (Language) checkBox.getTag();
                lang.setChecked(checkBox.isChecked());
                if (checkBox.isChecked())
                {
                    SelectLangaugeActivity.selectlanguageList.add(lang);
                } else {
                    SelectLangaugeActivity.selectlanguageList.remove(lang);
                }


//                if (SelectLangaugeActivity.selectlanguageList.size() <2 )
//                {
//
//                } else {
//                    lang.setChecked(false);
//                    SelectLangaugeActivity.selectlanguageList.remove(lang);
//                    Toast.makeText(context, context.getResources().getString(R.string.lang_selection_valid), Toast.LENGTH_SHORT).show();
//                }


                notifyItemChanged(position);

            }
        });


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
