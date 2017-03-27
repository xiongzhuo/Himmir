package com.himmiractivity.request;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.MD5;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.LodingBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ResetPwdRequest {
    private Context context;
    private String mobile;
    private String newPwd;
    private String userDeviceUuid;
    private String code;
    private Handler handler;
    private DialogView dialogView;

    public ResetPwdRequest(Context context, String mobile, String newPwd, String userDeviceUuid, String code, Handler handler) {
        this.context = context;
        this.mobile = mobile;
        this.newPwd = newPwd;
        this.userDeviceUuid = userDeviceUuid;
        this.code = code;
        this.handler = handler;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_RESETPWD);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("newPwd", MD5.MD5(newPwd));
        params.addBodyParameter("userDeviceUuid", userDeviceUuid);
        params.addBodyParameter("type", "reset");
        params.addBodyParameter("code", code);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<LodingBean> result = new JsonResult<LodingBean>();
                try {
                    Log.i("ResetPwdRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<LodingBean>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
//                        handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
                    }
                    //手机号码正常
                    else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_RECEIVED_CODE, result.getData()));
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
