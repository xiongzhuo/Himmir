package com.himmiractivity.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.himmiractivity.App;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.Utils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.ModifyNameRequest;
import com.himmiractivity.request.ReviseSharingNameRequest;
import com.himmiractivity.view.ResetNameDialog;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;


public class MyQrcodeActivity extends BaseBusActivity {
    @BindView(R.id.image_herd)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.ll_my_qrcode)
    LinearLayout llMyQrcode;
    private String userShareName, userShareCode, userShareImage;
    @BindViews({R.id.tv_sharing_name})
    List<TextView> textViews;
    @BindViews({R.id.btn_revise_name})
    List<ImageView> imageViews;
    //修改昵称
    ResetNameDialog dialog;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    if (!TextUtils.isEmpty(dialog.getName())) {
                        textViews.get(0).setText("共享名：" + dialog.getName());
                        sharedPreferencesDB.setString(StatisConstans.USER_SHARE_NAME, dialog.getName());
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.my_qrcode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        userShareName = sharedPreferencesDB.getString(StatisConstans.USER_SHARE_NAME, "");
        userShareCode = sharedPreferencesDB.getString(StatisConstans.USER_SHARE_CODE, "");
        userShareImage = sharedPreferencesDB.getString(StatisConstans.USER_SHARE_IMAGE, "");
        Log.i("ModifyNameRequest", userShareImage);
        textViews.get(0).setText("共享名：" + userShareName);
        imageViews.get(0).setOnClickListener(this);
        simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                .setUri(Configuration.HOST + userShareImage)
                .setTapToRetryEnabled(true)
                .setOldController(simpleDraweeView.getController())
                .build());
        setWindowBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL);
        setMidTxt("我的二维码");
        initTitleBar();
        addGroupImage();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_revise_name:
                //防止重复按按钮
                if (dialog != null && dialog.isShowing()) {
                    return;
                }
                dialog = new ResetNameDialog(MyQrcodeActivity.this);
                Window w = dialog.getWindow();
                if (w != null) {
                    w.setWindowAnimations(R.style.mystyle1);
                }
                dialog.show();
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                int width = getWindowManager().getDefaultDisplay().getWidth();
                dialog.getWindow().setLayout((int) (width * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setListener();
                dialog.init("请输入新的共享名", "", "取消", "确定", "rename");
                break;
            case R.id.btn_comit:
                if (TextUtils.isEmpty(dialog.getName())) {
                    ToastUtils.show(MyQrcodeActivity.this, "新的共享名不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                ReviseSharingNameRequest reviseSharingNameRequest = new ReviseSharingNameRequest(sharedPreferencesDB, MyQrcodeActivity.this, dialog.getName(), handler);
                try {
                    reviseSharingNameRequest.requestCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.close();
                break;
            case R.id.btn_canel:
                dialog.close();
                break;
            default:
                break;
        }
    }

    //size:代码中获取到的图片数量
    private void addGroupImage() {
        int width = Utils.getScreenWidth(this);
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width * 0.7), (int) (width * 0.7));
        imageView.setLayoutParams(params);  //设置图片宽高
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Bitmap bitmap = Utils.addLogo(Utils.create2DCoderBitmap(userShareCode, width, width), BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//        Bitmap bitmap = Utils.create2DCoderBitmap(userShareCode, width, width);
        imageView.setImageBitmap(bitmap); //图片资源
        llMyQrcode.addView(imageView); //动态添加图片
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        //取消屏幕最亮
        setWindowBrightness(WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
        super.onDestroy();
    }

    /**
     * 设置当前窗口亮度
     *
     * @param brightness
     */
    private void setWindowBrightness(float brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }
}
