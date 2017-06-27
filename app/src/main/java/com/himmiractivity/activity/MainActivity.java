package com.himmiractivity.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.himmiractivity.App;
import com.himmiractivity.Utils.GpsUtils;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.Utils;
import com.himmiractivity.Utils.WifiUtils;
import com.himmiractivity.base.BaseBusNoSocllowActivity;
import com.himmiractivity.circular_progress_bar.CircularProgressBar;
import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.FirstEvent;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.entity.PmBean;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.OnBooleanListener;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.DataServerConfigRequest;
import com.himmiractivity.request.LodingRequest;
import com.himmiractivity.request.OutdoorPMRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.ListPopupWindow;
import com.himmiractivity.view.PercentView;
import com.himmiractivity.view.SelectorImageView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseBusNoSocllowActivity {
    private long exitTime = 0;
    @BindView(R.id.progress)
    CircularProgressBar progressBar;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    Location mlocation;
    LocationManager mLocationManager;
    @BindView(R.id.percent_view)
    PercentView percentView;
    @BindViews({R.id.iv_add, R.id.iv_set, R.id.btn_speed_semll, R.id.btn_speed_add})
    List<ImageView> imageViews;
    @BindViews({R.id.iv_off_on_controller, R.id.iv_off_line_controller})
    List<SelectorImageView> selectorImageViews;
    @BindViews({R.id.tv_hz_valus, R.id.tv_room, R.id.tv_ug, R.id.tv_city, R.id.tv_co2, R.id.tv_out_side, R.id.tv_temp_indoor, R.id.tv_temp_out_side, R.id.tv_speed})
    List<TextView> textViews;
    private Double aimPercent = -1d;
    Bundle bundle;
    ListPopupWindow popupWindow = null;
    UserData userData;
    //获取设备
//    private List<String> list;
    private List<String> mList;
    private List<UserData.UserRoom> space;
    Socket socket;
    Protocal protocal;
    DataServerBean dataServerBean;
    String mac;
    PmAllData pmAllData;
    int hzNumeber;
    boolean isRevise = false;
    boolean isOff = false;
    //    boolean isFrist = true;
    private Timer mTimer = null;
    String host;
    int location = 0;
    ThreadPoolUtils threadPoolUtils;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.LOCATION:
                    Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    break;
                case StatisConstans.MSG_CYCLIC_TRANSMISSION:
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ScoketOFFeON.sendMessage(socket, protocal, mac);
                            } catch (Exception e) {
                                handler.sendEmptyMessage(StatisConstans.FAIL_TWO);
                                e.printStackTrace();
                            }
                        }
                    }));
                    break;
                case StatisConstans.FAIL_TWO:
                    if (!TextUtils.isEmpty(host)) {
                        Log.d("ConnectionManager", host);
                        threadPoolUtils.execute(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (Utils.isNetworkAvailable(MainActivity.this)) {
                                    try {
                                        if (socket != null) {
                                            socket.close();
                                            socket = null;
                                        }
                                        socket = SocketSingle.getInstance(host, location, true);
                                        ScoketOFFeON.receMessage(socket, protocal, handler);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }));
                    }
                    restoreData();
                    break;
                case StatisConstans.MSG_OUTDOOR_PM:
                    PmBean pmBean = (PmBean) msg.obj;
                    textViews.get(2).setText(pmBean.getPm25());
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    EventBus.getDefault().post(new FirstEvent("操作成功"));
                    // 发送 一个无序广播
//                    MainActivity.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_SUCC));
                    break;
                case StatisConstans.MSG_ENABLED_FAILING:
                    EventBus.getDefault().post(new FirstEvent("操作失败"));
                    // 发送 一个无序广播
