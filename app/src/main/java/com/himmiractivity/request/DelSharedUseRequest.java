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
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 删除指定共享用户BYUserKey
 */

public class DelSharedUseRequest {
    private Context context;
    private Handler handler;
    private DialogView dialogView;
    private String userKeyvalue;
    private String shareUserKey;
    SharedPreferencesDB sharedPreferencesDB;

    public DelSharedUseRequest(SharedPreferencesDB sharedPreferencesDB, String userKeyvalue, String shareUserKey, Context context, Handler handler) {
        this.context = context;
        this.userKeyvalue = userKeyvalue;
        this.shareUserKey = shareUserKey;
        this.handler = handler;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_DEL_SHARE_USE);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
        params.addBodyParameter("userKeyvalue", userKeyvalue);
        params.addBodyParameter("shareUserKey", shareUserKey);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<ModifyNameData> result = new JsonResult<ModifyNameData>();
                try {
                    Log.i("ModifyNameRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<ModifyNameData>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
//                        handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
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
