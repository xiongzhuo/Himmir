package com.himmiractivity.Utils;

import android.content.Context;
import android.location.LocationManager;
import android.provider.Settings;

import java.net.Socket;

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

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @param socket
     * @return
     */
    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }
}
