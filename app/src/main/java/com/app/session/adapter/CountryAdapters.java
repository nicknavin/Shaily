package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.model.Country;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class CountryAdapters extends RecyclerView.Adapter<CountryAdapters.ViewHolder> {


    Context context;
    ArrayList<Country> subCategoryArrayList;
ApiCallback apiCallback;
    public CountryAdapters(Context context, ArrayList<Country> list, ApiCallback apiCallback) {
        this.context = context;
        subCategoryArrayList = list;
        this.apiCallback=apiCallback;
    }


    @Override
    public CountryAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_country, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(CountryAdapters.ViewHolder holder, final int position) {

        Country country= subCategoryArrayList.get(position);
        if(country.getDial_cd().isEmpty())
        {
            holder.img_flag.setVisibility(View.GONE);
        }else
        {
            holder.img_flag.setVisibility(View.VISIBLE);



            if(country.getCountry_icon()!=null) {
                String imageUrl=country.getCountry_icon();

                Picasso.with(context).load(imageUrl).memoryPolicy(MemoryPolicy.NO_STORE).placeholder(R.mipmap.profile_large).into(holder.img_flag);
            }
            else
            {
                holder.img_flag.setImageResource(R.mipmap.country_icon);
            }  }




        holder.txt.setText(country.getCountryName());

        holder.txt.setTag(country);

        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             apiCallback.result(""+position);
            }
        });


    }


    @Override
    public int getItemCount() {

        return subCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt;
        ImageView img_flag;
        CardView card_view;


        public ViewHolder(View view) {
            super(view);
            txt = (CustomTextView) view.findViewById(R.id.txtLang);
            img_flag=(ImageView)view.findViewById(R.id.img_flag);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }

    }


}
