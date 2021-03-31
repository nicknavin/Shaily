package com.app.session.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.app.session.R;
import com.app.session.activity.FilterConsultantActivity;
import com.app.session.customview.AnimatedExpandableListView;
import com.app.session.customview.CircleImageView;
import com.app.session.customview.CustomTextView;
import com.app.session.data.model.Profession;
import com.app.session.data.model.Specialist;

import java.util.ArrayList;


public class MyExpandableAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    ArrayList<Profession> category_item = new ArrayList<>();
    Context appContext;
    String lang_cd;

    public MyExpandableAdapter(Context C, ArrayList<Profession> item, String cd) {
        appContext = C;
        category_item = item;
        lang_cd = cd;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_specialist, null);
        }
        final Specialist specialist = category_item.get(groupPosition).getSpecial_List().get(childPosition);
        ((LinearLayout) convertView.findViewById(R.id.lay)).setTag(specialist);
        ((LinearLayout) convertView.findViewById(R.id.lay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout view = (LinearLayout) v;
                Specialist obj = (Specialist) view.getTag();
                Intent intent = new Intent(appContext, FilterConsultantActivity.class);
                intent.putExtra("ID", obj.getSubcategory_cd());
                intent.putExtra("LANG_ID", lang_cd);
                intent.putExtra("NAME", specialist.getProfession());
                appContext.startActivity(intent);

            }
        });

        ((CustomTextView) convertView.findViewById(R.id.txt_specialist)).setText(specialist.getProfession());
        String imageUrl = specialist.getIcon_address();
        System.out.println(" imageUrl .... " + imageUrl);

        CircleImageView profPic = (CircleImageView) convertView.findViewById(R.id.pic);
        if (imageUrl != null) {
            if (!imageUrl.equals("")) {
                AQuery aQuery = null;
                aQuery = new AQuery(appContext);
                aQuery.id(profPic).image(imageUrl, true, true, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
            } else {
                profPic.setImageResource(R.mipmap.profile_large);
            }
        } else {
            profPic.setImageResource(R.mipmap.profile_large);
        }

//        String imageUrl = specialist.getc;

        if (imageUrl != null) {
            if (!imageUrl.equals("")) {
                AQuery aQuery = null;
                aQuery = new AQuery(appContext);
                aQuery.id(profPic).image(imageUrl, true, true, 0, R.mipmap.profile_large, null, AQuery.FADE_IN);
            } else {
                profPic.setImageResource(R.mipmap.profile_large);
            }
        } else {
            profPic.setImageResource(R.mipmap.profile_large);
        }

        return convertView;
    }


    @Override
    public int getGroupCount() {
        return category_item.size();
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {

        int size = category_item.get(groupPosition).getSpecial_List().size();
        return size;

    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) appContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_profession, null);
        }

        ((CustomTextView) convertView.findViewById(R.id.txt_profession)).setText(category_item.get(groupPosition).getCategory());
        if (b) {
            ((ImageView) convertView.findViewById(R.id.indicator)).setImageResource(R.mipmap.dropdown_menu);
        } else {
            ((ImageView) convertView.findViewById(R.id.indicator)).setImageResource(R.mipmap.dropdown_menu01);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}