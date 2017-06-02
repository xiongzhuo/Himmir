package com.himmiractivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.base.BaseBusNoSocllowActivity;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.LodingRequest;

import activity.hamir.com.himmir.R;

/**
 * 程序启动页
 */
public class SplashActivity extends BaseBusNoSocllowActivity implements OnClickListener {
    private SharedPreferencesDB sharedDB;
    int time = 1000;    //设置等待时间，单位为毫秒
    //    private MyHandler handler;
    UserData userData;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }
//
//    class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                //成功
//                case StatisConstans.MSG_RECEIVED_REGULAR:
//                    userData = (UserData) msg.obj;
//                    Intent intent = new Intent();
//                    intent.setClass(SplashActivity.this, MainActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(StatisConstans.USERDATA, userData);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    finish();
//                    break;
//                case StatisConstans.MSG_RECEIVED_BOUND:
//                    startActivity(new Intent(SplashActivity.this, LodingActivity.class));
//                    finish();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onMultiClick(View v) {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//        handler = new MyHandler();
        sharedDB = SharedPreferencesDB.getInstance(this);
        //当计时结束时，跳转至主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LodingRequest();
            }
        }, time);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
        super.onBackPressed();
    }

    public void LodingRequest() {
        if (TextUtils.isEmpty(sharedDB.getString(StatisConstans.USERPWD, "")) || TextUtils.isEmpty(sharedDB.getString(StatisConstans.USERNAME, ""))) {
            startActivity(new Intent(this, LodingActivity.class));
            finish();
        } else {
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(StatisConstans.USERDATA, userData);
//            intent.putExtras(bundle);
            startActivity(intent);
            finish();
//            LodingRequest lodingRequest = new LodingRequest(sharedDB, this, sharedDB.getString(StatisConstans.USERPWD, ""), sharedDB.getString(StatisConstans.USERNAME, ""), handler, false);
//            try {
//                lodingRequest.requestCode();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

}
