package com.himmiractivity.request;

import android.telecom.Call;
import android.util.Log;

import com.himmiractivity.Constant.Configuration;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/17.
 */

public class UploadHelper {

    public static final String TAG = "UploadHelper";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private final OkHttpClient client = new OkHttpClient();

    public String upload(String mobile, String userDeviceUuid, String userToken, File file) throws Exception {

        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
///                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"file\"; filename=\"" + fileName + "\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, file))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"imagetype\""),
//                        RequestBody.create(null, imageType))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"userphone\""),
//                        RequestBody.create(null, userPhone))
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("userDeviceUuid", userDeviceUuid)
//                .addFormDataPart("userToken", userToken)
//                .addFormDataPart("mobile", mobile)
//                .addFormDataPart("image", file.getName(), fileBody)
//                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"userDeviceUuid\""),
                        RequestBody.create(null, userDeviceUuid))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"userToken\""),
                        RequestBody.create(null, userToken))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mobile\""),
                        RequestBody.create(null, mobile))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"image\";filename =\"file.getName()\""), fileBody)
                .build();
        Request request = new Request.Builder()
                .url(Configuration.URL_GETIMAGEUPLOADS)
                .post(requestBody)
                .build();

        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                String string = e.getMessage();
                Log.i("lfq", "onFailure" + string);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("lfq", response.message() + " , body " + str);

                } else {
                    Log.i("lfq", response.message() + " error : body " + response.body().string());
                }
            }
        });
//        try {
//            response = client.newCall(request).execute();
//            String jsonString = response.body().string();
//            Log.d(TAG, " upload jsonString =" + jsonString);
//
//            if (!response.isSuccessful()) {
//                throw new Exception("upload error code " + response);
//            } else {
//                JSONObject jsonObject = new JSONObject(jsonString);
//                int errorCode = jsonObject.getInt("errorCode");
//                if (errorCode == 0) {
//                    Log.d(TAG, " upload data =" + jsonObject.getString("data"));
//                    return jsonObject.getString("data");
//                } else {
//                    throw new Exception("upload error code " + errorCode + ",errorInfo=" + jsonObject.getString("errorInfo"));
//                }
//            }
//
//        } catch (Exception e) {
//            Log.d(TAG, "upload IOException ", e);
//        }
        return null;
    }
}
