package com.himmiractivity.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.himmiractivity.Utils.AppManager;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.activity.MainActivity;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.view.SwipeBackLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;

import activity.hamir.com.himmir.R;
import butterknife.ButterKnife;

public abstract class BaseBusActivity extends AutoLayoutActivity implements View.OnClickListener {
    protected Context mContext;
    View mTitleView;
    ImageView mLeftView;// 左侧按钮
    TextView mMidView;// 中间文本
    TextView mRightView;// 右侧按钮
    protected SharedPreferencesDB sharedPreferencesDB;

    private GoogleApiClient client;
    protected SwipeBackLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesDB = SharedPreferencesDB.getInstance(this);
        x.view().inject(this);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.base, null);
        layout.attachToActivity(this);
        setContentView(this.getContentLayoutId());
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    StatisConstans.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            if (TextUtils.isEmpty(sharedPreferencesDB.getString("userDeviceUuid", ""))) {
                String imei = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
                sharedPreferencesDB.setString("userDeviceUuid", imei);
            }
        }
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        this.initView(savedInstanceState);
        this.initData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化菜单栏
     */

    protected void initTitleBar() {
        mTitleView = findViewById(R.id.title_bar);
        if (mTitleView != null) {
            mLeftView = (ImageView) findViewById(R.id.btn_left);
            mTitleView.setVisibility(View.VISIBLE);
            mLeftView.setOnClickListener(this);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 加载页面布局文件
     */
    protected abstract int getContentLayoutId();

    /**
     * 页面控件初始化
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * Initialize the Activity data
     */
    protected abstract void initData();

    /**
     * Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        this.showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        if (msg == null) return;
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this, msg, duration);
        } else {
            ToastUtils.show(this, msg, ToastUtils.LENGTH_SHORT);
        }
    }

    public void showToast(int resId) {
        this.showToast(resId, Toast.LENGTH_SHORT);
    }


    public void showToast(int resId, int duration) {
        if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
            ToastUtils.show(this, resId, duration);
        } else {
            ToastUtils.show(this, resId, ToastUtils.LENGTH_SHORT);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("BaseBus Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    /**
     * 设置中间文本
     */

    protected void setMidTxt(String strTxt) {
        mMidView = (TextView) findViewById(R.id.tv_title);
        if (mMidView != null) {
            mMidView.setText(strTxt);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == StatisConstans.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (TextUtils.isEmpty(sharedPreferencesDB.getString("userDeviceUuid", ""))) {
                    String imei = ((TelephonyManager) this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
                    sharedPreferencesDB.setString("userDeviceUuid", imei);
                }
            } else {
                // Permission Denied
                Toast.makeText(this, "请允许才能获得设备ID", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 设置中间文本
     */

    protected void setRightView(String strTxt, boolean isTrue) {
        mRightView = (TextView) findViewById(R.id.btn_right);
        if (mRightView != null) {
            mRightView.setText(strTxt);
            if (isTrue) {
                mRightView.setVisibility(View.VISIBLE);
                mRightView.setOnClickListener(this);
            }
        }
    }
}
