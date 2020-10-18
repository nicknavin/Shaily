package com.app.session.thumbyjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;


public class ThumbyUtils {

    public static final Bitmap getBitmapAtFrame(Context context, Uri uri, long frameTime, int width, int height) {

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//        if (ApiLevelHelper.INSTANCE.isAtLeast(Build.VERSION_CODES.Q)) {
//            mediaMetadataRetriever.setDataSource(uri.getPath());
//        } else {
//            mediaMetadataRetriever.setDataSource(context, uri);
//        }

        mediaMetadataRetriever.setDataSource(context, uri);

        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime,  MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

        try {
           // bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return bitmap;
    }
}
