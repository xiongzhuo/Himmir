package com.himmiractivity.request;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.MD5;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.activity.LodingActivity;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.HomeDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/15.
 */

public class LodingRequest {
    private Context context;
    private String pwd;
    private String mobile;
    private Handler handler;
    private HomeDialog.DialogView dialogView;
    SharedPreferencesDB sharedPreferencesDB;
    boolean isDialog;

    public LodingRequest(SharedPreferencesDB sharedPreferencesDB, Context context, String pwd, String mobile, Handler handler, boolean isDialog) {
        this.context = context;
        this.pwd = pwd;
        this.isDialog = isDialog;
        this.mobile = mobile;
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.handler = handler;
    }

    public void requestCode() throws Exception {
        if (isDialog) {
            if (null == dialogView) {
                dialogView = new HomeDialog.DialogView(context);
                dialogView.show();
                dialogView.setMessage("登陆中");
            }
        }
        RequestParams params = new RequestParams(Configuration.URL_LOGIN);
        params.addBodyParameter("pwd", MD5.MD5(pwd));
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<UserData> result = new JsonResult<UserData>();
                try {
                    if (null != dialogView) {
                        dialogView.cancel();
                    }
                    Log.i("首页返回", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<UserData>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
                    }
                    //手机号码正常
                    else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_RECEIVED_REGULAR, result.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(context, "网络请求失败");
                Log.i("logTest_xz", "onError");
                if (null != dialogView) {
                    dialogView.cancel();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("logTest_xz", "onCancelled");
            }

            @Override
            public void onFinished() {
                Log.i("logTest_xz", "onFinished");
            }

            @Override
            public boolean onCache(String result) {
                Log.i("logTest_xz", "onCache");
                return false;
            }
        });
    }
}
