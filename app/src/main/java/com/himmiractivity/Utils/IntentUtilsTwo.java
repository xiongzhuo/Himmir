package com.himmiractivity.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

public class IntentUtilsTwo {

    public static void intentToSMS(Activity context, String number, String body) {
        if (context == null) {
            return;
        }
        Uri smsToUri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", body);
        context.startActivity(intent);
    }


    public static void intentToCall(Context context, String phone) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }


}
