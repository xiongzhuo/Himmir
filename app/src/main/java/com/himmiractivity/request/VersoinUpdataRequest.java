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
import com.himmiractivity.entity.AllUserDerviceBaen;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.VersionBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/16.
 */

public class VersoinUpdataRequest {
    private Context context;
    private Handler handler;
    SharedPreferencesDB sharedPreferencesDB;

    public VersoinUpdataRequest(SharedPreferencesDB sharedPreferencesDB, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        RequestParams params = new RequestParams(Configuration.URL_APP_VER);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<VersionBean> result = new JsonResult<VersionBean>();
                try {
                    Log.i("CodeRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<VersionBean>() {
                            }.getType());
                    if (!result.isFlag()) {
                        handler.sendEmptyMessage(StatisConstans.FAIL);
                        ToastUtil.show(context, result.getMsg());
                    } else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_QQUIP, result.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handler.sendEmptyMessage(StatisConstans.FAIL);
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
