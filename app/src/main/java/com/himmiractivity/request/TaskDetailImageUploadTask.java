package com.himmiractivity.request;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.FiledUtil;
import com.himmiractivity.Utils.FormFile;
import com.himmiractivity.Utils.ImageUploadForm;
import com.himmiractivity.Utils.ImageUtil;
import com.himmiractivity.Utils.JsonUtils;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.JsonResult;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 任务上传图片
 */
public class TaskDetailImageUploadTask extends
        AsyncTask<Void, Void, JsonResult<ImageBean>> {

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, File> filMap = new HashMap<String, File>();
    private Activity context;
    private SharedPreferencesDB sharedDB;
    private Handler handler;
    private DialogView dialog;
    //    private FormFile formFile;
    private File file;

    public TaskDetailImageUploadTask(Activity context, File file, Handler handler) throws Exception {
        this.context = context;
        this.handler = handler;

        sharedDB = SharedPreferencesDB.getInstance(context);
//        map.put("fileType", "1");
        map.put("userToken", sharedDB.getString("token", ""));
        map.put("userDeviceUuid", sharedDB.getString("userDeviceUuid", ""));
        map.put("mobile", sharedDB.getString("phone", ""));
        this.file = file;
//        map.put("image", getBitmapByte(bitmap).toString());
//        for (File file : files) {
        file = file;
//            formFile = new FormFile(file.getName(), file, "image", "image/jpeg");
//            filMap.put(file.getName(), file);
//    }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new DialogView(context);
        dialog.show();
        dialog.setMessage("加载中");
    }

    @Override
    protected JsonResult<ImageBean> doInBackground(Void... params) {
        JsonResult<ImageBean> result = new JsonResult<ImageBean>();
        try {
            ImageUploadForm imageUploadForm = new ImageUploadForm();
//            Log.i("json", ImageUtil.getUrl(Configuration.URL_GETIMAGEUPLOADS, map));
            String json = imageUploadForm.uploadForm(map, "image", file, file.getName(), ImageUtil.getUrl(Configuration.URL_GETIMAGEUPLOADS, map));
//            json = FiledUtil.toUtf8(json);
//            Log.i("json", json);
//            boolean succ = SocketHttpRequester.post(Configuration.URL_GETIMAGEUPLOADS, map,
//                    formFile);

//            Log.i("ResetPwdRequest", succ + "================");
//            result.setFlag(true);
            if (TextUtils.isEmpty(json)) {
                return result;
            }
            result = JsonUtils.parseJson(json,
                    new TypeToken<ImageBean>() {
                    }.getType());
        } catch (Exception e) {
            result.setMsg("服务器错误！");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JsonResult<ImageBean> result) {
        super.onPostExecute(result);
        if (!result.isFlag()) {
            ToastUtil.show(context, result.getMsg());
        } else {
            handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_IMAGE_SUCCES, result.getData()));
        }
        if (dialog != null) {
            dialog.close();
        }
    }

    //
//    public byte[] SaveImage(String path) {
//        FileStream fs = new FileStream(path, FileMode.Open, FileAccess.Read); //将图片以文件流的形式进行保存
//        BinaryReader br = new BinaryReader(fs);
//        byte[] imgBytesIn = br.ReadBytes((int) fs.Length);  //将流读入到字节数组中
//        return imgBytesIn;
//    }

    //qq  caonimei   QQ  dakk  dengxia   wolai chuli  dakai QQ
//    private String Stream2String(InputStream is) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); //强制缓存大小为16KB，一般Java类默认为8KB
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {  //处理换行符
//                sb.append(line + "\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return sb.toString();
//    }
    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}

