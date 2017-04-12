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
import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DataServerConfigRequest {
    SharedPreferencesDB sharedPreferencesDB;
    Handler handler;
    Context context;
    DialogView dialogView;

    public DataServerConfigRequest(SharedPreferencesDB sharedPreferencesDB, Handler handler, Context context) {
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.handler = handler;
        this.context = context;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_GETDATASERVERCONFIG);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString("phone", ""));
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString("userDeviceUuid", ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString("token", ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<DataServerBean> result = new JsonResult<DataServerBean>();
                try {
                    Log.i("DataCodeRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<DataServerBean>() {
                            }.getType());
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
                    } else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.CONFIG_REGULAR, result.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtil.show(context, "请求失败");
                if (null != dialogView) {
                    dialogView.cancel();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
            }

            @Override
            public void onFinished() {
                if (null != dialogView) {
                    dialogView.cancel();
                }
            }

            @Override
            public boolean onCache(String result) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
                return false;
            }
        });
    }
}
