package com.himmiractivity.activity;


import android.Manifest;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.himmiractivity.Utils.WifiUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.AllUserDerviceBaen;
import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.entity.Space;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.AllDeviceInfoRequest;
import com.himmiractivity.request.DataServerConfigRequest;
import com.himmiractivity.request.LodingRequest;
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

public class MainActivity extends BaseBusActivity {
    TextView tvUg, tvCity, tvCco2, tvOutSide, tvTempIndoor, tvTempOutSide, tvSpeed;
    Location mlocation;
    LocationManager mLocationManager;
    @BindView(R.id.percent_view)
    PercentView percentView;
    @BindViews({R.id.iv_add, R.id.iv_set, R.id.btn_speed_semll, R.id.btn_speed_add})
    List<ImageView> imageViews;
    @BindViews({R.id.iv_off_on_controller, R.id.iv_off_line_controller})
    List<SelectorImageView> selectorImageViews;
    @BindViews({R.id.tv_hz_valus, R.id.tv_room})
    List<TextView> textViews;
    private Double aimPercent = (0d / 225d) * 100d;
    Bundle bundle;
    UserData userData;
    //获取设备
    private List<String> list;
    private List<Space> space;
    public static MainActivity instans;
    Socket socket;
    Protocal protocal;
    DataServerBean dataServerBean;
    String mac;
    AllUserDerviceBaen allUserDerviceBaen;
    PmAllData pmAllData;
    int hzNumeber;
    boolean isRevise = false;
    boolean isOff = true;
    boolean isFrist = true;
    private Timer mTimer = null;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_CYCLIC_TRANSMISSION:
                    ScoketOFFeON.sendMessage(socket, protocal, mac);
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    // 发送 一个无序广播
                    MainActivity.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_SUCC));
                    break;
                case StatisConstans.MSG_ENABLED_FAILING:
                    // 发送 一个无序广播
                    MainActivity.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_KILL));
                    break;
                case StatisConstans.MSG_QUEST_SERVER:
                    pmAllData = (PmAllData) msg.obj;
                    Intent intentData = new Intent();
                    intentData.setAction(StatisConstans.BROADCAST_HONGREN_DATA);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pm_all_data", pmAllData);
                    // 要发送的内容
                    intentData.putExtras(bundle);
                    MainActivity.this.sendBroadcast(intentData);
                    if (pmAllData.getFanFreq() > 9) {
                        upData();
                    } else {
                        restoreData();
                    }
                    break;
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    userData = (UserData) msg.obj;
                    initRechclerView();
                    break;
                case StatisConstans.CONFIG_REGULAR:
                    dataServerBean = (DataServerBean) msg.obj;
                    sharedPreferencesDB.setString("ip", dataServerBean.getDataServerConfig().getPrimary_server_address());
                    sharedPreferencesDB.setString("port", dataServerBean.getDataServerConfig().getPrimary_server_port() + "");
                    ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(dataServerBean.getDataServerConfig().getPrimary_server_address(), dataServerBean.getDataServerConfig().getPrimary_server_port());
                        }
                    }));
                    startTimer();
                    break;
                case StatisConstans.MSG_QQUIP:
                    allUserDerviceBaen = (AllUserDerviceBaen) msg.obj;
                    if (allUserDerviceBaen.getSpace() != null && allUserDerviceBaen.getSpace().size() > 0) {
                        space = allUserDerviceBaen.getSpace();
                        list = new ArrayList<>();
                        for (int i = 0; i < space.size(); i++) {
                            if (space.get(i).getUserRoom() != null) {
                                list.add(space.get(i).getUserRoom().getRoom_name());
                            } else {
                                list.add("无名");
                            }
                        }
                        if (list.size() > 0) {
                            if (isFrist) {
                                mac = allUserDerviceBaen.getSpace().get(0).getDevice().getDevice_mac();
                                textViews.get(1).setText(list.get(0).trim());
                                isFrist = false;
                            }
                        }
                    }
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
        instans = this;
        App.getInstance().addActivity(this);
        tvUg = (TextView) findViewById(R.id.tv_ug);
        tvCco2 = (TextView) findViewById(R.id.tv_co2);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvOutSide = (TextView) findViewById(R.id.tv_out_side);
        tvTempIndoor = (TextView) findViewById(R.id.tv_temp_indoor);
        tvTempOutSide = (TextView) findViewById(R.id.tv_temp_out_side);
        tvSpeed = (TextView) findViewById(R.id.tv_speed);
        tvCity.setOnClickListener(this);
        imageViews.get(2).setOnClickListener(this);
        imageViews.get(3).setOnClickListener(this);
        textViews.get(1).setOnClickListener(this);
        imageViews.get(1).setOnClickListener(this);
        imageViews.get(0).setOnClickListener(this);
        selectorImageViews.get(0).setOnClickListener(this);
        Log.i("aimPercent", aimPercent + "=-------");
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", "--");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    StatisConstans.MY_PERMISSIONS_REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    StatisConstans.MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            try {
                mlocation = getLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_room:
                if (list != null && list.size() > 0) {
                    ListPopupWindow popupWindow = new ListPopupWindow(MainActivity.this, textViews.get(1), list, new ListPopupWindow.downOnclick() {
                        @Override
                        public void onDownItemClick(int position) {
                            mac = allUserDerviceBaen.getSpace().get(position).getDevice().getDevice_mac();
                            textViews.get(1).setText(list.get(position).trim());
                            stopTimer();
                            startTimer();
                        }
                    });
                } else {
                    ToastUtil.show(MainActivity.this, "暂无房间");
                }
                break;
            case R.id.iv_add:
                //判断是否开启了WI-FI
                if (WifiUtils.isWifiConnected(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, DeployWifiActivity.class));
                } else {
                    ToastUtils.show(MainActivity.this, "设备WI-FI未开启", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btn_speed_add:
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
                        ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                    }
                } else {
                    ToastUtils.show(MainActivity.this, "设备离线", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btn_speed_semll:
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
                        ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                    }
                } else {
                    ToastUtils.show(MainActivity.this, "设备离线", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.iv_set:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                Bundle bundle = new Bundle();
                if (userData != null) {
                    bundle.putSerializable("userData", userData);
                }
                intent.putExtras(bundle);
                startActivityForResult(intent, StatisConstans.MSG_IMAGE_REQUEST);
                break;
            case R.id.iv_off_on_controller:
                selectorImageViews.get(0).toggle(!selectorImageViews.get(0).isChecked());
                if (protocal == null) {
                    protocal = new Protocal();
                }
                ScoketOFFeON.sendMessage(socket, protocal, mac, selectorImageViews.get(0).isChecked());
                break;
            default:
                break;
        }
    }

    public Location getLocation() throws Exception {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mlocation == null) {
            mlocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (GpsUtils.isGpsEnable(this)) {
            GPSLocation();
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
            GPSLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void LodingRequest() {
        if (TextUtils.isEmpty(sharedPreferencesDB.getString("userpwd", "")) || TextUtils.isEmpty(sharedPreferencesDB.getString("username", ""))) {
            startActivity(new Intent(this, LodingActivity.class));
            finish();
        } else {
            LodingRequest lodingRequest = new LodingRequest(sharedPreferencesDB, this, sharedPreferencesDB.getString("userpwd", ""), sharedPreferencesDB.getString("username", ""), sharedPreferencesDB.getString("userDeviceUuid", ""), handler);
            try {
                lodingRequest.requestCode();
            } catch (Exception e) {
                e.printStackTrace();
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
        if (requestCode == StatisConstans.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    mlocation = getLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "请允许才能进行定位", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void GPSLocation() {
        if (!TextUtils.isEmpty(tvCity.getText().toString().trim())) {
            return;
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(mlocation.getLatitude(), mlocation.getLongitude(), 1);
//            StringBuilder stringBuilder = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
//                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    stringBuilder.append(address.getAddressLine(i)).append("\n");
//                }
//                stringBuilder.append(address.getLocality()).append("_");
//                stringBuilder.append(address.getPostalCode()).append("_");
//                stringBuilder.append(address.getCountryCode()).append("_");
//                stringBuilder.append(address.getCountryName()).append("_");
//                System.out.println(stringBuilder.toString());
                if (!TextUtils.isEmpty(address.getLocality())) {
                    tvCity.setText(address.getLocality().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("province", address.getAdminArea().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("city", address.getLocality().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("area", address.getSubLocality());
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "定位失败", Toast.LENGTH_LONG).show();
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
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getSerializable("userData") != null) {
                userData = (UserData) bundle.getSerializable("userData");
                initRechclerView();
            } else {
                LodingRequest();
            }
        } else {
            LodingRequest();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    public void request(String host, int location) {
        while (GpsUtils.isServerClose(socket)) {
            try {
                // 1.连接服务器
                socket = SocketSingle.getInstance(host, location);
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
        sharedPreferencesDB.setString("ip", "");
        sharedPreferencesDB.setString("port", "");
//        try {
//            if (socket != null) {
//                socket.close();
//                socket = null;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        super.onDestroy();
    }


    private void initRechclerView() {
        AllDeviceInfoRequest allDeviceInfoRequest = new AllDeviceInfoRequest(sharedPreferencesDB, this, handler);
        try {
            allDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
            }
        } else {
            hzNumeber = pmAllData.getFanFreq();
            textViews.get(0).setText(pmAllData.getFanFreq() + "");
        }
        if (pmAllData.getCo2Thickness() > 1000) {
            tvCco2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.mred));
        } else {
            tvCco2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        }
        Log.d("xiongzhuo", "跟新数据跟新数据" + pmAllData.getCo2Thickness());
        tvCco2.setText(pmAllData.getCo2Thickness() + "");
        tvTempIndoor.setText(sensorIndoorTemp + "℃");
        tvTempOutSide.setText(sensorOutdoorTemp + "℃");
        tvSpeed.setText(pmAllData.getBlowingRate() + "");
    }

    public void restoreData() {
        selectorImageViews.get(0).setVisibility(View.GONE);
        selectorImageViews.get(1).setVisibility(View.VISIBLE);
        aimPercent = (0d / 225d) * 100d;
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", "--");

        textViews.get(0).setText("--");
        tvCco2.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        tvCco2.setText("--");
        tvTempIndoor.setText("--℃");
        tvTempOutSide.setText("--℃");
        tvSpeed.setText("--");
    }
}
