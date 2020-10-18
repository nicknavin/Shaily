package com.app.session.utility;

import android.app.Activity;
import android.view.Window;

import com.app.session.R;


/**
 * Created by USER on 05-11-2015.
 */
public class MyDialog {

    android.app.Dialog dd;

    //    android.app.Dialog dd;
    public void loading(Activity act) {


        dd = new android.app.Dialog(act);
        try {
            dd.setTitle("");
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            dd.getWindow().setLayout(-1, -2);
            dd.setCancelable(false);

            //((ProgressView)dd.findViewById(R.id.rey_loading)).setst

//            ((MyTextView)dd.findViewById(R.id.title)).setText(title);
//            ((MyTextView)dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void loadingSMS(Activity act, String msg) {


        dd = new android.app.Dialog(act);
        try {

            //dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.custom_loading);
            dd.getWindow().setLayout(-1, -2);
            dd.setCancelable(false);
//            ((CustomTextView)dd.findViewById(R.id.tv_loading)).setVisibility(View.VISIBLE);
//            ((CustomTextView)dd.findViewById(R.id.tv_loading)).setText(msg);
            //((ProgressView)dd.findViewById(R.id.rey_loading)).setst

//            ((MyTextView)dd.findViewById(R.id.title)).setText(title);
//            ((MyTextView)dd.findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dd.dismiss();
//                }
//            });
            dd.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void dismiss_loading() {
        if (dd != null && dd.isShowing()) {
            dd.dismiss();
        }
    }
}


