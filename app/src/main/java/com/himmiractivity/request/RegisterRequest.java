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

public class RegisterRequest {
    private Context context;
    private String pwd;
    private String mobile;
    private String userDeviceUuid;
    private String code;
    private Handler handler;
    private DialogView dialogView;

    public RegisterRequest(Context context, String pwd, String mobile, String userDeviceUuid, String code, Handler handler) {
        this.pwd = pwd;
        this.mobile = mobile;
        this.userDeviceUuid = userDeviceUuid;
        this.code = code;
        this.context = context;
        this.handler = handler;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_REGISTER);
        params.addBodyParameter("pwd", MD5.MD5(pwd));
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("userDeviceUuid", userDeviceUuid);
        params.addBodyParameter("code", code);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
                JsonResult<LodingBean> result = new JsonResult<LodingBean>();
                try {
                    Log.i("RegisterRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<LodingBean>() {
                            }.getType());
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
//                        handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
                    } else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_RECEIVED_CODE, result.getData()));
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
