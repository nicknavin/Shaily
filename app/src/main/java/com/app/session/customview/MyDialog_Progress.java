package com.app.session.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.rey.material.widget.ProgressView;


/**
 * Created by ebabu on 3/12/15.
 */
public class MyDialog_Progress {
    static Dialog dd = null;
    static ProgressView progressDialog;

    public static Dialog open(Context context) {
        dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_progress);
            progressDialog = (ProgressView) dd.findViewById(R.id.progress_pv_circular_inout);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.show();
            dd.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

    public static void close(Context context) {
        try {
            if (dd.isShowing() || dd != null){
                if(progressDialog!=null)
                progressDialog.stop();
            }
            dd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
