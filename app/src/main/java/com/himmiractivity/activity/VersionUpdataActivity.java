package com.himmiractivity.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.UpdateManage;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.VersionBean;
import com.himmiractivity.interfaces.OnBooleanListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.VersoinUpdataRequest;
import com.himmiractivity.util.AppUtils;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class VersionUpdataActivity extends BaseBusActivity {
    @BindViews({R.id.ll_up_to_version, R.id.ll_version_ing, R.id.ll_versioned})
    List<LinearLayout> linearLayouts;
    @BindViews({R.id.tv_old_version, R.id.tv_up_to_version, R.id.tv_versioned})
    List<TextView> textViews;
    @BindView(R.id.btn_version_updata)
    Button btnVersionUpdata;
    private int versionCode;
    private String versionName;

    private boolean isDown = false;

    private final int SHOW_DIALOG = 101;
    VersionBean versionBean;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.FAIL:
                    linearLayouts.get(0).setVisibility(View.GONE);
                    linearLayouts.get(1).setVisibility(View.GONE);
                    linearLayouts.get(2).setVisibility(View.GONE);
                    break;
                case StatisConstans.MSG_QQUIP:
                    versionBean = (VersionBean) msg.obj;
                    //获得当前版本号
                    versionCode = AppUtils.getAppVersionCode(VersionUpdataActivity.this);
                    versionName = VersionUpdataActivity.this.getResources().getString(R.string.app_version);
                    //假设我们在子线程联网后获取服务器版本号和版本名称(真是项目中都是从服务器获取的)
                    //然后判断服务器版本号是否和当前版本号一样
                    if (versionBean.getAppVerInfo().getVersion_code() > versionCode) {//发送一个handler进入主页面
                        linearLayouts.get(0).setVisibility(View.VISIBLE);
                        linearLayouts.get(1).setVisibility(View.GONE);
                        linearLayouts.get(2).setVisibility(View.GONE);
                        textViews.get(0).setText("当前版本：" + versionName);
                        textViews.get(1).setText("最新版本：" + versionBean.getAppVerInfo().getVersion_name());
                    } else {
                        linearLayouts.get(0).setVisibility(View.GONE);
                        linearLayouts.get(1).setVisibility(View.GONE);
                        linearLayouts.get(2).setVisibility(View.VISIBLE);
                        textViews.get(2).setText("当前已经是最新版本：" + versionName);
                    }
                    break;
                case SHOW_DIALOG:
                    showUpdateDialog();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_version;
    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_version_updata:
                permissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
                    @Override
                    public void onResulepermiss(boolean bln) {
                        if (bln) {
                            handler.sendEmptyMessage(SHOW_DIALOG);
                        } else {
                            ToastUtil.show(VersionUpdataActivity.this, "请允许读写权限！");
                        }
                    }
                });
//                if (ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            StatisConstans.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//                } else {
//                    handler.sendEmptyMessage(SHOW_DIALOG);
//                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("版本信息");
        initTitleBar();
        btnVersionUpdata.setOnClickListener(this);
        try {
            VersoinUpdataRequest versoinUpdataRequest = new VersoinUpdataRequest(sharedPreferencesDB, VersionUpdataActivity.this, handler);
            versoinUpdataRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == StatisConstans.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                handler.sendEmptyMessage(SHOW_DIALOG);
//            } else {
//                Toast.makeText(VersionUpdataActivity.this, "请您允许才能下载更新", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void showUpdateDialog() {
        //弹出对话框提示更新
        AlertDialog.Builder adb = new AlertDialog.Builder(VersionUpdataActivity.this);
        adb.setTitle("版本更新");
        adb.setMessage(versionBean.getAppVerInfo().getUpdata_content());

        adb.setCancelable(false);//要么点击确定，要么点击取消。否则不会关闭dialog
        adb.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //下载更新的APK
                downUpdateAPK();
            }
        });
        adb.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //点击取消进入主页面
                dialog.dismiss();
            }
        });
        adb.show();
    }

    /**
     * 下载新的APK
     */
    private void downUpdateAPK() {
        if (versionBean != null) {
            UpdateManage updateManage = new UpdateManage(VersionUpdataActivity.this, versionBean.getAppVerInfo().getAddress());
            updateManage.showDownloadDialog();
            isDown = true;
        }
    }
}
