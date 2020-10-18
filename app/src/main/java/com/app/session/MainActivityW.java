package com.app.session;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.widget.Toast;
import android.annotation.TargetApi;

import com.app.session.base.BaseActivity;

import androidx.appcompat.widget.Toolbar;

/**
 * Shayan Rais (http://shanraisshan.com)
 * created on 7/28/2016
 */
public class MainActivityW extends BaseActivity implements View.OnClickListener {

    private LinearLayout attachmentLayout;
    private boolean isHidden = true;
    View dialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //for API-8 (doing only in main activity)
      //  toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        attachmentLayout = (LinearLayout) findViewById(R.id.menu_attachments);

        ImageButton btnDocument = (ImageButton) findViewById(R.id.menu_attachment_document);
        ImageButton btnCamera = (ImageButton) findViewById(R.id.menu_attachment_camera);
        ImageButton btnGallery = (ImageButton) findViewById(R.id.menu_attachment_gallery);
        ImageButton btnAudio = (ImageButton) findViewById(R.id.menu_attachment_audio);


        btnDocument.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnAudio.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        showMenu();

        switch (v.getId()) {
            case R.id.menu_attachment_document:
                Toast.makeText(MainActivityW.this, "Document Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_attachment_camera:
                Toast.makeText(MainActivityW.this, "Camera Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_attachment_gallery:
                Toast.makeText(MainActivityW.this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_attachment_audio:
                Toast.makeText(MainActivityW.this, "Audio Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_attachment_location:
                Toast.makeText(MainActivityW.this, "Location Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_attachment_contact:
                Toast.makeText(MainActivityW.this, "Contact Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//__________________________________________________________________________________________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.action_notification:
                //[os_lollipop_: play animation accordingly]

                    showMenu();
                return true;

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//__________________________________________________________________________________________________
//    void showMenuBelowLollipop() {
//        int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
//        int cy = attachmentLayout.getTop();
//        int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());
//
//        try {
//            Animator animator = ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.setDuration(300);
//
//            if (isHidden) {
//                //Log.e(getClass().getSimpleName(), "showMenuBelowLollipop");
//                attachmentLayout.setVisibility(View.VISIBLE);
//                animator.start();
//                isHidden = false;
//            } else {
//                Animator animatorReverse = animator.reverse();
//                animatorReverse.start();
//                animatorReverse.addListener(new SupportAnimator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart() {
//                    }
//
//                    @Override
//                    public void onAnimationEnd() {
//                        //Log.e("MainActivity", "onAnimationEnd");
//                        isHidden = true;
//                        attachmentLayout.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel() {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat() {
//                    }
//                });
//            }
//        } catch (Exception e) {
//            //Log.e(getClass().getSimpleName(), "try catch");
//            isHidden = true;
//            attachmentLayout.setVisibility(View.INVISIBLE);
//        }
//    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void showMenu() {
        int cx = (attachmentLayout.getLeft() + attachmentLayout.getRight());
        int cy = attachmentLayout.getTop();
        int radius = Math.max(attachmentLayout.getWidth(), attachmentLayout.getHeight());

        if (isHidden) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, 0, radius);
            attachmentLayout.setVisibility(View.VISIBLE);
            anim.start();
            isHidden = false;
        } else {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(attachmentLayout, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    attachmentLayout.setVisibility(View.INVISIBLE);
                    isHidden = true;
                }
            });
            anim.start();
        }
    }

    private void hideMenu() {
        attachmentLayout.setVisibility(View.GONE);
        isHidden = true;
    }
}
