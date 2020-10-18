package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.app.session.R;
import com.app.session.customview.CustomTextView;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerCAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<String> items;
    private final int mResource;

    public SpinnerCAdapter(@NonNull Context context, @LayoutRes int resource,
                           @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        CustomTextView offTypeTv = (CustomTextView) view.findViewById(R.id.txtSpinner);
        ImageView imageView=(ImageView)view.findViewById(R.id.imgGender);
        if(items.get(position).equals("Male"))
        {
            imageView.setImageResource(R.mipmap.ic_male);
        }
        if(items.get(position).equals("Female"))
        {
            imageView.setImageResource(R.mipmap.ic_female);
        }
        offTypeTv.setText( items.get(position));


        return view;
    }
}