package com.app.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ratufatechnologies on 27/10/16.
 */

public class Circle extends View {

    private static final int START_ANGLE_POINT = -90;

    private final Paint paint;

    private final Paint whitePaint;

    private final RectF rect;

    private  final RectF whiteRect;

    private float angle;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int strokeWidth = 0;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        //Circle color


        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //Circle color
        whiteRect = new RectF(0, 0, 220, 220);
        //size 200x200 example
        rect = new RectF(10, 10, 50, 50);

        //Initial Angle (optional, it can be zero)
        angle = 0;


    }


    // Define a method to generate a bitmap shader from a drawable resource
    private static Shader getDrawableShader(Context ctx, int resId) {
        Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), resId);
        return new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    // Outside of onDraw(), preferably on a background thread if possible
    Shader shader = getDrawableShader(getContext(), R.drawable.app_new_icon);
    Shader planeshader = getDrawableShader(getContext(), R.drawable.camera_button);

// In onDraw()


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.translate(getWidth()/4f,getHeight()/4f );
        whitePaint.setShader(planeshader);
        canvas.drawArc(whiteRect, -90, 360, true, whitePaint);
        paint.setShader(shader);
        canvas.drawArc(rect, START_ANGLE_POINT, angle, true, paint);

    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}