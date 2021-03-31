package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Language;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class SelectedLanguageAdapter extends RecyclerView.Adapter<SelectedLanguageAdapter.ViewHolder> {



    Context context;
    private ArrayList<Language> languageArrayList = new ArrayList<>();

    ApiCallback apiCallback;
    public SelectedLanguageAdapter(Context context, ArrayList<Language> list)
    {
        languageArrayList=list;

        this.context = context;

    }
    public SelectedLanguageAdapter(Context context, ArrayList<Language> list, ApiCallback callback)
    {
        languageArrayList=list;

        this.context = context;
        apiCallback=callback;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedLanguageAdapter.ViewHolder holder, int position)
    {

Language language=languageArrayList.get(position);
holder.txtLanguageName.setText(language.getName());
holder.txtLanguageName.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        apiCallback.result(""+position);
    }
});

    }




    @Override
    public int getItemCount() {

        return languageArrayList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomTextView txtLanguageName;

        public ViewHolder(View view) {
            super(view);
            txtLanguageName=(CustomTextView)view.findViewById(R.id.txtLanguageName);

        }

    }



}
