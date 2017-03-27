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
import com.himmiractivity.activity.SetActivity;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.ModifySuccessView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 用户昵称修改
 */

public class ModifyNameRequest {
    private Context context;
    private String newName;
    private Handler handler;
    ModifySuccessView modifySuccessView;
    SharedPreferencesDB sharedPreferencesDB;

    public ModifyNameRequest(SharedPreferencesDB sharedPreferencesDB, Context context, String newName, Handler handler) {
        this.context = context;
        this.newName = newName;
        this.handler = handler;
        this.sharedPreferencesDB = sharedPreferencesDB;
    }

    public void requestCode() throws Exception {
        RequestParams params = new RequestParams(Configuration.URL_MODIFYNAME);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString("phone", ""));
        params.addBodyParameter("newName", newName);
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString("userDeviceUuid", ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString("token", ""));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<ModifyNameData> result = new JsonResult<ModifyNameData>();
                try {
                    if (modifySuccessView == null) {
                        modifySuccessView = new ModifySuccessView(context);
                        modifySuccessView.show();
                        modifySuccessView.setMessage("修改成功");
                    }
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
                ToastUtil.show(context, "请求失败");
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
