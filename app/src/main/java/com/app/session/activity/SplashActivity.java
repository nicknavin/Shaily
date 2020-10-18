package com.app.session.activity;

import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.app.session.R;
import com.app.session.base.BaseActivity;
import com.app.session.utility.Constant;
import com.app.session.utility.DataPrefrence;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SplashActivity extends BaseActivity {

//please see this , getuserSubscribedAllStroies,subscriptionStories in this api, give me subsciptin group name and group image
    // getSubscribedSubscriptionGroups give me user details
    //

    //572482702
    //pwd e8ax16
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        startActivity(new Intent(getApplicationContext(), AddSubscriptionStoryActivity.class));
startApp();
    }




    private void startApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                    if (DataPrefrence.getPref(context, Constant.LOGIN_FLAG, false)) {
                        Intent intent=null;
                        if (DataPrefrence.getPref(context, Constant.IS_CONSULTANT, "").equals("1"))
                        {

                            intent = new Intent(context, ConsultantStoryActivity.class);

                        }
                        startActivity(intent);

                    } else {
//                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        startActivity(new Intent(getApplicationContext(), TermsConditionPage.class));

                    }
                    finish();


                } catch (Exception e) {
                }

            }
        }).start();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void startAppold() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                    if (DataPrefrence.getPref(context, Constant.LOGIN_FLAG, false)) {
                        Intent intent=null;
                        if (DataPrefrence.getPref(context, Constant.IS_COMPANY, "").equals("1")) {
                           // intent = new Intent(context, CompanyHomeActivity.class);
                        } else {
                            intent = new Intent(context, ConsultantStoryActivity.class);

                        }
                        startActivity(intent);

                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                    finish();


                } catch (Exception e) {
                }

            }
        }).start();
    }

    private void getkay()
    {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.you.name", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
