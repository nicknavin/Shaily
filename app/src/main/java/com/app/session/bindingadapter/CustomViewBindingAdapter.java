package com.app.session.bindingadapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.session.api.Urls;
import com.app.session.customview.CircleImageView;
import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;

public class CustomViewBindingAdapter {

    //    @BindingAdapter("imageUrlsss")
//    public  static  void setImageFromInternet(ImageView view, String url)
//    {
//        Glide.with(view.getContext()).load(url).placeholder(R.mipmap.ic_launcher).into(view);
//    }

//    @BindingAdapter({"imageUrl", "error"})
//    public static void loadImage(ImageView view, String url, Drawable error) {
//        Glide.with(view.getContext()).load(url).placeholder(error).into(view);
//    }


    /*The adapter is called if both imageUrl and error are used for an ImageView object and imageUrl is a string and error is a Drawable. If you want the adapter to be called when any of the attributes is set, you can set the optional requireAll flag of the adapter to false, as shown in the following example:*/

    @BindingAdapter(value = {"imageUrl", "error"},requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable errors)
    {
        String urls = Urls.BASE_IMAGES_URL +url;
        Glide.with(view.getContext()).load(urls).placeholder(errors).error(errors).into(view);
    }

    @BindingAdapter(value = {"circleimageUrl", "error"},requireAll = false)
    public static void loadCircleImage(CircleImageView view, String url, Drawable errors)
    {
        String urls = Urls.BASE_IMAGES_URL +url;
        Glide.with(view.getContext()).load(urls).placeholder(errors).error(errors).into(view);
    }

//    @BindingAdapter( "priceText")
//    public static String getFormattedPrice(Double price)
//    {
//        return Double.toString(price);
//    }
//
    @BindingAdapter( "salepriceText")
    public static void setPrice(TextView textView, Double price)
    {
        textView.setText(Double.toString(price)+" using binding");
    }

    @BindingAdapter( "rattingText")
    public static void setRatting(TextView textView, Float ratting)
    {
        textView.setText(Float.toString(ratting*ratting)+" using binding");
    }





}


