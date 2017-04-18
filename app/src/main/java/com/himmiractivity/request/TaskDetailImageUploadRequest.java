package com.himmiractivity.request;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.ImageUtil;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.MD5;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.entity.LodingBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TaskDetailImageUploadRequest {
    private SharedPreferencesDB sharedPreferencesDB;
    private Context context;
    private Handler handler;
    private List<File> files;
    DialogView dialogView;

    public TaskDetailImageUploadRequest(SharedPreferencesDB sharedPreferencesDB, Context context, Handler handler, List<File> files) {
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.context = context;
        this.handler = handler;
        this.files = files;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        RequestParams params = new RequestParams(Configuration.URL_GETIMAGEUPLOADS);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
//        for (File file : files) {
//            params.addBodyParameter("image", file);
//        }
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                JsonResult<String> result = new JsonResult<String>();
                try {
                    if (null != dialogView) {
                        dialogView.cancel();
                    }
                    Log.i("ResetPwdRequest", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<String>() {
                            }.getType());
                    //手机号码已经被其它账号绑定
                    if (!result.isFlag()) {
                        ToastUtil.show(context, result.getMsg());
//                        handler.sendEmptyMessage(StatisConstans.MSG_RECEIVED_BOUND);
                    }
                    //手机号码正常
                    else {
                        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_IMAGE_SUCCES, result.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
                ToastUtil.show(context, "服务器异常");
                Log.i("ResetPwdRequest", "onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("ResetPwdRequest", "onCancelled");
            }

            @Override
            public void onFinished() {
                Log.i("ResetPwdRequest", "onFinished");
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
}
