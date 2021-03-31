package com.app.session.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.app.session.R;
import com.app.session.customview.CircleImageView;
import com.app.session.data.model.AllStory;
import com.app.session.utility.DynamicHeightImageView;
import com.app.session.utility.DynamicHeightTextView;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Random;

/***
 * ADAPTER
 */

public class SampleAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SampleAdapter";
    Context contxt;

    static class ViewHolder {
        DynamicHeightTextView txtLineOne, txt_userName;
        CircleImageView profPic;
        DynamicHeightImageView img;
        ProgressView rey_loading;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    ArrayList<AllStory> storyGroupArrayList = new ArrayList<>();

    public SampleAdapter(Context context, final int textViewResourceId, ArrayList<AllStory> list) {
        super(context, textViewResourceId);
        storyGroupArrayList = list;
        contxt = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();

        mBackgroundColors.add(R.color.green);

        mBackgroundColors.add(R.color.grey);
    }


    @Override
    public int getCount() {
        return storyGroupArrayList.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.txt_userName = (DynamicHeightTextView) convertView.findViewById(R.id.txt_userName);

            vh.img = (DynamicHeightImageView) convertView.findViewById(R.id.img);
            vh.profPic = (CircleImageView) convertView.findViewById(R.id.imgProfile);
            vh.rey_loading = (ProgressView) convertView.findViewById(R.id.rey_loading);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(getItem(position) + position);
        String video_url = null, img_url = null;
        if (position < storyGroupArrayList.size()) {
            AllStory allStory = storyGroupArrayList.get(position);
            if (allStory.getUser_name().isEmpty() || allStory.getUser_name().equals("null")) {
                vh.txt_userName.setText("");
            } else {
                vh.txt_userName.setText(allStory.getUser_name());
            }

            AQuery aQuery = null;
            aQuery = new AQuery(contxt);

            if (allStory.getType().equals("2")) {
                if (allStory.getStory_text() != null && !allStory.getStory_text().equals("")) {
                    String url = allStory.getStory_text();

                    // aQuery.id(vh.img).image(url, false, false, 0, R.mipmap.ic_photo_cont, null, AQuery.FADE_IN_NETWORK);
                    aQuery.id(vh.img).image(url, true, true, 0, 0, new BitmapAjaxCallback() {

                        @Override
                        public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {

                            if(status.getCode()==200) {
                                iv.setImageBitmap(bm);
                            }
                            else
                            {
                                iv.setImageResource(R.mipmap.ic_photo_cont);
                            }
                            vh.rey_loading.stop();

                            //do something to the bitmap
                            // iv.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);

                        }
                    });

                } else {
                    vh.img.setImageResource(R.mipmap.ic_photo_cont);
                }
            } else if (allStory.getType().equals("3")) {


                if (allStory.getStory_text() != null) {


                    if (allStory.getThumbnail_text().contains("mp4") && allStory.getThumbnail_text() != null) {
                        img_url = allStory.getStory_text();
                        video_url = allStory.getThumbnail_text();
                    } else {
                        video_url = allStory.getStory_text();
                        img_url = allStory.getThumbnail_text();
                    }
                    // aQuery.id(vh.img).image(img_url, false, false, 0, R.mipmap.ic_photo_cont, null, AQuery.FADE_IN_NETWORK);
                    aQuery.id(vh.img).image(img_url, true, true, 0, 0, new BitmapAjaxCallback() {

                        @Override
                        public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {

                            if(status.getCode()==200) {
                                iv.setImageBitmap(bm);
                            }
                            else
                            {
                                iv.setImageResource(R.mipmap.ic_photo_cont);
                            }
                            vh.rey_loading.stop();
                            //do something to the bitmap
                            // iv.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);

                        }
                    });
                }

            }


            if (allStory.getImgUrl() != null && !allStory.getImgUrl().equals("")) {
                System.out.println("getImgUrl " + allStory.getImageUrl());
                String prof_imageUrl = allStory.getImgUrl();

                aQuery.id(vh.profPic).image(prof_imageUrl, false, false, 0, R.mipmap.ic_photo_cont, null, AQuery.FADE_IN_NETWORK);

            } else {
                vh.profPic.setImageResource(R.mipmap.ic_photo_cont);
            }
        }
//        if (position == 0) {
//            vh.img.setImageResource(R.mipmap.demo);
//        }
//        if (position == 1) {
//            vh.img.setImageResource(R.mipmap.chip);
//        }
//        if (position == 2) {
//            vh.img.setImageResource(R.mipmap.demo_icon);
//        }
//        if (position == 3) {
//            vh.img.setImageResource(R.mipmap.demo7);
//        }
//
//        if (position > 3) {
//            if (position % 2 == 0) {
//                vh.img.setImageResource(R.mipmap.demos);
//            } else {
//                vh.img.setImageResource(R.mipmap.demo7);
//            }
//        }
        return convertView;
    }

    private double getPositionRatio(final int position) {
        //      SnapStoryGroup snapStoryGroup=storyGroupArrayList.get(position);
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}