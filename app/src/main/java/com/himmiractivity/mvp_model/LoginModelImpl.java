package com.himmiractivity.mvp_model;

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
import com.himmiractivity.request.LodingRequest;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */

public class LoginModelImpl implements LoginModel {
    @Override
    public void loadData(SharedPreferencesDB sharedPreferencesDB, String pwd, String mobile, final OnLoginDataListener listener) {
        RequestParams params = new RequestParams(Configuration.URL_LOGIN);
        params.addBodyParameter("pwd", MD5.MD5(pwd));
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<UserData> result = new JsonResult<UserData>();
                try {
                    Log.i("首页返回", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<UserData>() {
                            }.getType());
                    //success为false
                    if (!result.isFlag()) {
                        listener.onToast(result.getMsg());
                    }
                    //success为true
                    else {
                        listener.onSuccess(result.getData());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //请求失败
                listener.onToast("网络请求失败");
                Log.i("logTest_xz", "onError");
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

    public interface OnLoginDataListener {
        void onSuccess(UserData userData);

        void onToast(String string);
    }
}
