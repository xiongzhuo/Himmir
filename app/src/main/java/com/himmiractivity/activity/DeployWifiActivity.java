package com.himmiractivity.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class DeployWifiActivity extends BaseBusActivity implements OnSmartLinkListener {
    public final String EXTRA_SMARTLINK_VERSION = "EXTRA_SMARTLINK_VERSION";
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_wifi_name)
    TextView tv_wifi_name;
    @BindView(R.id.et_wifi_pass)
    EditText etWifiPass;
    String wifiName;
    protected ISmartLinker mSnifferSmartLinker;
    protected DialogView mWaitingDialog;
    protected Handler mViewHandler = new Handler();
    private boolean mIsConncting = false;

    @Override
    protected int getContentLayoutId() {
        return R.layout.deploy_wifi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        int smartLinkVersion = getIntent().getIntExtra(EXTRA_SMARTLINK_VERSION, 3);
        if (smartLinkVersion == 7) {
            mSnifferSmartLinker = MulticastSmartLinker.getInstance();
        } else {
            mSnifferSmartLinker = SnifferSmartLinker.getInstance();
        }
        if (null == mWaitingDialog) {
            mWaitingDialog = new DialogView(DeployWifiActivity.this);
        }
        mWaitingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                mSnifferSmartLinker.setOnSmartLinkListener(null);
                mSnifferSmartLinker.stop();
                mIsConncting = false;
            }
        });
    }

    @Override
    protected void initData() {
        setMidTxt("配置Wi-Fi密码");
        initTitleBar();
        btnNext.setOnClickListener(this);
        wifiName = WifiUtils.getConnectWifiSsid(DeployWifiActivity.this);
        tv_wifi_name.setText(wifiName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (TextUtils.isEmpty(etWifiPass.getText().toString().trim())) {
                    ToastUtil.show(DeployWifiActivity.this, "WI-FI密码不能为空");
                    return;
                }
                mWaitingDialog.show();
                mWaitingDialog.setMessage("正在配网中");
                if (!mIsConncting) {
                    //设置要配置的ssid 和pswd
                    try {
                        mSnifferSmartLinker.setOnSmartLinkListener(DeployWifiActivity.this);
                        //开始 smartLink
                        mSnifferSmartLinker.start(getApplicationContext(), etWifiPass.getText().toString().trim(),
                                tv_wifi_name.getText().toString().trim());
                        mIsConncting = true;
                        mWaitingDialog.show();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    //成功的方法
    @Override
    public void onLinked(final SmartLinkedModule smartLinkedModule) {
        mViewHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != mWaitingDialog) {
                    mWaitingDialog.cancel();
                }
                Intent intent = new Intent();
                intent.setClass(DeployWifiActivity.this, ScanQRCode.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("smartLinkedModule", smartLinkedModule);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //失败的方法
    @Override
    public void onCompleted() {
        mViewHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (null != mWaitingDialog) {
                    mWaitingDialog.cancel();
                }
                Toast.makeText(getApplicationContext(), getString(R.string.hiflying_smartlinker_completed),
                        Toast.LENGTH_SHORT).show();
                mWaitingDialog.dismiss();
                mIsConncting = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSnifferSmartLinker.setOnSmartLinkListener(null);
//        try {
//            unregisterReceiver(mWifiChangedReceiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //超时的方法
    @Override
    public void onTimeOut() {
        mViewHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (null != mWaitingDialog) {
                    mWaitingDialog.cancel();
                }
                ToastUtils.show(DeployWifiActivity.this, getString(R.string.hiflying_smartlinker_timeout),
                        Toast.LENGTH_SHORT);
                mWaitingDialog.dismiss();
                mIsConncting = false;
            }
        });
    }
}
