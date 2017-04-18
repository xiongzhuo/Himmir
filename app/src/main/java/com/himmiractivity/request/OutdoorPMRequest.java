package com.himmiractivity.request;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.PhoneCode;
import com.himmiractivity.entity.PmBean;
import com.himmiractivity.interfaces.StatisConstans;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 短信验证吗获取
 */

public class OutdoorPMRequest {

    private SharedPreferencesDB sharedPreferencesDB;
    private String cityName;
    private Context context;
    private Handler handler;

    public OutdoorPMRequest(Context context, SharedPreferencesDB sharedPreferencesDB, String cityName, Handler handler) {
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.cityName = cityName;
        this.context = context;
        this.handler = handler;
    }

    public void requestCode() throws Exception {
        RequestParams params = new RequestParams(Configuration.URL_OUTDOOR_PM);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString("phone", ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString("token", ""));
        params.addBodyParameter("cityName", cityName);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<PmBean> result = new JsonResult<PmBean>();
                try {
                    Log.i("URL_OUTDOOR_PM", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<PmBean>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
                    }
                    //手机号码正常
                    else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_OUTDOOR_PM, result.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(context, "网络请求失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