//                    MainActivity.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_KILL));
                    break;
                case StatisConstans.MSG_QUEST_SERVER:
                    pmAllData = (PmAllData) msg.obj;
                    if (pmAllData.getFanFreq() > 9) {
                        upData();
                    } else {
                        restoreData();
                    }
                    EventBus.getDefault().post(pmAllData);
                    break;
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    userData = (UserData) msg.obj;
                    ll_content.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    sharedPreferencesDB.setString(StatisConstans.USER_SHARE_NAME, userData.getUserShareName());
                    sharedPreferencesDB.setString(StatisConstans.USER_SHARE_CODE, userData.getUserShareCode());
                    sharedPreferencesDB.setString(StatisConstans.USER_SHARE_IMAGE, userData.getUserImage());
                    initialization();
                    break;
                case StatisConstans.CONFIG_REGULAR:
                    dataServerBean = (DataServerBean) msg.obj;
                    host = dataServerBean.getDataServerConfig().getPrimary_server_address();
                    location = dataServerBean.getDataServerConfig().getPrimary_server_port();
                    sharedPreferencesDB.setString(StatisConstans.IP, dataServerBean.getDataServerConfig().getPrimary_server_address());
                    sharedPreferencesDB.setString(StatisConstans.PORT, dataServerBean.getDataServerConfig().getPrimary_server_port() + "");
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(dataServerBean.getDataServerConfig().getPrimary_server_address(), dataServerBean.getDataServerConfig().getPrimary_server_port());
                        }
                    }));
                    startTimer();
                    break;
                case StatisConstans.MSG_RECEIVED_BOUND:
                    startActivity(new Intent(MainActivity.this, LodingActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private GoogleApiClient client;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        mList = new ArrayList<String>();
        threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 10);
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getSerializable(StatisConstans.USERDATA) != null) {
                userData = (UserData) bundle.getSerializable(StatisConstans.USERDATA);
                sharedPreferencesDB.setString(StatisConstans.USER_SHARE_NAME, userData.getUserShareName());
                sharedPreferencesDB.setString(StatisConstans.USER_SHARE_CODE, userData.getUserShareCode());
                sharedPreferencesDB.setString(StatisConstans.USER_SHARE_IMAGE, userData.getUserImage());
                ll_content.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                initialization();
            } else {
                LodingRequest();
            }
        } else {
            LodingRequest();
        }
        textViews.get(3).setOnClickListener(this);
        imageViews.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = textViews.get(0).getText().toString().trim();
                if (!TextUtils.isEmpty(str) && !str.contains("-")) {
                    int hz = Integer.valueOf(textViews.get(0).getText().toString().trim());
                    if (hz < 14) {
                        ToastUtils.show(MainActivity.this, "风量最小值", Toast.LENGTH_SHORT);
                        textViews.get(0).setText("10");
                    } else {
                        hz = hz - 5;
                        textViews.get(0).setText(hz + "");
                        isRevise = true;
                        hzNumeber = hz;
                        threadPoolUtils.execute(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                    }
                } else {
                    ToastUtils.show(MainActivity.this, "设备离线", Toast.LENGTH_SHORT);
                }
            }
        });
        imageViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textViews.get(0).getText().toString().trim();
                if (!TextUtils.isEmpty(string) && !string.contains("-")) {
                    int hz = Integer.valueOf(textViews.get(0).getText().toString().trim());
                    if (hz > 46) {
                        ToastUtils.show(MainActivity.this, "风量最大值", Toast.LENGTH_SHORT);
                        textViews.get(0).setText("50");
                    } else {
                        hz = hz + 5;
                        textViews.get(0).setText(hz + "");
                        isRevise = true;
                        hzNumeber = hz;
                        threadPoolUtils.execute(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                    }
                } else {
                    ToastUtils.show(MainActivity.this, "设备离线", Toast.LENGTH_SHORT);
                }
            }
        });
        textViews.get(1).setOnClickListener(this);
        imageViews.get(1).setOnClickListener(this);
        imageViews.get(0).setOnClickListener(this);
        selectorImageViews.get(0).setOnClickListener(this);
        Log.i("aimPercent", aimPercent + "=-------");
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", "--");
        permissionRequests(Manifest.permission.ACCESS_FINE_LOCATION, new OnBooleanListener() {
            @Override
            public void onResulepermiss(boolean bln) {
                if (bln) {
                    permissionRequests(Manifest.permission.ACCESS_COARSE_LOCATION, new OnBooleanListener() {
                        @Override
                        public void onResulepermiss(boolean bln) {
                            if (bln) {
                                try {
                                    mlocation = getLocation();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                ToastUtil.show(MainActivity.this, "请允许获取位置权限！");
                            }
                        }
                    });
                } else {
                    ToastUtil.show(MainActivity.this, "请允许获取位置权限！");
                }
            }
        });
    }

    private void initialization() {
        mList.clear();
        if (userData.getUserDevs() != null && userData.getUserDevs().size() > 0) {
            space = userData.getUserDevs();
            for (int i = 0; i < space.size(); i++) {
                if (!TextUtils.isEmpty(space.get(i).getDevice_nickname())) {
                    mList.add(space.get(i).getDevice_nickname());
                } else {
                    mList.add("无名");
                }
            }
            if (mList.size() > 0) {
                mac = userData.getUserDevs().get(0).getDevice_mac();
                textViews.get(1).setText(mList.get(0).trim());
            }
        } else {
            mac = "";
            textViews.get(1).setText("暂无设备");
            restoreData();
        }
        initRechclerView();
    }

    @Override
    protected void initData() {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.tv_room:
                if (mList != null && mList.size() > 0) {
                    //防止重复按按钮
                    if (popupWindow != null && popupWindow.isShowing()) {
                        return;
                    }
                    popupWindow = new ListPopupWindow(MainActivity.this, textViews.get(1), mList, new ListPopupWindow.downOnclick() {
                        @Override
                        public void onDownItemClick(int position) {
                            mac = userData.getUserDevs().get(position).getDevice_mac();
                            textViews.get(1).setText(mList.get(position).trim());
                            handler.sendEmptyMessage(StatisConstans.MSG_CYCLIC_TRANSMISSION);
                        }
                    });
                } else {
                    ToastUtil.show(MainActivity.this, "暂无房间");
                }
                break;
            case R.id.iv_add:
                if (Utils.isFastClick()) {
                    //判断是否开启了WI-FI
                    if (WifiUtils.isWifiConnected(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, DeployWifiActivity.class));
                    } else {
                        ToastUtils.show(MainActivity.this, "设备WI-FI未开启", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case R.id.iv_set:
                if (Utils.isFastClick()) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SetActivity.class);
                    Bundle bundle = new Bundle();
                    if (userData != null) {
                        bundle.putSerializable(StatisConstans.USERDATA, userData);
                    }
                    intent.putExtras(bundle);
                    startActivityForResult(intent, StatisConstans.MSG_IMAGE_REQUEST);
                }
                break;
            case R.id.iv_off_on_controller:
                if (Utils.isFastClick()) {
                    selectorImageViews.get(0).toggle(!selectorImageViews.get(0).isChecked());
                    isOff = true;
                    if (protocal == null) {
                        protocal = new Protocal();
                    }
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ScoketOFFeON.sendMessage(socket, protocal, mac, selectorImageViews.get(0).isChecked());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() throws Exception {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有定位权限", Toast.LENGTH_SHORT).show();
            return null;
        }
        mlocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mlocation == null) {
            mlocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (GpsUtils.isGpsEnable(this)) {
            threadPoolUtils.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    GPSLocation();
                }
            }));
        } else {
            Intent callGPSSettingIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            this.startActivity(callGPSSettingIntent);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, mLocationListener);
        return mlocation;
    }

    //开启定位回调接口
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            try {
                mlocation = getLocation();
//                GPSLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void LodingRequest() {
        if (TextUtils.isEmpty(sharedPreferencesDB.getString(StatisConstans.USERPWD, "")) || TextUtils.isEmpty(sharedPreferencesDB.getString(StatisConstans.USERNAME, ""))) {
            startActivity(new Intent(this, LodingActivity.class));
            finish();
        } else {
            LodingRequest lodingRequest = new LodingRequest(sharedPreferencesDB, this, sharedPreferencesDB.getString(StatisConstans.USERPWD, ""), sharedPreferencesDB.getString(StatisConstans.USERNAME, ""), handler, false);
            try {
                lodingRequest.requestCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntent().putExtras(intent);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(StatisConstans.SUCCESS))) {
            if (getIntent().getStringExtra(StatisConstans.SUCCESS).equals("true")) {
                LodingRequest();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StatisConstans.MSG_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            LodingRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void GPSLocation() {
        if (!TextUtils.isEmpty(textViews.get(3).getText().toString().trim())) {
            return;
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mlocation.getLatitude(), mlocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if (!TextUtils.isEmpty(address.getLocality())) {
                    textViews.get(3).setText(address.getLocality().substring(0, address.getLocality().length() - 1));
                    OutdoorPMRequest outdoorPMRequest = new OutdoorPMRequest(MainActivity.this, sharedPreferencesDB, textViews.get(3).getText().toString().trim(), handler);
                    outdoorPMRequest.requestCode();
                    sharedPreferencesDB.setString(StatisConstans.PROVINCE, address.getAdminArea().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString(StatisConstans.CITY, address.getLocality().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString(StatisConstans.AREA, address.getSubLocality());
                }
            }
        } catch (Exception e) {
            handler.sendEmptyMessage(StatisConstans.LOCATION);
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getIntExtra("s", 0) == 2) {
            LodingRequest();
        }
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    public void request(final String host, final int location) {
        try {
            // 1.连接服务器
            socket = SocketSingle.getInstance(host, location, false);
            Log.d("ConnectionManager", "AbsClient*****已经建立连接");
            protocal = Protocal.getInstance();
            ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
            threadPoolUtils.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    ScoketOFFeON.receMessage(socket, protocal, handler);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimer != null) {
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(StatisConstans.MSG_CYCLIC_TRANSMISSION);
                }
            }, 0, 2000);
        }
    }


    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        sharedPreferencesDB.setString(StatisConstans.IP, "");
        sharedPreferencesDB.setString(StatisConstans.PORT, "");
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    private void initRechclerView() {
        try {
            DataServerConfigRequest dataServerConfigRequest = new DataServerConfigRequest(sharedPreferencesDB, handler, MainActivity.this);
            dataServerConfigRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upData() {
        selectorImageViews.get(0).setVisibility(View.VISIBLE);
        selectorImageViews.get(1).setVisibility(View.GONE);
        if (isOff) {
            if (selectorImageViews.get(0).isChecked()) {
                if (pmAllData.getoNstate().equals("开机")) {
                    isOff = false;
                } else {
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ScoketOFFeON.sendMessage(socket, protocal, mac, selectorImageViews.get(0).isChecked());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                }
            } else {
                if (pmAllData.getoNstate().equals("关机")) {
                    isOff = false;
                } else {
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ScoketOFFeON.sendMessage(socket, protocal, mac, selectorImageViews.get(0).isChecked());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
                }
            }
        } else {
            if (!TextUtils.isEmpty(pmAllData.getoNstate()) && pmAllData.getoNstate().equals("开机")) {
                selectorImageViews.get(0).toggle(true);
            } else {
                selectorImageViews.get(0).toggle(false);
            }
            isOff = false;
        }
        aimPercent = ((double) pmAllData.getIndoorPmThickness() / 225d) * 100d;
        double sensorIndoorTemp = (double) pmAllData.getSensorIndoorTemp() / 10;
        double sensorOutdoorTemp = (double) pmAllData.getSensorOutdoorTemp() / 10;
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", pmAllData.getIndoorPmThickness() + "");

        if (isRevise) {
            if (hzNumeber == pmAllData.getFanFreq()) {
                isRevise = false;
            } else {
                try {
                    ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            hzNumeber = pmAllData.getFanFreq();
            textViews.get(0).setText(pmAllData.getFanFreq() + "");
        }
        if (pmAllData.getCo2Thickness() > 1000) {
            textViews.get(4).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.mred));
        } else {
            textViews.get(4).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        }
        Log.d("xiongzhuo", "跟新数据跟新数据" + pmAllData.getCo2Thickness());
        textViews.get(4).setText(pmAllData.getCo2Thickness() + "");
        textViews.get(6).setText(sensorIndoorTemp + "");
        textViews.get(7).setText(sensorOutdoorTemp + "");
        textViews.get(8).setText(pmAllData.getBlowingRate() + "");
    }

    public void restoreData() {
        selectorImageViews.get(0).setVisibility(View.GONE);
        selectorImageViews.get(1).setVisibility(View.VISIBLE);
        aimPercent = -1d;
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", "--");

        textViews.get(0).setText("--");
        textViews.get(4).setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        textViews.get(4).setText("--");
        textViews.get(6).setText("--");
        textViews.get(7).setText("--");
        textViews.get(8).setText("--");
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
