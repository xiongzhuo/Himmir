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
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ModifyRoomNameRequest {
    SharedPreferencesDB sharedPreferencesDB;
    Context context;
    Handler handler;
    String userRoomNewName;
    String userRoomId;
    private DialogView dialogView;

    public ModifyRoomNameRequest(Context context, SharedPreferencesDB sharedPreferencesDB, String userRoomNewName, String userRoomId, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.userRoomNewName = userRoomNewName;
        this.userRoomId = userRoomId;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_MODIFY_ROOM_NAME);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString("phone", ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString("token", ""));
        params.addBodyParameter("userKey", sharedPreferencesDB.getString("key", ""));
        params.addBodyParameter("userDevSn", userRoomId);
        params.addBodyParameter("userDevNickName", userRoomNewName);
        Log.d("dataServer", sharedPreferencesDB.getString("phone", "") + "===" + sharedPreferencesDB.getString("token", "") + "===" + sharedPreferencesDB.getString("key", "") + "===" + userRoomId + "===" + userRoomNewName);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<ImageBean> result = new JsonResult<ImageBean>();
                try {
                    Log.i("RegisterRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<ImageBean>() {
                            }.getType());
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
                    } else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_MODIFY_NAME, result.getData()));
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
