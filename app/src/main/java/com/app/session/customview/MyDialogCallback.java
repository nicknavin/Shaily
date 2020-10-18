package com.app.session.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.interfaces.ApiCallback;


/**
 * Created by win8 on 9/11/2015.
 */
public class MyDialogCallback {

    public static Dialog iPhone (String title, Context context, final ApiCallback callback) {


        final Dialog dd = new Dialog(context);
        try {
            dd.getWindow ().requestFeature (Window.FEATURE_NO_TITLE);
            dd.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);
            dd.setContentView (R.layout.dialog_iphone);
            dd.getWindow ().setLayout (-1, -2);
            dd.getWindow ().setLayout (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((CustomTextView) dd.findViewById (R.id.title)).setText (title);
            ((CustomTextView) dd.findViewById (R.id.yes)).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    dd.dismiss ();
                    callback.result("");
                }
            });
            dd.show ();

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return dd;
    }
}





