package com.himmiractivity.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.himmiractivity.App;
import com.himmiractivity.Utils.ChangePhotosUtils;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.AddedBean;
import com.himmiractivity.interfaces.OnBooleanListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.AddShardRequest;
import com.himmiractivity.request.AddTVRequest;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindViews;


public class SharedDeviceActivity extends BaseBusActivity {
    private final int SCANNIN_GREQUEST_CODE = 1;
    private final int ADD_SHARED = 2;
    @BindViews({R.id.ll_my_qrcode, R.id.ll_projection_screen, R.id.ll_add_shared, R.id.ll_manager_shared, R.id.ll_added_shared})
    List<LinearLayout> linearLayouts;
    Intent intent;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    AddedBean addedBean = (AddedBean) msg.obj;
                    ToastUtil.show(SharedDeviceActivity.this, addedBean.getMsg());
                    break;
                case StatisConstans.MSG_QQUIP:
                    String string = (String) msg.obj;
                    ToastUtil.show(SharedDeviceActivity.this, string);
                    break;
            }
            return false;
        }
    });

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
                Intent intentMy = new Intent(SharedDeviceActivity.this, MyQrcodeActivity.class);
                startActivity(intentMy);
                break;
            case R.id.ll_projection_screen:
                permissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
                    @Override
                    public void onResulepermiss(boolean bln) {
                        if (bln) {
                            permissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                                @Override
                                public void onResulepermiss(boolean bln) {
                                    if (bln) {
                                        intent = new Intent();
                                        intent.setClass(SharedDeviceActivity.this, CaptureActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                                    } else {
                                        ToastUtil.show(SharedDeviceActivity.this, "请允许相机权限！");
                                    }
                                }
                            });
                        } else {
                            ToastUtil.show(SharedDeviceActivity.this, "请允许读写权限！");
                        }
                    }
                });
                break;
            case R.id.ll_add_shared:
                permissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
                    @Override
                    public void onResulepermiss(boolean bln) {
                        if (bln) {
                            permissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                                @Override
                                public void onResulepermiss(boolean bln) {
                                    if (bln) {
                                        intent = new Intent();
                                        intent.setClass(SharedDeviceActivity.this, CaptureActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivityForResult(intent, ADD_SHARED);
                                    } else {
                                        ToastUtil.show(SharedDeviceActivity.this, "请允许相机权限！");
                                    }
                                }
                            });
                        } else {
                            ToastUtil.show(SharedDeviceActivity.this, "请允许读写权限！");
                        }
                    }
                });
                break;
            case R.id.ll_added_shared:
                intent = new Intent();
                intent.setClass(SharedDeviceActivity.this, AddedSharedActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_manager_shared:
                intent = new Intent();
                intent.setClass(SharedDeviceActivity.this, ManagerSharedActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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
                    //二维吗扫描结果
                    try {
                        Bundle bundle = data.getExtras();
                        String shareCode = bundle.getString(StatisConstans.RESULT);
                        Log.i("shareCode", shareCode);
                        AddTVRequest addTVRequest = new AddTVRequest(sharedPreferencesDB, shareCode, this, handler);
                        addTVRequest.requestCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ADD_SHARED:
                if (resultCode == RESULT_OK) {
                    //二维吗扫描结果
                    try {
                        Bundle bundle = data.getExtras();
                        String shareCode = bundle.getString(StatisConstans.RESULT);
                        Log.i("shareCode", shareCode);
                        AddShardRequest addShardRequest = new AddShardRequest(sharedPreferencesDB, this, shareCode, handler);
                        addShardRequest.requestCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
