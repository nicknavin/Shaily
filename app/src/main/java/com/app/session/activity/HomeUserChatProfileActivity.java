package com.app.session.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.service.MyForgroundService;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

public class HomeUserChatProfileActivity extends BaseActivity {

    BottomNavigationView bottomNavigationView;
    MenuItem menuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user_chat_profile);
        setUpNavigation();
        startService();
    }
    public void setUpNavigation(){

        bottomNavigationView =findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,
                navHostFragment.getNavController());



    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfileMenuIcon();
    }

    private void setProfileMenuIcon()
    {
        menuItem = bottomNavigationView.getMenu().findItem(R.id.navigation_profile);
//        MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.navigation_profile);
        String encodeImage=DataPrefrence.getPref(context, Constant.ENCODED_IMAGE,"");
        if(!encodeImage.equalsIgnoreCase(""))
        {
            byte[] b = Base64.decode(encodeImage, Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);

            Bitmap bitmap=getCroppedBitmap(bm);

            if (bitmap!=null) {
                Drawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    menuItem.setIconTintMode(PorterDuff.Mode.DST_IN);
                }

                menuItem.setIcon(mBitmapDrawable);
            }

        }
else {
            mlog("setProfileMenuIcon .. " + profileUrl);
            Glide.with(this).asBitmap().load(profileUrl).placeholder(R.drawable.ic_baseline_account_circle_24).apply(RequestOptions.circleCropTransform()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    if (resource != null) {

                        Drawable mBitmapDrawable = new BitmapDrawable(getResources(), resource);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            menuItem.setIconTintMode(PorterDuff.Mode.DST_IN);

                        }

                        menuItem.setIcon(mBitmapDrawable);


                    } else {
                        menuItem.setIcon(R.drawable.ic_baseline_account_circle_24);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }


    }


    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
    public void startService() {
        Intent serviceIntent = new Intent(this, MyForgroundService.class);
        serviceIntent.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_TYPE_JOIN);
        serviceIntent.putExtra("userId",userId);
        ContextCompat.startForegroundService(this, serviceIntent);
    }





}