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
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.LodingBean;
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 密码修改
 */

public class ModifyPwdRequest {
    private Context context;
    private String newPwd;
    private String pwd;
    private Handler handler;
    private DialogView dialogView;
    SharedPreferencesDB sharedPreferencesDB;

    public ModifyPwdRequest(SharedPreferencesDB sharedPreferencesDB, Context context, String newPwd, String pwd, Handler handler) {
        this.context = context;
        this.newPwd = newPwd;
        this.pwd = pwd;
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.handler = handler;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_MODIFYPWD);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString("phone", ""));
        params.addBodyParameter("newPwd", MD5.MD5(newPwd));
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString("userDeviceUuid", ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString("token", ""));
        params.addBodyParameter("pwd", MD5.MD5(pwd));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<ModifyNameData> result = new JsonResult<ModifyNameData>();
                try {
                    Log.i("ModifyPwdRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<ModifyNameData>() {
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
