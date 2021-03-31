package com.app.session.adapter;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.data.model.Language;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class UpdatedLanguageAdapter extends RecyclerView.Adapter<UpdatedLanguageAdapter.ViewHolder> {


    Context context;
    ArrayList<Language> languageArrayList;
    Typeface tf;
ApiCallback apiCallback;
    public UpdatedLanguageAdapter(Context context, ArrayList<Language> list, ApiCallback callback) {
        this.context = context;
        languageArrayList = list;
        apiCallback=callback;
        tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Regular.ttf");
    }


    @Override
    public UpdatedLanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language_preference, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(UpdatedLanguageAdapter.ViewHolder holder, final int position) {
        Language language = languageArrayList.get(position);
        holder.txt.setText(language.getNative_name());
        if(language.isChecked())
        {
            holder.cardView.setVisibility(View.GONE);
        }

   holder.txt.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           apiCallback.result(""+position);
       }
   });


    }


    @Override
    public int getItemCount() {

        return languageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt;
        CardView cardView;


        public ViewHolder(View view) {
            super(view);
            txt = (CustomTextView) view.findViewById(R.id.txtLang);
            cardView=(CardView)view.findViewById(R.id.card_view);
//            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
//            lay = (LinearLayout) view.findViewById(R.id.lay);


        }

    }


}
