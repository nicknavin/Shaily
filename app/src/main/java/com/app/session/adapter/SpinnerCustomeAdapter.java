package com.app.session.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.app.session.R;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Brief_CV;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerCustomeAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Brief_CV> items;
    private final int mResource;

    public SpinnerCustomeAdapter(@NonNull Context context, @LayoutRes int resource,
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
        final View view = LayoutInflater.from(mContext).inflate(mResource, parent, false);

        CustomTextView offTypeTv = (CustomTextView) view.findViewById(R.id.txtLanguageName);


        Brief_CV offerData = items.get(position);

        offTypeTv.setText(offerData.getLanguage_id().getName());


        return view;
    }
}