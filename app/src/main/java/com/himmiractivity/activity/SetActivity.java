package com.himmiractivity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.himmiractivity.App;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.AppManager;
import com.himmiractivity.Utils.ChangePhotosUtils;
import com.himmiractivity.Utils.FiledUtil;
import com.himmiractivity.Utils.IntentUtilsTwo;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.UiUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.OnBooleanListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.ModifyNameRequest;
import com.himmiractivity.request.TaskDetailImageUploadTask;
import com.himmiractivity.view.PhoneDialog;
import com.himmiractivity.view.ResetNameDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 设置页面
 */

public class SetActivity extends BaseBusActivity {
    public final String IMAGE_UNSPECIFIED = "image/*";
    private List<File> files;// 图片文件集合
    public static final int P_CAMERA = 1;
    public static final int P_ZOOM = 2;
    public static final int P_RESULT = 3;
    private String imageName;
    private String imageDir;
    private Bitmap photo;
    @BindViews({R.id.btn_back, R.id.iv_shared_device})
    List<ImageView> imageViews;
    @BindViews({R.id.ll_equip, R.id.ll_message, R.id.ll_feedback, R.id.ll_phone, R.id.ll_help, R.id.ll_chanage_pass, R.id.ll_exit, R.id.ll_version})
    List<LinearLayout> linearLayouts;
    PhoneDialog phoneDialog;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cir_image)
    SimpleDraweeView simImage;
    TextView tv_sell_phone, tv_service_phone;
    Bundle bundle;
    UserData userData;
    //修改昵称
    ResetNameDialog dialog;
    ImageBean imageBean;
    private boolean isResult = false;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    ModifyNameData modifyNameData = (ModifyNameData) msg.obj;
                    if (!TextUtils.isEmpty(dialog.getName())) {
                        tvTitle.setText(dialog.getName());
                    } else {
                        tvTitle.setText(modifyNameData.getUserName());
                    }
                    isResult = true;
                    break;
                case StatisConstans.MSG_IMAGE_SUCCES:
                    imageBean = (ImageBean) msg.obj;
                    if (photo != null) {
                        /**
                         * 圆形头像加载
                         * */
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(SetActivity.this.getContentResolver(), photo, null, null));
                        simImage.setController(Fresco.newDraweeControllerBuilder()
                                .setUri(uri)
                                .setTapToRetryEnabled(true)
                                .setOldController(simImage.getController())
                                .build());
                        isResult = true;
                    }
                    break;
            }

            return false;
        }
    }

    );

    @Override
    protected int getContentLayoutId() {
        return R.layout.set;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        getWrite();
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getSerializable(StatisConstans.USERDATA) != null) {
                userData = (UserData) bundle.getSerializable(StatisConstans.USERDATA);
                tvTitle.setText(userData != null && TextUtils.isEmpty(userData.getUserName().trim()) ? "熊卓" : userData.getUserName());
                /**
                 * 圆形头像加载
                 * */
                simImage.setController(Fresco.newDraweeControllerBuilder()
                        .setUri(Configuration.HOST + userData.getUserImage())
                        .setTapToRetryEnabled(true)
                        .setOldController(simImage.getController())
                        .build());
            }
        }
        files = new ArrayList<>();
    }

    @Override
    protected void initData() {
    }

    public void testCall(View view, final String phone) {
        permissionRequests(Manifest.permission.CALL_PHONE, new OnBooleanListener() {
            @Override
            public void onResulepermiss(boolean bln) {
                if (bln) {
                    callPhone(phone);
                } else {
                    ToastUtil.show(SetActivity.this, "请允许电话权限！");
                }
            }
        });
    }

    public void getPhoto() {
        permissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
            @Override
            public void onResulepermiss(boolean bln) {
                if (bln) {
                    permissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                        @Override
                        public void onResulepermiss(boolean bln) {
                            if (bln) {
                                ChangePhotosUtils
                                        .changePhotos(SetActivity.this);
                            } else {
                                ToastUtil.show(SetActivity.this, "请允许相机权限！");
                            }
                        }
                    });
                } else {
                    ToastUtil.show(SetActivity.this, "请允许读写权限！");
                }
            }
        });
    }

    public void getWrite() {
        permissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new OnBooleanListener() {
            @Override
            public void onResulepermiss(boolean bln) {
                if (bln) {

                } else {
                    ToastUtil.show(SetActivity.this, "请允许读写权限！");
                }
            }
        });
    }

    public void callPhone(String phone) {
        IntentUtilsTwo.intentToCall(this, phone);
        phoneDialog.dismiss();
    }

    @OnClick(R.id.ll_equip)
    public void startQquip() {
        Intent intentEq = new Intent();
        intentEq.setClass(this, QquipManager.class);
        startActivityForResult(intentEq, StatisConstans.MSG_SUCCCE_TURS);
    }

    @OnClick(R.id.ll_phone)
    public void contactPhone() {
        openPopupWindow();
    }

    @OnClick(R.id.cir_image)
    public void cirPhoto() {
        getPhoto();
    }

    @OnClick(R.id.iv_shared_device)
    public void startDeviceShared() {
        Intent intentShared = new Intent(SetActivity.this, SharedDeviceActivity.class);
        startActivity(intentShared);
    }

    @OnClick(R.id.ll_version)
    public void startUpdataVersion() {
        Intent intentVer = new Intent(SetActivity.this, VersionUpdataActivity.class);
        startActivity(intentVer);
    }

    @OnClick(R.id.tv_title)
    public void titleResetName() {
        //防止重复按按钮
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new ResetNameDialog(SetActivity.this);
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
        dialog.init("请输入新的名称", "", "取消", "确定", "rename");
    }

    @OnClick(R.id.ll_help)
    public void llHelp() {
        startActivity(new Intent(this, HelpActivity.class));
    }

    @OnClick(R.id.ll_chanage_pass)
    public void chanagePass() {
        Intent intentRevice = new Intent();
        intentRevice.setClass(this, RevicePassActivity.class);
        startActivity(intentRevice);
    }

    @OnClick(R.id.ll_exit)
    public void llExit() {
        sharedPreferencesDB.setString(StatisConstans.USERPWD, "");
        Intent intent = new Intent();
        intent.setClass(this, LodingActivity.class);
        startActivity(intent);
        finish();
        AppManager.getAppManager().finishActivity(MainActivity.class);
    }

    @OnClick(R.id.btn_back)
    public void btnBack() {
        if (isResult) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            finish();
        }
    }


    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comit:
                if (TextUtils.isEmpty(dialog.getName())) {
                    ToastUtils.show(SetActivity.this, "新的名称不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                ModifyNameRequest modifyNameRequest = new ModifyNameRequest(sharedPreferencesDB, SetActivity.this, dialog.getName(), handler);
                try {
                    modifyNameRequest.requestCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.close();
                break;
            case R.id.tv_sell_phone:
                testCall(tv_sell_phone, phoneDialog.getSellPhone());
                break;
            case R.id.tv_service_phone:
                testCall(tv_service_phone, phoneDialog.getServicePhone());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isResult) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openPopupWindow() {
        //防止重复按按钮
        if (phoneDialog != null && phoneDialog.isShowing()) {
            return;
        }
        phoneDialog = new PhoneDialog(SetActivity.this);
        //设置PopupWindow的View点击事件
        phoneDialog.show();
        Window w = phoneDialog.getWindow();
        w.setWindowAnimations(R.style.AnimBottom);
        w.setGravity(Gravity.BOTTOM);
        w.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = w.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        w.setAttributes(lp);
        phoneDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 裁剪头像
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",
                UiUtil.dip2px(SetActivity.this, 75));
        intent.putExtra("outputY",
                UiUtil.dip2px(SetActivity.this, 75));
        intent.putExtra("return-data", true);
        startActivityForResult(intent, P_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StatisConstans.MSG_SUCCCE_TURS && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
        }
        /* 拍照 */
        if (requestCode == P_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            startPhotoZoom(Uri.fromFile(picture));
        }

        if (data == null)
            return;

		/* 读取相册缩放图片 */
        if (requestCode == P_ZOOM) {
            startPhotoZoom(data.getData());
        }

		/* 处理结果 */
        if (requestCode == P_RESULT) {
            Bundle extras = data.getExtras();

            if (extras != null) {
                photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // (0 - 100)压缩文件
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

//                photo = Bitmap.createScaledBitmap(photo, 70, 70, true);
//                photo.compress(Bitmap.CompressFormat.JPEG, 15, stream);

                imageDir = Environment.getExternalStorageDirectory()
                        + "/imageloader/Cache";
                imageName = userData.getUserMobile() + System.currentTimeMillis()
                        + ".jpg";
//                imageName = userData.getUserMobile() + DatetimeUtil.getTodayStr()
//                        + ".jpg";
                File f = new File(imageDir, imageName);
                if (!f.exists()) {
                    f.mkdirs();
                }
                if (files.size() > 0) {
                    files.remove(0);
                }
                FiledUtil.saveBitmap(photo, f);
                files.add(f);
                String url = imageDir + "/" + imageName;
//                TaskDetailImageUploadRequest taskDetailImageUploadRequest = new TaskDetailImageUploadRequest(sharedPreferencesDB, SetActivity.this, handler, files);
//                try {
//                    taskDetailImageUploadRequest.requestCode();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                try {
//                    UploadHelper uploadHelper = new UploadHelper();
//                    uploadHelper.upload(sharedPreferencesDB.getString("phone", ""), sharedPreferencesDB.getString("userDeviceUuid", ""), sharedPreferencesDB.getString("token", ""), f);
                    new TaskDetailImageUploadTask(
                            SetActivity.this, f,
                            handler).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
