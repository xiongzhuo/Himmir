package com.himmiractivity.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hiflying.smartlink.SmartLinkedModule;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.DeviceInfoBean;
import com.himmiractivity.interfaces.OnBooleanListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.DeviceInfoRequest;
import com.himmiractivity.view.ClearEditText;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class ScanQRCode extends BaseBusActivity {
    private final int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.et_code_number)
    ClearEditText etCodeNumber;
    @BindView(R.id.btn_qr_next)
    Button btnQrNext;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    SmartLinkedModule smartLinkedModule;
    String code;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    DeviceInfoBean deviceInfoBean = (DeviceInfoBean) msg.obj;
                    Intent intent = new Intent();
                    intent.setClass(ScanQRCode.this, InformationComitActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(StatisConstans.DEVICE_INFO, deviceInfoBean);
                    bundle.putString(StatisConstans.SERVER_NUMBER, code);
                    bundle.putString(StatisConstans.MAC, smartLinkedModule.getMac());
                    bundle.putString(StatisConstans.IP, smartLinkedModule.getIp());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.scan_qr_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("扫描二维码");
        initTitleBar();
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            smartLinkedModule = (SmartLinkedModule) bundle.getSerializable("smartLinkedModule");
        }
        ivQrCode.setOnClickListener(this);
        btnQrNext.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qr_code:
                setIvQrCode();
                break;
            case R.id.btn_qr_next:
                code = etCodeNumber.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.show(this, "请输入二维码编号");
                    return;
                }
                try {
                    DeviceInfoRequest deviceInfoRequest = new DeviceInfoRequest(sharedPreferencesDB, code, smartLinkedModule.getMac(), handler, this);
                    deviceInfoRequest.requestCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }

    }

    public void setIvQrCode() {
        permissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
            @Override
            public void onResulepermiss(boolean bln) {
                if (bln) {
                    Intent intent = new Intent();
                    intent.setClass(ScanQRCode.this, CaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                } else {
                    ToastUtil.show(ScanQRCode.this, "请允许调用相机权限！");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    etCodeNumber.setText(bundle.getString(StatisConstans.RESULT));
                }
                break;
        }
    }
}
