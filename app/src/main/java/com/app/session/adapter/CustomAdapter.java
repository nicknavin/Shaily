package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.session.R;
import com.app.session.api.Urls;
import com.app.session.data.model.Country;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Oclemy on 8/4/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Country> countryArrayList;

    public CustomAdapter(Context c, ArrayList<Country> list) {
        this.c = c;
        this.countryArrayList = list;
    }

    @Override
    public int getCount() {
        return countryArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return countryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.row_spinner_country, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Country country = (Country) getItem(position);
        viewHolder.txtLang.setText(country.getCountryName());

        if(position!=0) {
            if (country.getCountry_icon() != null) {
                String url= Urls.BASE_IMAGES_URL+"userFiles/flags-mini/"+country.getCountry_cd()+".png";
                System.out.println("url "+url);
                Picasso.with(c).load(url).placeholder(R.mipmap.country_icon).into(viewHolder.img_flag);
                viewHolder.img_flag.setVisibility(View.VISIBLE);
            }
        }else
        {
            viewHolder.img_flag.setVisibility(View.GONE);
        }


        return convertView;
    }


    private class ViewHolder {
        TextView txtLang;
        ImageView img_flag;

        public ViewHolder(View view) {
            txtLang = (TextView) view.findViewById(R.id.text_view_spinner);
            img_flag = (ImageView) view.findViewById(R.id.img_flag);
        }
    }


}
