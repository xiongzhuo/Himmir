package com.himmiractivity.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;
import com.himmiractivity.App;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.WifiUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.DialogView;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;


public class SharedDeviceActivity extends BaseBusActivity {
    private final int SCANNIN_GREQUEST_CODE = 1;
    @BindViews({R.id.ll_my_qrcode, R.id.ll_projection_screen, R.id.ll_add_shared, R.id.ll_manager_shared, R.id.ll_added_shared})
    List<LinearLayout> linearLayouts;

    @Override
    protected int getContentLayoutId() {
        return R.layout.shared_device;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("分享设备");
        initTitleBar();
        linearLayouts.get(0).setOnClickListener(this);
        linearLayouts.get(1).setOnClickListener(this);
        linearLayouts.get(2).setOnClickListener(this);
        linearLayouts.get(3).setOnClickListener(this);
        linearLayouts.get(4).setOnClickListener(this);
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
            case R.id.ll_my_qrcode:
                startActivity(new Intent(SharedDeviceActivity.this, MyQrcodeActivity.class));
                break;
            case R.id.ll_projection_screen:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            StatisConstans.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(SharedDeviceActivity.this, MipcaActivityCaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                }
                break;
            case R.id.ll_add_shared:
                break;
            case R.id.ll_added_shared:
                break;
            case R.id.ll_manager_shared:
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == StatisConstans.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setClass(SharedDeviceActivity.this, MipcaActivityCaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            } else {
                Toast.makeText(SharedDeviceActivity.this, "请你允许才能扫描二维码", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //二维吗扫描结果

                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
