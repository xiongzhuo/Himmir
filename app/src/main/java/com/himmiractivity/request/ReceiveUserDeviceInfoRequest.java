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
import com.himmiractivity.entity.ArticleInfo;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/21.
 */

public class ReceiveUserDeviceInfoRequest {
    SharedPreferencesDB sharedPreferencesDB;
    Context context;
    Handler handler;
    String userRoom;
    String userTureName;
    String deviceMac;
    String buyProvince;
    String buyCity;
    String buyCounty;
    String buyDetailAddress;
    String installProvince;
    String installCity;
    String devNickName;
    String installCounnty;
    String installDetailAddress;
    String deviceSn;
    private DialogView dialogView;

    public ReceiveUserDeviceInfoRequest(SharedPreferencesDB sharedPreferencesDB, Context context, Handler handler, String userRoom, String userTureName, String deviceMac, String deviceSn, String devNickName) {
        this.sharedPreferencesDB = sharedPreferencesDB;
        this.context = context;
        this.handler = handler;
        this.userRoom = userRoom;
        this.userTureName = userTureName;
        this.deviceMac = deviceMac;
        this.devNickName = devNickName;
        this.deviceSn = deviceSn;
    }

    public void setBuyAddress(String buyProvince, String buyCity, String buyCounty, String buyDetailAddress) {
        this.buyProvince = buyProvince;
        this.buyCity = buyCity;
        this.buyCounty = buyCounty;
        this.buyDetailAddress = buyDetailAddress;
    }

    public void setinstallAddress(String installProvince, String installCity, String installCounnty, String installDetailAddress) {
        this.installProvince = installProvince;
        this.installCity = installCity;
        this.installCounnty = installCounnty;
        this.installDetailAddress = installDetailAddress;
    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        Log.d("dataServer", userRoom + ":" + userTureName + ":" + deviceMac + ":" + buyProvince + ":" + buyProvince + ":" + buyCity);
        RequestParams params = new RequestParams(Configuration.URL_RECEIVEUSERDEVICE);
        params.addBodyParameter("mobile", sharedPreferencesDB.getString(StatisConstans.PHONE, ""));
        params.addBodyParameter("userDeviceUuid", sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""));
        params.addBodyParameter("userToken", sharedPreferencesDB.getString(StatisConstans.TOKEN, ""));
        params.addBodyParameter("userKey", sharedPreferencesDB.getString(StatisConstans.KEY, ""));
        params.addBodyParameter("userRoom", userRoom);
        params.addBodyParameter("userTureName", userTureName);
        params.addBodyParameter("deviceMac", deviceMac);
        params.addBodyParameter("buyProvince", buyProvince);
        params.addBodyParameter("buyCity", buyCity);
        params.addBodyParameter("buyCounty", buyCounty);
        params.addBodyParameter("buyDetailAddress", buyDetailAddress);
        params.addBodyParameter("installProvince", installProvince);
        params.addBodyParameter("installCity", installCity);
        params.addBodyParameter("installCounnty", installCounnty);
        params.addBodyParameter("installDetailAddress", installDetailAddress);
        params.addBodyParameter("deviceSn", deviceSn);
        params.addBodyParameter("devNickName", devNickName);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String json) {
                if (null != dialogView) {
                    dialogView.cancel();
                }
                JsonResult<ImageBean> result = new JsonResult<ImageBean>();
                try {
                    Log.i("Receivsss", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    result = JsonUtils.parseJson(json,
                            new TypeToken<ImageBean>() {
                            }.getType());
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
