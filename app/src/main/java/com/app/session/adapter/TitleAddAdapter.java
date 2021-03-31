package com.app.session.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiItemCallback;
import com.app.session.data.model.TitleRef;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class TitleAddAdapter extends RecyclerView.Adapter<TitleAddAdapter.ViewHolder> {


    Context context;
    private ArrayList<TitleRef> titleRefsList = new ArrayList<>();
private ApiItemCallback apiItemCallback;
    public TitleAddAdapter(Context context, ArrayList<TitleRef> list, ApiItemCallback callback) {
        this.context = context;
        titleRefsList = list;
        this.apiItemCallback=callback;
    }


    @Override
    public TitleAddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selected_category_layout, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(TitleAddAdapter.ViewHolder holder, final int position) {
        TitleRef titleRef = titleRefsList.get(position);
        holder.txtTitle.setText(titleRef.getTitle_name());
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiItemCallback.result(position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return titleRefsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtTitle;

      CardView card_view;


        public ViewHolder(View view) {
            super(view);
            txtTitle = (CustomTextView) view.findViewById(R.id.txtCategory);
            card_view = (CardView) view.findViewById(R.id.card_view);




        }

    }


}
