package com.himmiractivity.Utils;

import android.content.Context;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by Administrator on 2017/3/10.
 */

public class GpsUtils {
    //获取是否已打开自身GPS
    public static boolean isGpsEnable(Context mContext) {
        String providers = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }
}
