package com.himmiractivity.request;

import android.app.IntentService;
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
import com.himmiractivity.entity.ManagerShardBean;
import com.himmiractivity.interfaces.StatisConstans;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 已经加分享
 */

public class AddedShardRequest {
    private Context context;
    private Handler handler;
    SharedPreferencesDB sharedPreferencesDB;

    public AddedShardRequest(SharedPreferencesDB sharedPreferencesDB, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        RequestParams params = new RequestParams(Configuration.URL_GETCHARE_USERLIST);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<ManagerShardBean> result = new JsonResult<ManagerShardBean>();
                try {
                    Log.i("ModifyNameRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<ManagerShardBean>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
                        handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
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
                handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
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
