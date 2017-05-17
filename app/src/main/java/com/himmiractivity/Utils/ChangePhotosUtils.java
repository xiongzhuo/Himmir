package com.himmiractivity.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;

import activity.hamir.com.himmir.R;

/**
 * Created by Administrator on 2016/7/5 0005.
 */
public class ChangePhotosUtils {
    public static final int P_ZOOM = 2;
    public static final int P_CAMERA = 1;

    /**
     * 选择照片方式
     */
    public static void changePhotos(final Activity context) {

        View view = context.getLayoutInflater()
                .inflate(R.layout.choicesex_dialog, null);
        final Button radio1 = (Button) view.findViewById(R.id.radio1);
        final Button radio2 = (Button) view.findViewById(R.id.radio2);
        final AlertDialog builder = new AlertDialog.Builder(
                context).create();
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takephotos(context);
                builder.dismiss();
            }
        });
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choicephotos(context);
                builder.dismiss();
            }
        });
        Window w = builder.getWindow();
        w.setWindowAnimations(R.style.mystyle1);
        builder.show();
        builder.getWindow().setContentView(view);
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = (int) (display.getWidth() * 0.87); //设置宽度
        builder.getWindow().setAttributes(lp);
    }

    /**
     * 选择图片
     */
    public static void choicephotos(Activity context) {
        context.startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                P_ZOOM);
    }

    /**
     * 拍照
     */
    public static void takephotos(Activity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "temp.jpg")));
        context.startActivityForResult(intent, P_CAMERA);
    }

}
