package com.app.session.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.session.R;
import com.google.android.exoplayer2.Player;
import com.potyvideo.library.AndExoPlayerView;

public class VideoPlayerActivity extends AppCompatActivity {
    AndExoPlayerView andExoPlayerView;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_video_player);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        if (getIntent().getStringExtra("URL") != null)
        {
            url = getIntent().getStringExtra("URL");

        }
        andExoPlayerView.setSource(url);
        andExoPlayerView.setShowFullScreen(true);
//


    }
    public void showToast(String x) {
        Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
//        andExoPlayerView.setShowFullScreen(true);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            andExoPlayerView.setShowFullScreen(false);
//            showToast("landscape");
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
////            andExoPlayerView.set
//        }
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//        {  andExoPlayerView.setShowFullScreen(false);
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            showToast("potriat");
//        }
    }

}