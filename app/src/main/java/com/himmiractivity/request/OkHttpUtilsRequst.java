package com.himmiractivity.request;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.ImageUtil;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.HomeDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/4.
 */

public class OkHttpUtilsRequst {
    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, File> filMap = new HashMap<String, File>();
    private Activity context;
    private SharedPreferencesDB sharedDB;
    private Handler handler;
    private HomeDialog.DialogView dialog;
    //    private FormFile formFile;
    private File file;
    private HomeDialog.DialogView dialogView;

    public OkHttpUtilsRequst(Activity context, File file, Handler handler) throws Exception {
        this.context = context;
        this.handler = handler;

        sharedDB = SharedPreferencesDB.getInstance(context);
//        map.put("fileType", "1");
        map.put("userToken", sharedDB.getString(StatisConstans.TOKEN, ""));
        map.put("userDeviceUuid", sharedDB.getString(StatisConstans.USERDEVICEUUID, ""));
        map.put("mobile", sharedDB.getString(StatisConstans.PHONE, ""));
        this.file = file;
//        map.put("image", getBitmapByte(bitmap).toString());
//        for (File file : files) {
        file = file;
//            formFile = new FormFile(file.getName(), file, "image", "image/jpeg");
//            filMap.put(file.getName(), file);
//    }

    }

    public void requestCode() throws Exception {
        if (null == dialogView) {
            dialogView = new HomeDialog.DialogView(context);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
        if (!file.exists()) {
            Toast.makeText(context, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.post()//
                .addFile("image", file.getName(), file)//
                .url(ImageUtil.getUrl(Configuration.URL_GETIMAGEUPLOADS, map))
//                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (null != dialogView) {
                            dialogView.cancel();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("首页放回", response);
                        if (null != dialogView) {
                            dialogView.cancel();
                        }
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }
                        JsonResult<ImageBean> result = new JsonResult<ImageBean>();
                        try {
                            result = JsonUtils.parseJson(response,
                                    new TypeToken<ImageBean>() {
                                    }.getType());
                            if (!result.isFlag()) {
                                ToastUtil.show(context, result.getMsg());
                            } else {
                                handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_IMAGE_SUCCES, result.getData()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
