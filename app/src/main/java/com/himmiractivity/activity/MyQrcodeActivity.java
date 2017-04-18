package com.himmiractivity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.App;
import com.himmiractivity.Utils.Utils;
import com.himmiractivity.base.BaseBusActivity;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class MyQrcodeActivity extends BaseBusActivity {
    @BindView(R.id.ll_my_qrcode)
    LinearLayout llMyQrcode;

    @Override
    protected int getContentLayoutId() {
        return R.layout.my_qrcode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
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
        imageView.setImageResource(R.drawable.launch_image); //图片资源
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams t_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        t_params.setMargins(0, 15, 0, 0);
        textView.setLayoutParams(t_params);  //设置图片宽高
        textView.setText("扫一扫我的二维码名片，添加分享。"); //图片资源
        llMyQrcode.addView(imageView); //动态添加图片
        llMyQrcode.addView(textView); //动态添加图片
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
        super.onDestroy();
    }
}
