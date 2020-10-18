package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class CustomSpinnerGenderAdapters extends RecyclerView.Adapter<CustomSpinnerGenderAdapters.ViewHolder> {


    Context context;
    ArrayList<String> genderArrayList;
ApiCallback apiCallback;
    public CustomSpinnerGenderAdapters(Context context, ArrayList<String> list, ApiCallback apiCallback) {
        this.context = context;
        genderArrayList = list;
        this.apiCallback=apiCallback;
    }


    @Override
    public CustomSpinnerGenderAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner_country, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(CustomSpinnerGenderAdapters.ViewHolder holder, final int position) {


        holder.txt.setText(genderArrayList.get(position));
        if(genderArrayList.get(position).equals("Male"))
        {
            holder.img_flag.setImageResource(R.mipmap.ic_male);
        }
        else
        {
            holder.img_flag.setImageResource(R.mipmap.ic_female);
        }

        holder.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             apiCallback.result(""+position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return genderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt;
        ImageView img_flag;
        LinearLayout layItem;



        public ViewHolder(View view) {
            super(view);
            txt = (CustomTextView) view.findViewById(R.id.text_view_spinner);
            img_flag=(ImageView)view.findViewById(R.id.img_flag);
            layItem=(LinearLayout)view.findViewById(R.id.layItem);
        }

    }


}
