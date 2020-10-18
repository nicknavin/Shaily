package com.app.session.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by hp on 08/03/2016.
 */
public class PermissionsUtils {

    private Context context;
    private static PermissionsUtils permissionsUtils = null;
    public static final int SDK_INT_MARSHMALLOW = 23;
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;


    private PermissionsUtils(Context context) {
        this.context = context;
    }

    public static PermissionsUtils getInstance(Context context) {
        if (permissionsUtils == null) {
            permissionsUtils = new PermissionsUtils(context);
        }
        return permissionsUtils;
    }

    public boolean requiredPermissionsGranted(final Context context) {
        if (Build.VERSION.SDK_INT >= SDK_INT_MARSHMALLOW)
        {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.INTERNET)) {
                permissionsNeeded.add("Internet");
            }
            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsNeeded.add("Location");
            }

            if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
                permissionsNeeded.add("Read Phone State");
            }

            if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS)) {
                permissionsNeeded.add("Receive SMS");
            }
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Write External Storage");
            }
            if(!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                permissionsNeeded.add("Read External Storage");
            }
            if(!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            {
                permissionsNeeded.add("Record Audio");
            }
            if(!addPermission(permissionsList, Manifest.permission.MODIFY_AUDIO_SETTINGS))
            {
                permissionsNeeded.add("Modify Audio Settings");
            }
            if(!addPermission(permissionsList, Manifest.permission.READ_SMS))
            {
                permissionsNeeded.add("SMS READ");
            }
            if(!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            {
                permissionsNeeded.add("Receive SMS");
            }


            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++) {
                        message = message + ", " + permissionsNeeded.get(i);
                    }
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
                ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }

        }
        return true;
    }


    public boolean isPermissionGranted(final Context context, String permission, String text) {
        if (Build.VERSION.SDK_INT >= SDK_INT_MARSHMALLOW)
        {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, permission)) {
                permissionsNeeded.add(text);
            }

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++) {
                        message = message + ", " + permissionsNeeded.get(i);
                    }
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
                ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }

        }
        return true;
    }

    private void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!((Activity) context).shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    public void setRequestedPermissions(List<String> permissionsNeeded) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String permission : info.requestedPermissions) {
                    permissionsNeeded.add(permission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean areAllPermissionsGranted(int[] grantResults)
    {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void checkIfLocationGranted(final Context context) {
        new AlertDialog.Builder(context)
                .setMessage("You need to grant Location permissions to verify your locality.")
                .setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
                            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.RECEIVE_SMS, "Receive SMS")) {
                                return;
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }








    public boolean askForPermission(Activity activity, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
return false;
            } else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }




    public boolean isPermissionGranted(final Context context, final Fragment fragment , String permission, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, permission,fragment,context)) {
                permissionsNeeded.add(text);
            }

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++) {
                        message = message + ", " + permissionsNeeded.get(i);
                    }
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    fragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                                }
                            });

                    return false;
                }
//                fragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                ActivityCompat.requestPermissions((Activity)context, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                fragment.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

                return false;
            }

        }
        return true;
    }
    private boolean addPermission(List<String> permissionsList, String permission, Fragment fragment, Context context) {
        if (fragment != null && ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!(fragment.shouldShowRequestPermissionRationale(permission)))
                return false;
        }
        return true;
    }



    public boolean requiredPermissionsGrantedComman(final Context context) {
        if (Build.VERSION.SDK_INT >= SDK_INT_MARSHMALLOW)
        {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
//            if (!addPermission(permissionsList, Manifest.permission.INTERNET)) {
//                permissionsNeeded.add("Internet");
//            }
//            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                permissionsNeeded.add("Location");
//            }

//            if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
//                permissionsNeeded.add("Read Phone State");
//            }
//
//            if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS)) {
//                permissionsNeeded.add("Receive SMS");
//            }
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Write External Storage");
            }
            if(!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                permissionsNeeded.add("Read External Storage");
            }
            if(!addPermission(permissionsList, Manifest.permission.CAMERA))
            {
                permissionsNeeded.add("Camera");
            }



            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++) {
                        message = message + ", " + permissionsNeeded.get(i);
                    }
                    showMessageOKCancel(context, message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return false;
                }
                ((Activity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }

        }
        return true;
    }

}
