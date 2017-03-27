package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himmiractivity.App;
import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.Utils.ChangePhotosUtils;
import com.himmiractivity.Utils.FiledUtil;
import com.himmiractivity.Utils.IntentUtilsTwo;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.UiUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.ModifyNameRequest;
import com.himmiractivity.request.TaskDetailImageUploadTask;
import com.himmiractivity.view.CircleImageView;
import com.himmiractivity.view.ResetNameDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

/**
 * 设置页面
 */

public class SetActivity extends BaseBusActivity implements PopupWindow.OnDismissListener {
    private ImageLoader im = ImageLoader.getInstance();
    public final String IMAGE_UNSPECIFIED = "image/*";
    private List<File> files;// 图片文件集合
    public static final int P_NONE = 0;
    public static final int P_CAMERA = 1;
    public static final int P_ZOOM = 2;
    public static final int P_RESULT = 3;
    private String imageName;
    private String imageDir;
    private Bitmap photo;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.ll_equip)
    LinearLayout llEquip;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_feedback)
    LinearLayout llFeedback;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_help)
    LinearLayout llHelp;
    @BindView(R.id.ll_chanage_pass)
    LinearLayout llChanagePass;
    @BindView(R.id.ll_exit)
    LinearLayout llExit;
    PopupWindow popupWindow;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cir_image)
    CircleImageView cirImage;
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
                    tvTitle.setText(modifyNameData.getUserName());
                    break;
                case StatisConstans.MSG_IMAGE_SUCCES:
                    imageBean = (ImageBean) msg.obj;
                    if (photo != null) {
                        cirImage.setImageBitmap(photo);
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
//        initTitleBar();
//        setMidTxt("修改密码");
        App.getInstance().addActivity(this);
        im.init(ImageLoaderConfiguration
                .createDefault(SetActivity.this));
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getSerializable("userData") != null) {
                userData = (UserData) bundle.getSerializable("userData");
                tvTitle.setText(userData != null && TextUtils.isEmpty(userData.getUserName().trim()) ? "熊卓" : userData.getUserName());
                if (!TextUtils.isEmpty(userData.getUserImage())) {// 加载登陆头像
                    DisplayImageOptions.Builder mOptions = new DisplayImageOptions.Builder();
                    mOptions.displayer(new RoundedBitmapDisplayer(20)).build();
                    im.displayImage(Configuration.HOST + userData.getUserImage(), cirImage);
                } else {// 显示默认头像
                    cirImage.setImageResource(R.drawable.overlay);
                }
            }
        }
        files = new ArrayList<>();
        llEquip.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llPhone.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        llChanagePass.setOnClickListener(this);
        llExit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        cirImage.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //设备管理
            case R.id.ll_equip:
                Intent intentEq = new Intent();
                intentEq.setClass(this, QquipManager.class);
                startActivity(intentEq);
                break;
            case R.id.cir_image:
                ChangePhotosUtils
                        .changePhotos(SetActivity.this);
                break;
            //我的消息
            case R.id.ll_message:
                break;
            //质量反馈
            case R.id.ll_feedback:
                break;
            //联系客服
            case R.id.ll_phone:
                openPopupWindow(v);
                break;
            case R.id.tv_title:
                dialog = new ResetNameDialog(SetActivity.this);
                Window w = dialog.getWindow();
                w.setWindowAnimations(R.style.mystyle1);
                dialog.show();
                //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                int width = getWindowManager().getDefaultDisplay().getWidth();
                dialog.getWindow().setLayout((int) (width * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setListener();
                dialog.init("请输入新的名称", "", "取消", "确定", "rename");
                break;
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
            case R.id.btn_canel:
                dialog.close();
                break;
            //使用帮助
            case R.id.ll_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            //修改密码
            case R.id.ll_chanage_pass:
                Intent intentRevice = new Intent();
                intentRevice.setClass(this, RevicePassActivity.class);
                startActivity(intentRevice);
                break;
            //退出登陆
            case R.id.ll_exit:
                sharedPreferencesDB.setString("username", "");
                sharedPreferencesDB.setString("userpwd", "");
                Intent intent = new Intent();
                intent.setClass(this, LodingActivity.class);
                startActivity(intent);
                finish();
                if (MainActivity.instans != null) {
                    MainActivity.instans.finish();
                }
                break;
            //返回
            case R.id.btn_back:
                if (isResult) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.tv_sell_phone:
                IntentUtilsTwo.intentToCall(this, tv_sell_phone.getText().toString().trim());
                popupWindow.dismiss();
                break;
            case R.id.tv_service_phone:
                IntentUtilsTwo.intentToCall(this, tv_service_phone.getText().toString().trim());
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
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

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.pop_phone, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(this);

        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        tv_sell_phone = (TextView) view.findViewById(R.id.tv_sell_phone);
        tv_service_phone = (TextView) view.findViewById(R.id.tv_service_phone);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_sell_phone.setOnClickListener(this);
        tv_sell_phone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    /**
     * 裁剪头像
     *
     * @param uri
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

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

                photo = Bitmap.createScaledBitmap(photo, 70, 70, true);
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

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
    }
}
