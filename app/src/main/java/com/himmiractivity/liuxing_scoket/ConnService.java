package com.himmiractivity.liuxing_scoket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Administrator on 2017/3/30.
 */

public class ConnService extends Service implements ConnManager.ConnectionListener {

    private ConnManager mConnManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 连接服务
     */
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnManager = ConnManager.getInstance();
                mConnManager.setConnectionListener(ConnService.this);
                mConnManager.connect();
                //发送验证码
                mConnManager.sendAuth("#A");
            }
        }).start();

    }

    @Override
    public void pushData(String data) {
        Intent intent = new Intent();
        intent.setAction(PushReceiver.ACTION);
        intent.putExtra(PushReceiver.DATA, data);
        sendBroadcast(intent);
    }

}
