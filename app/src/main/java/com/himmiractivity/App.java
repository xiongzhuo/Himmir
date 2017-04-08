package com.himmiractivity;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.himmiractivity.Utils.MyCrashHandler;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

public class App extends Application {
    private List<Activity> activityList = new LinkedList<>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static App instance;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    //实例化一次
    public synchronized static App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        x.Ext.init(this);
        MyCrashHandler handler = MyCrashHandler.getInstance(this.handler);
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    // add Activity
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}