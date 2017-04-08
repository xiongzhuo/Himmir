package com.himmiractivity.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Administrator on 2017/4/8.
 */

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private static MyCrashHandler myCrashHandler;
    private Context context;

    private MyCrashHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
//                    Intent intent = new Intent(context, ExceptionActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                    ToastUtil.show(context, "系统崩溃");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

            }
        }).start();

    }

    private MyCrashHandler() {
    }

    Handler handler;

    public static MyCrashHandler getInstance(Handler handler) {
        synchronized (MyCrashHandler.class) {
            if (myCrashHandler != null) {
                return myCrashHandler;
            } else {
                myCrashHandler = new MyCrashHandler(handler);
                return myCrashHandler;
            }
        }

    }

    public void init(Context context) {
        this.context = context;
    }
}
