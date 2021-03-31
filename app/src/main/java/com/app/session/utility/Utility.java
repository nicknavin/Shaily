package com.app.session.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.app.session.BuildConfig;
import com.app.session.R;
import com.app.session.activity.SplashActivity;
import com.app.session.api.Urls;
import com.app.session.data.model.StoryModel;
import com.app.session.data.model.UserLangauges;
import com.app.session.data.model.UserStory;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.palette.graphics.Palette;


public class Utility {

    public static Context appContext;
    private static String PREFERENCE = "Consultlot";
    private static int MAX_IMAGE_DIMENSION = 720;
    public static Snackbar snackbar;
    public static Utility utility;

    public static void showInternetConnectionToast(Context context) {
        Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
    }

    public static Utility getInstant() {

        if (utility == null) {
            utility = new Utility();
        }
        return utility;
    }

    public static File callBitmaptoFile(Bitmap bitmap, String name, Context context) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


        return imageFile;
    }





    public static String getCalculatedDate(String date) {
        String[] dates = date.split("T");
//        date="2020-08-10";
        date = dates[0];
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(c);
            endDate = df.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }


        long duration = endDate.getTime() - startDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long years = (days / 365);

        long weeks = (days % 365) / 7;
        days = (days % 365) % 7;
        if (weeks > 0) {
            return "" + weeks + "week";
        } else {
            if (days == 0) {
                return "";
            } else {
                return "" + days + "days";
            }
        }
    }


    public static String changeDateFormate(String date) {

        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("dd/M/yyyy");
            Date dateObj = curFormater.parse(date);
            SimpleDateFormat postFormater = new SimpleDateFormat("dd/MMMM/yyyy");

            date = postFormater.format(dateObj);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDate() {
        String date = null;
        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd/MMMM/yyyy");
            date = df.format(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String changeTimeFormate(String time_formate) {

        String time = "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        DateFormat formatter2 = new SimpleDateFormat("hh:mm a", Locale.US);
        try {
            Date date = formatter.parse(time_formate);
            time = formatter2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    static int pic_color = 0x000000;

    public static int getDominantColor(Bitmap bitmap) {

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //work with the palette here
                int defaultValue = 0x000000;
                int vibrant = palette.getVibrantColor(defaultValue);
                int vibrantLight = palette.getLightVibrantColor(defaultValue);
                int vibrantDark = palette.getDarkVibrantColor(defaultValue);
                int muted = palette.getMutedColor(defaultValue);
                int mutedLight = palette.getLightMutedColor(defaultValue);
                int mutedDark = palette.getDarkMutedColor(defaultValue);


//                lay0.setBackgroundColor(vibrant);
//                lay1.setBackgroundColor(vibrantLight);
//                lay2.setBackgroundColor(vibrantDark);
//                lay3.setBackgroundColor(muted);
//                lay4.setBackgroundColor(mutedLight);
//                lay5.setBackgroundColor(mutedDark);
                pic_color = vibrantLight;

            }
        });
        return pic_color;
    }

    public static String TimeFormate(String time_formate) {

        String time = "";

        DateFormat formatter2 = new SimpleDateFormat("hh:mm a", Locale.US);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        try {
            Date date = formatter2.parse(time_formate);
            time = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static String getDecimalFormat(double value) {
        DecimalFormat df2 = new DecimalFormat(".######");
        String output = df2.format(value);
        return output;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static String getMediaFile() {

        try {
            File flyerFolder = null;
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String path = "Flyerplane/Media/FlyerplaneImages/sent";
                flyerFolder = new File(sd, path);
                if (!flyerFolder.exists()) {
                    flyerFolder.mkdirs();
                    flyerFolder.getAbsolutePath();
                    System.out.println("fly path " + flyerFolder.getAbsolutePath());
                }

            }
            return flyerFolder.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static Drawable getImageFromURL(String photoDomain) {
//        Drawable drawable = null;
//        try {
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpGet request = new HttpGet(photoDomain.trim());
//            HttpResponse response = httpClient.execute(request);
//            InputStream is = response.getEntity().getContent();
//            drawable = Drawable.createFromStream(is, "src");
//        } catch (MalformedURLException e) {
//        } catch (IOException e) {
//        }
//        return drawable;
//    }

//    public static String postParamsAndfindJSON(String url, ArrayList<NameValuePair> params) {
//        String result = "";
//
//        System.out.println("URL comes in jsonparser class is:  " + url + params);
//        try {
//            int TIMEOUT_MILLISEC = 100000; // = 10 seconds
//            HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
//            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//            // httpGet.setURI(new URI(url));
//
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            InputStream is = httpResponse.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//
//            is.close();
//            result = sb.toString();
//
//        } catch (Exception e) {
//            System.out.println("exception in jsonparser class ........");
//            e.printStackTrace();
//            result = "";
//            return result;
//        }
//        return result;
//    }

    public static void log(String str)
    {
        // System.out.println(str);
        // Log.i("LOG",str);
        Log.d("tag",str);
    }

    static Locale myLocale;

    public static void setLocale(String lang, Context context) {

        myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    public static void setLang(Context context, String lang) {
        Locale locale;

        lang = DataPrefrence.getPref(context, Constant.PREFERED_LANGUAGE, "");
        if (lang.equals("ar")) {
            locale = new Locale("ar");
        } else {
            locale = new Locale("en");
        }

        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
    }
/*	public static String multiPart(String url,MultipartEntity entity) {
        String result="";
		HttpClient httpclient;

		try {
	    httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    httppost.setEntity(entity);
		System.out.println("cvsc" + httppost);
		HttpResponse response = httpclient.execute(httppost);
		result = EntityUtils.toString(response.getEntity());
		System.out.println("Given Result to photo  " + result);

	} catch (Exception e) {
		// TODO: handle exception
	}
		return result;

	}*/


    public static Bitmap DownloadImageDirect(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        // String imageEncoded = Base64Coder.encodeTobase64(image);

        // Log.d("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static void alertBoxShow(Context context, int msg) {
        // set dialog for user's current location set for searching theaters.
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alert");
        dialog.setMessage(msg);
        dialog.setPositiveButton(" Ok ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void ShowStringAlertWithMessage(Context context, int alerttitle,
                                                  int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void ShowStringAlert2WithMessage(final Context context, int alerttitle,
                                                   int locationvalidation) {
        // Assign the alert builder , this can not be assign in Click events
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage(locationvalidation);
        builder.setTitle(alerttitle);
        // Set behavior of negative button
        builder.setPositiveButton("GET STARTED", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((DashboardPatient) context).moveToNextActivity("GET STARTED");
                //Intent intent=new Intent(context,GetStartedActivity.class);
            }
        });
        builder.setNegativeButton("FAQ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the dialog
                dialog.cancel();
                //((DashboardPatient) context).moveToNextActivity("FAQ");
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //    public static String findJSONFromUrl(String url) {
//        String result = "";
//
//        System.out.println("URL comes in jsonparser class is:  " + url);
//        try {
//            int TIMEOUT_MILLISEC = 100000; // = 10 seconds
//            HttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams,
//                    TIMEOUT_MILLISEC);
//            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(url);
//            // httpGet.setURI(new URI(url));
//
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            InputStream is = httpResponse.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//
//            is.close();
//            result = sb.toString();
//            System.out.println("result  in jsonparser class ........" + result);
//
//        } catch (Exception e) {
//            System.out.println("exception in jsonparser class ........");
//            result = null;
//        }
//        return result;
//    }
    public static void ShowSnackbarOther(Context context, View v, String msg) {

        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

        View snackbarView = snackbar.getView();
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
        textView.setMaxLines(5);

        snackbar.show();

    }

    public static Bitmap getBitmap(String url) {
        Bitmap imageBitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            try {
                imageBitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
                System.out.println("exception in get bitma putility");
            }

            bis.close();
            is.close();
            final int IMAGE_MAX_SIZE = 50;
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                // b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = imageBitmap.getHeight();
                int width = imageBitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) x, (int) y, true);
                imageBitmap.recycle();
                imageBitmap = scaledBitmap;

                System.gc();
            } else {
                // b = BitmapFactory.decodeStream(in);
            }

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            System.out.println("exception in get bitma putility");
        } catch (Exception e) {
            System.out.println("exception in get bitma putility");
            e.printStackTrace();
        }
        return imageBitmap;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }
    }

    public static byte[] scaleImage(Context context, Uri photoUri)
            throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = 0;// getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION
                || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth)
                    / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight)
                    / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                    srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*
         * if (type.equals("image/png")) {
         * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
         * (type.equals("image/jpg") || type.equals("image/jpeg")) {
         * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
         */
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }

    static int mMaxWidth, mMaxHeight;

    @SuppressWarnings("deprecation")
    public static Bitmap loadResizedImage(Context mContext, final File imageFile) {
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mMaxWidth = display.getWidth();
        mMaxHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        int scale = calculateInSampleSize(options, mMaxWidth, mMaxHeight);
        while (options.outWidth / scale > mMaxWidth
                || options.outHeight / scale > mMaxHeight) {
            scale++;
        }
        Bitmap bitmap = null;
        Bitmap scaledBitmap = null;
        if (scale > 1) {
            try {
                scale--;
                options = new BitmapFactory.Options();
                options.inSampleSize = scale;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPurgeable = true;
                options.inTempStorage = new byte[16 * 100];
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),
                        options);
                if (bitmap == null) {
                    return null;
                }

                // resize to desired dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double newWidth;
                double newHeight;
                if ((double) width / mMaxWidth < (double) height / mMaxHeight) {
                    newHeight = mMaxHeight;
                    newWidth = (newHeight / height) * width;
                } else {
                    newWidth = mMaxWidth;
                    newHeight = (newWidth / width) * height;
                }

                scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        Math.round((float) newWidth),
                        Math.round((float) newHeight), true);
                bitmap.recycle();
                bitmap = scaledBitmap;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }
            System.gc();
        } else {
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }

        return rotateImage(bitmap, imageFile);
    }

    public static Bitmap rotateImage(final Bitmap bitmap,
                                     final File fileWithExifInfo) {
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = bitmap;
        int orientation = 0;
        try {
            orientation = getImageOrientation(fileWithExifInfo
                    .getAbsolutePath());
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static int getImageOrientation(final String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        int orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return 0;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static Typeface Appttf(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "AE100132.TTF");

    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String getTimezone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat date = new SimpleDateFormat("ZZZZZ", Locale.getDefault());
        String localTime = date.format(currentLocalTime);
        System.out.println(localTime + "  TimeZone   ");
        return localTime;
    }


    // remove for preferences

    public static void removepreference(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        settings.edit().remove(name).commit();
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static boolean checkNumber(String str) {

        String regex = "[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        if (pattern.matcher(str).matches()) {
            return true;
        }
        return false;
    }


    /* public static void ShowAlertDialog(String msg)
     {
         AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
         builder.setTitle("SwipeMe");
         builder.setMessage(msg);
         builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 dialog.dismiss();
             }
         });

 // Create the AlertDialog
         AlertDialog dialog = builder.create();
         dialog.show();

     }
 */
    public static boolean validateFirstName(String firstName) {
        return firstName.matches("[A-Z][a-zA-Z]*");
    } // end

    /**
     * Function to show settings alert dialog
     */
    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

//    public static void showExit(final Context mContext, final Activity activity, final String msg) {
//        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
//        // Setting Dialog Title
//        alertDialog.setTitle("FlyerPlane");
//
//        // Setting Dialog Message
//        if (msg.equals("1")) {
//
//            alertDialog.setMessage(mContext.getResources().getString(R.string.msg_exit));
//        } else {
//            alertDialog.setMessage(mContext.getResources().getString(R.string.msg_logut));
//
//        }
//        // Setting Icon to Dialog
//        //  alertDialog.setIcon(R.drawable.);
//        alertDialog.setCancelable(false);
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (msg.equals("1")) {
//                    activity.finish();
//                } else {
//
//                    removekey(mContext, activity);
//                }
//
//            }
//        });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }


    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static void setSystemLocaleLegacy(Configuration config, Locale locale) {
        config.locale = locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale) {
        config.setLocale(locale);
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    public static String getKeyboardLang(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodSubtype inputMethodSubtype = inputMethodManager.getCurrentInputMethodSubtype();
        Locale mLocale = new Locale(inputMethodSubtype.getLocale());
        String localeDisplayName = mLocale.getDisplayName();
        //If the device Locale is English
        String sdf = mLocale.getDisplayLanguage();
        String fdsf = mLocale.getISO3Language();
        String dsfdsffsdfs = mLocale.getLanguage();
        if (Locale.getDefault().getISO3Language().equals("eng")) {
            //this is how those languages displayed on English
            System.out.println("s00000dfsdf");
        } else if (localeDisplayName.equals("Arabic")) {
            System.out.println("sdfsdf");
        }


//        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        InputMethodSubtype inputMethodSubtype = inputMethodManager.getCurrentInputMethodSubtype();
//        String lang= inputMethodSubtype.getLocale();
//
//        InputMethodManager imm = (InputMethodManager)
//               context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        List<InputMethodInfo> ims = imm.getEnabledInputMethodList();
//
//        for (InputMethodInfo method : ims){
//            List<InputMethodSubtype> submethods =
//                    imm.getEnabledInputMethodSubtypeList(method, true);
//            for (InputMethodSubtype submethod : submethods){
//                if (submethod.getMode().equals("keyboard")){
//                    String currentLocale = submethod.getLocale();
//                    Log.i("Tag", "Available input method locale: " + currentLocale);
//                    return  currentLocale;
//
//                }
//            }
//        }
        return "";
    }


    public static String readContacts(Context appContext) {

        String phoneNumber = null;
        String email = null;
        String number = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = appContext.getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    int i = 0;

                    System.out.println("total number " + phoneNumber);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                        System.out.println(output);


                        number = phoneNumber + ",";


                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
//                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
//
//                    while (emailCursor.moveToNext()) {
//
//                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
//
//                        output.append("\nEmail:" + email);
//
//                    }
//
//                    emailCursor.close();
                }

                output.append("\n");
            }

            //  outputText.setText(output);
        }
        return number;
    }


    public static void exportDB() {
        // TODO Auto-generated method stub

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "com.ebabu.flyerplane"
                        + "//databases//" + "FlyerplaneDatabase";
                String backupDBPath = "/FlyerplaneDatabase";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (!backupDB.exists()) {
                    backupDB.createNewFile();
                }
                Log.d("DBPath", "backupDB: " + backupDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                System.out.println("the data table is " + backupDB.toString());

            }
        } catch (Exception e) {

            System.out.println("exp " + e.toString());
//            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
//                    .show();

        }
    }


    File flyerFolder;

    public String makeFile() {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String path = "Consultlot/Remark";
                flyerFolder = new File(sd, path);

                if (!flyerFolder.exists()) {
                    flyerFolder.mkdirs();
                    flyerFolder.getAbsolutePath();
                    System.out.println("fly path " + flyerFolder.getAbsolutePath());


                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flyerFolder.getAbsolutePath();
    }


//    public static String getMediaFile() {
//
//        try {
//            File flyerFolder = null;
//            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//            if (sd.canWrite()) {
//                String path = "Sportot/Media";
//                flyerFolder = new File(sd, path);
//                if (!flyerFolder.exists()) {
//                    flyerFolder.mkdirs();
//                    flyerFolder.getAbsolutePath();
//                    System.out.println("fly path " + flyerFolder.getAbsolutePath());
//                }
//
//            }
//            return flyerFolder.getAbsolutePath();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public static String getAudioFile() {

        try {
            File ConsultlotFolder = null;
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String path = "Consultlot/Media/ConsultlotImages/media";
                ConsultlotFolder = new File(sd, path);
                if (!ConsultlotFolder.exists()) {
                    ConsultlotFolder.mkdirs();
                    ConsultlotFolder.getAbsolutePath();
                    System.out.println("Consultlot path " + ConsultlotFolder.getAbsolutePath());
                }
            }
            return ConsultlotFolder.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getFile() {

        try {
            File flyerFolder = null;
            File myFile = null;
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String path = "Flyerplane/Media/FlyerplaneImages/sent";
                flyerFolder = new File(sd, path);
                if (!flyerFolder.exists()) {

                    //flyerFolder.mkdirs();
                    //flyerFolder.getParentFile().mkdirs();
                    // flyerFolder.createNewFile();
                    System.out.println("flyerfolder path" + flyerFolder.getAbsolutePath());
                    return flyerFolder;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void showInternetPop(Context context) {

//        MyDialog.iPhone(context.getResources().getString(R.string.connection), context);

    }

    public static void Log(String msg) {
        Log.d("TAG", msg);
    }


    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public static boolean checkLink(String str) {
        if (str.contains("facebook") || str.contains("google") || str.contains("youtube") || str.contains("twitter") || str.contains("plus") || str.contains("linkedin")) {
            return true;
        }
        return false;

    }

    private static final Pattern WEB_URL_PATTERN = Patterns.WEB_URL;

    public static boolean isValidURL(String urlString) {
        return WEB_URL_PATTERN.matcher(urlString).matches();

    }

    public static void callWeblink(String url, Context context) {
        if (Utility.isConnectingToInternet(context)) {
            if (Utility.isValidURL(url)) {
                if (!url.startsWith("https://")) {
                    url = "https://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        } else {

            com.app.session.customview.MyDialog.iPhone(context.getResources().getString(R.string.connection), context);
        }
    }


    public static String getDob(String dob) {
        String date_birth = "";
        try {

            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd,MMM,yyyy");
            Date date = format1.parse(dob);
            System.out.println(format2.format(date));
            date_birth = format2.format(date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_birth;

    }

    public static String hasNewLine(String str) {
        try {
            boolean hasNewline = str.contains("\\n");
            if (hasNewline) {
                System.out.println("msg have newline");
                str = str.trim().replace("\\n", " \n ");
                return str.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.trim();
    }

    public static void clearDataBase(Context context) {

        DataPrefrence.setPref(context, Constant.USER_ID, "");
        DataPrefrence.setPref(context, Constant.EMAILID, "");
        DataPrefrence.setPref(context, Constant.USER_NAME, "");
        DataPrefrence.setPref(context, Constant.MOBILE_NO, "");
        DataPrefrence.setPref(context, Constant.FULLNAME, "");
        DataPrefrence.setPref(context, Constant.COUNTRY_ID, "");
        DataPrefrence.setPref(context, Constant.ACCESS_TOKEN, "");
        DataPrefrence.setPref(context, Constant.LOGIN_FLAG, false);
        DataPrefrence.setPref(context, Constant.LOGIN_TYPE, "");
        DataPrefrence.setPref(context, Constant.IS_CONSULTANT, "");
        DataPrefrence.setPref(context, Constant.IS_COMPANY, "");
        DataPrefrence.setPref(context, Constant.LANGUAGE_SELECTED, false);
        DataPrefrence.setPref(context, Constant.CATEGORY_SELECTED, false);

    }

    public static void unAuthorized(Context context, Activity activity) {
        String msg = context.getResources().getString(R.string.unauthorized_error);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        clearDataBase(context);

        Intent intent = new Intent(context, SplashActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static String getTime() {

        TimeZone tz = TimeZone.getDefault();
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.println("timeStamp " + timeStamp);
        return timeStamp;
    }
    public static String getDays(String date_formate) {
        String[] dates = date_formate.split("T");
        date_formate = dates[0];


        String change_date = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
        try {
            Date date = formatter.parse(date_formate);
            change_date = formatter2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return change_date;

    }
    public static String change_DateFormate(String date_formate) {
        String change_date = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DateFormat formatter2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
        try {
            Date date = formatter.parse(date_formate);
            change_date = formatter2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return change_date;

    }

    public static String getLocalTime() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat date = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String localTime = date.format(currentLocalTime);
        System.out.println(localTime + "  TimeZone   ");
        return localTime;
    }


     public static String getTime1() {
        TimeZone tz = TimeZone.getDefault();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String timeStamp1 = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());

        String stmap=timeStamp+"T"+timeStamp1+"Z";
         System.out.println("timeStamp " + stmap);
        return stmap;
    }

    public static String getTimeSlot(String time) {
        String timeSlot="";
        try {


            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault());

            //  final DateFormat formatter = DateFormat.getDateTimeInstance();
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            final Date timezone = formatter.parse(time);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            System.out.println(formatter.format(timezone));
            time= formatter.format(timezone);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
            Date date = formatter.parse(time);
            System.out.println(format2.format(date));
            timeSlot = format2.format(date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeSlot;


    }

    public static String getChatTime(String time)
    {
        String timeSlot="";
        try {

            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault());

          //  final DateFormat formatter = DateFormat.getDateTimeInstance();
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            final Date timezone = formatter.parse(time);
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            System.out.println(formatter.format(timezone));
            time= formatter.format(timezone);
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
            Date date = formatter.parse(time);
            System.out.println(format2.format(date));
            timeSlot = format2.format(date).toString();
        } catch (ParseException e) {
            e.printStackTrace();
       }
        return timeSlot;
    }



    public static String getTimeOnly() {

        Date date = new Date();
        //This method returns the time in millis
        long timeMilli = date.getTime();

        System.out.println("timeStamp " + timeMilli);
        return "" + timeMilli;

    }


    public static String getTimeStamp()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       return ""+timestamp.getTime();
    }



    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public static long getVideoSize(String path) {


        try {
            long MiB = 1024 * 1024;
            long KiB = 1024;
            File file = new File(path);
            long length = file.length();
            length = length / MiB;
            System.out.println("File Path : " + file.getPath() + ", File size : " + length + " MB");
            return length;
        } catch (Exception e) {
            System.out.println("File not found : " + e.getMessage() + e);
            e.printStackTrace();
        }
        return 0;

    }

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
//        String url = "http://i3.ytimg.com/vi/" + extractYTId(videoUrl) + "/0.jpg";
        String url = "https://i3.ytimg.com/vi/" + extractYTId(videoUrl) + "/mqdefault.jpg";
        return url;
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        System.out.println("youtubeid"+vId);
        return vId;
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file=url;
//        Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
//        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



    public static String getStringFromList(LinkedList<UserStory> list) {
        Gson gson = new Gson();

        String inputString = gson.toJson(list);

        System.out.println("inputString= " + inputString);
        return inputString;
    }

    public static LinkedList<UserStory> getStoryList(String json) {
        LinkedList<UserStory> list = new LinkedList<>();

        Gson gson = new Gson();

        list = gson.fromJson(json,
                new TypeToken<LinkedList<UserStory>>() {
                }.getType());
        return list;
    }


    public static File getFileByBitmap(Context context, Bitmap bitmap, String name) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        System.out.println("file path"+imageFile.getAbsolutePath());
        return imageFile;


    }

    public static String getJsonToStringFromList(ArrayList<UserStory> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    public static ArrayList<UserStory> getListFromJson(String json) {
        Gson gson = new Gson();
        ArrayList<UserStory> data = gson.fromJson(json,
                new TypeToken<ArrayList<UserStory>>() {
                }.getType());
        return data;
    }


    public static void sharePost(Bitmap bitmap, StoryModel storyData, Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("image/*");

//        String msg =postType +"\n" + postInformation + "\n" + "This post is published on GoFound app!" + "\n" + "For more information,get GoFound app on Android for free!" + "\n" +
//                "Google play:" + "\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n" +
//                "App Store:" + "\n" ;
        String msg = "";
        if (storyData.getStoryType().equals("image")) {
            msg = storyData.getStoryTitle() + "\n" + storyData.getStoryText();
        } else {
            msg = storyData.getStoryUrl() + "\n" + storyData.getStoryTitle() + "\n" + storyData.getStoryText();
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, msg);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(context, bitmap));
        context.startActivity(Intent.createChooser(shareIntent, "Share Story"));
    }

    public static void shareVideoUrl(StoryModel storyData, Context context) {

        String msg = storyData.getStoryTitle() + "\n" + storyData.getStoryText();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//
//     i.setType("*/*");
//
//    String[] mimetypes = {"image/*", "video/*","text/plain"};
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, msg);
        if (storyData.getStoryType().equals("video"))
        {
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg + "\n" + Urls.BASE_IMAGES_URL +storyData.getStoryUrl());
        }
        else {
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg + "\n" + storyData.getStoryUrl());
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Story"));


    }

public static long getAudioDuration(Context context,String path )
{
    Uri uri = Uri.parse(path);
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    mmr.setDataSource(context,uri);
    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    long millSecond = Integer.parseInt(durationStr);
    return  millSecond;
}



    public static Uri getLocalBitmapUri(Context context, Bitmap bmp) {
        Uri bmpUri = null;

        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, "post" + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing bitmap", e);
        }
//        FileProvider.getUriForFile(Objects.requireNonNull(getActivity().getApplicationContext()),
//                BuildConfig.APPLICATION_ID + ".provider", imageFile);
        bmpUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", imageFile);
//            bmpUri = Uri.fromFile(imageFile);

        return bmpUri;
    }


    public static void displayDocument(Context context,Uri uri)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        System.out.println("mime type "+type);
        if(type.contains("doc"))
        {
            type="msword";
        }
        else if(type.equals("xlsx"))
        {
            type="vnd.ms-excel";
        }

        intent.setDataAndType(uri, "application/" + type);
        // FLAG_GRANT_READ_URI_PERMISSION is needed on API 24+ so the activity opening the file can read it
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            // Show an error
        } else {
            context.startActivity(intent);
        }
    }

public static String getRealPathFromUri(Context context,final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                String fileName = getFilePath(context, uri);
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                }

                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {}
                }

//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private static String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}// final class ends here

