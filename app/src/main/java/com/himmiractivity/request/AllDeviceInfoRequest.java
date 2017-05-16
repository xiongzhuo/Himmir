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
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.HomeDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/16.
 */

public class AllDeviceInfoRequest {
    private Context context;
    private Handler handler;
    SharedPreferencesDB sharedPreferencesDB;
    private HomeDialog.DialogView dialogView;

    public AllDeviceInfoRequest(SharedPreferencesDB sharedPreferencesDB, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new HomeDialog.DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_DERVICE);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""));
        params.addBodyParameter("userKey", sharedPreferencesDB.getString(StatisConstans.KEY, ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
                JsonResult<AllUserDerviceBaen> result = new JsonResult<AllUserDerviceBaen>();
                try {
                    Log.i("CodeRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<AllUserDerviceBaen>() {
                            }.getType());
                    if (!result.isFlag()) {
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
                ToastUtil.show(context, "网络请求失败");
                if (null != dialogView) {
                    dialogView.cancel();
                }
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
