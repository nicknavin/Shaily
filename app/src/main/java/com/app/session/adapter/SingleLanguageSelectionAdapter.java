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
public class SingleLanguageSelectionAdapter extends RecyclerView.Adapter<SingleLanguageSelectionAdapter.ViewHolder> {


    Context context;
    private ArrayList<Language> languageArrayList = new ArrayList<>();

    ApiCallback apiCallback;
    boolean flagSelection = false;
    private int lastCheckedPosition = -1;
    public SingleLanguageSelectionAdapter(Context context, ArrayList<Language> list, ApiCallback callback) {
        languageArrayList = list;

        this.context = context;
        apiCallback = callback;

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
    public void onBindViewHolder(@NonNull SingleLanguageSelectionAdapter.ViewHolder holder, int position) {

        Language language = languageArrayList.get(position);
        holder.txtLanguageName.setText(language.getNative_name());
        if(lastCheckedPosition==position)
        {
            holder.txtLanguageName.setTextColor(context.getResources().getColor(R.color.red));
        }
        else
        {
            holder.txtLanguageName.setTextColor(context.getResources().getColor(R.color.black));
        }
//        else
//        {
//            holder.txtLanguageName.setTextColor(context.getResources().getColor(R.color.black));
//        }

            holder.txtLanguageName.setTag(language);
        holder.txtLanguageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                lastCheckedPosition=position;



                Language detail1 = (Language) view.getTag();
//                if (!detail1.isChecked())
//                {
//                    holder.txtLanguageName.setTextColor(context.getResources().getColor(R.color.tab_text_color));
//                    detail1.setChecked(true);
//                }
//                else
//                {
//                    holder.txtLanguageName.setTextColor(context.getResources().getColor(R.color.black));
//                detail1.setChecked(false);
//                }
                notifyDataSetChanged();
                apiCallback.result("" + position);

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
            txtLanguageName = (CustomTextView) view.findViewById(R.id.txtLanguageName);

        }

    }


}
