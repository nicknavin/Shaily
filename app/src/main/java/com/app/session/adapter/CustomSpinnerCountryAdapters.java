package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.customview.CustomTextView;
import com.app.session.interfaces.ApiCallback;
import com.app.session.model.Country;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Navya on 28-04-2016.
 */
public class CustomSpinnerCountryAdapters extends RecyclerView.Adapter<CustomSpinnerCountryAdapters.ViewHolder> {


    Context context;
    ArrayList<Country> countryArrayList;
ApiCallback apiCallback;
    public CustomSpinnerCountryAdapters(Context context, ArrayList<Country> list, ApiCallback apiCallback) {
        this.context = context;
        countryArrayList = list;
        this.apiCallback=apiCallback;
    }


    @Override
    public CustomSpinnerCountryAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner_country, parent, false);
        ViewHolder vh;
        vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(CustomSpinnerCountryAdapters.ViewHolder holder, final int position) {

            Country country= countryArrayList.get(position);
            holder.img_flag.setVisibility(View.VISIBLE);
            //if(country.getCountry_icon()!=null)
            {
                String imageUrl= Urls.BASE_IMAGES_URL+"userFiles/flags-mini/"+country.getCountry_cd().toLowerCase()+".png";
                System.out.println("country url"+imageUrl);
                Picasso.with(context).load(imageUrl).memoryPolicy(MemoryPolicy.NO_STORE).placeholder(R.mipmap.country_icon).into(holder.img_flag);
            }
//            else
//            {
//                holder.img_flag.setImageResource(R.mipmap.country_icon);
//            }




        holder.txt.setText(country.getCountryName());

        holder.layItem.setTag(country);

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

        return countryArrayList.size();
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
