package com.app.session.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.service.MyForgroundService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutionException;

public class HomeUserChatProfileActivity extends BaseActivity {
//navih
    BottomNavigationView bottomNavigationView;
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

        setProfileMenuIcon();

    }


    private void setProfileMenuIcon()
    {
        MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.navigation_profile);

        Glide.with(this).asBitmap().load(profileUrl).placeholder(R.drawable.ic_baseline_account_circle_24).apply(RequestOptions.circleCropTransform()).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (resource != null) {

                    Drawable mBitmapDrawable = new BitmapDrawable(getResources(), resource);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        menuItem.setIconTintMode(PorterDuff.Mode.DST);
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



    public void startService() {
        Intent serviceIntent = new Intent(this, MyForgroundService.class);
        serviceIntent.putExtra(MyForgroundService.EXTRA_EVENT_TYPE, MyForgroundService.EVENT_TYPE_JOIN);
        serviceIntent.putExtra("userId",userId);
        ContextCompat.startForegroundService(this, serviceIntent);
    }





}