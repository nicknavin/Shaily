package com.app.session.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;

import com.app.session.utility.Constant;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class TextMessageReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        Log.d("TextMessageReceiver", "onReceive()");
        Object[] messages = (Object[]) bundle.get("pdus");
        SmsMessage[] sms = new SmsMessage[messages.length];

        for (int n = 0; n < messages.length; n++) {
            sms[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
        }

        for (SmsMessage msg : sms) {
//            String msgs=msg.getMessageBody();
//            String st= msg.getMessageBody().substring(7,msgs.length()-1);
//
//            if (st.startsWith("is your one time")) {
//
//                Log.d("message", msg.getMessageBody());
////                if (Utility.getSharedPreferencesBoolean(appContext, Constant.IS_VERIFIEDFACEBOOK)) {
////                    VerifiedFacebook.updateMessageBox(msg.getMessageBody());
////                    Utility.setSharedPreferenceBoolean(appContext,Constant.IS_VERIFIEDFACEBOOK,false);
////                } else
////                if(Utility.getSharedPreferencesBoolean(appContext, Constant.IS_VERIFIEDREGISTER)) {
////                    Register_Activity_three.updateMessageBox(msg.getMessageBody());
////                    Utility.setSharedPreferenceBoolean(appContext,Constant.IS_VERIFIEDREGISTER,false);
////                }
//
//                VerificationCodeActivity.updateMessageBox(msg.getMessageBody());
//
//
//            }
            //452122 is your one time password(OTP) for phone verification.
            if (msg.getMessageBody().contains("is your one time password(OTP) for phone verification.")) {

                Log.d("message", msg.getMessageBody());
//                if (Utility.getSharedPreferencesBoolean(appContext, Constant.IS_VERIFIEDFACEBOOK)) {
//                    VerifiedFacebook.updateMessageBox(msg.getMessageBody());
//                    Utility.setSharedPreferenceBoolean(appContext,Constant.IS_VERIFIEDFACEBOOK,false);
//                } else
//                if(Utility.getSharedPreferencesBoolean(appContext, Constant.IS_VERIFIEDREGISTER)) {
//                    Register_Activity_three.updateMessageBox(msg.getMessageBody());
//                    Utility.setSharedPreferenceBoolean(appContext,Constant.IS_VERIFIEDREGISTER,false);
//                }


//                VerificationCodeActivity.updateMessageBox(msg.getMessageBody());
                Intent intent2 = new Intent(Constant.CUSTOME_BROADCAST);
                intent2.putExtra("MSG",msg.getMessageBody());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

            }
        }
    }
}
