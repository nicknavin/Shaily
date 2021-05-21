package com.app.session.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CustomPager extends ViewPager {
    private View mCurrentView;
    private Boolean mAnimStarted = false;

    public CustomPager(Context context) {
        super(context);
    }

    public CustomPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(!mAnimStarted && null != getAdapter())
        {
            int height = 0;
            mCurrentView = ((FragmentPagerAdapter) getAdapter()).getItem(getCurrentItem()).getView();

            if (mCurrentView != null) {
                mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = mCurrentView.getMeasuredHeight();
//                if (VersionUtils.isJellyBean() && height < getMinimumHeight())
//                {
//                    height = getMinimumHeight();
//                }
            }

            // Not the best place to put this animation, but it works pretty good.
            int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight)
            {
                final int targetHeight = height;
                final int currentHeight = getLayoutParams().height;
                final int heightChange = targetHeight - currentHeight;

                Animation a = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        if (interpolatedTime >= 1) {
                            getLayoutParams().height = targetHeight;
                        } else {
                            int stepHeight = (int) (heightChange * interpolatedTime);
                            getLayoutParams().height = currentHeight + stepHeight;
                        }
                        requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mAnimStarted = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mAnimStarted = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                a.setDuration(1000);
                startAnimation(a);
                mAnimStarted = true;
            } else {
                heightMeasureSpec = newHeight;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }
}