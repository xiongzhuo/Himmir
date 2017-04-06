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
import com.himmiractivity.liuxing_scoket.Protocal;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.AllDeviceInfoRequest;
import com.himmiractivity.request.DataServerConfigRequest;
import com.himmiractivity.request.LodingRequest;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.ListPopupWindow;
import com.himmiractivity.view.PercentView;
import com.himmiractivity.view.SelectorImageView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

public class MainActivity extends BaseBusActivity {
    @BindView(R.id.tv_ug)
    TextView tvUg;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_out_side)
    TextView tvOutSide;
    @BindView(R.id.tv_co2)
    TextView tvCco2;
    @BindView(R.id.tv_temp_indoor)
    TextView tvTempIndoor;
    @BindView(R.id.tv_temp_out_side)
    TextView tvTempOutSide;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.btn_speed_semll)
    ImageView btnSpeedSemll;
    @BindView(R.id.btn_speed_add)
    ImageView btnSpeedAdd;
    @BindView(R.id.iv_set)
    ImageView ivSet;
    Location mlocation;
    LocationManager mLocationManager;
    @BindView(R.id.percent_view)
    PercentView percentView;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_off_on_controller)
    SelectorImageView ivOffOnController;
    @BindView(R.id.iv_off_line_controller)
    SelectorImageView ivOffLineController;
    @BindView(R.id.tv_hz_valus)
    TextView tvHzValus;
    @BindView(R.id.tv_room)
    TextView tvRoom;
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

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
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
                    // 发送 一个无序广播
                    MainActivity.this.sendBroadcast(intentData);
                    if (isOff) {
                        if (!TextUtils.isEmpty(pmAllData.getoNstate()) && pmAllData.getoNstate().equals("开机")) {
                            ivOffOnController.toggle(true);
                        } else {
                            ivOffOnController.toggle(false);
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
                        tvHzValus.setText(pmAllData.getFanFreq() + "");
                    }

                    tvCco2.setText(pmAllData.getCo2Thickness() + "");
                    tvTempIndoor.setText(sensorIndoorTemp + "℃");
                    tvTempOutSide.setText(sensorOutdoorTemp + "℃");
                    tvSpeed.setText(pmAllData.getBlowingRate() + "");
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
                            while (true) {
                                try {
                                    request(dataServerBean.getDataServerConfig().getPrimary_server_address(), dataServerBean.getDataServerConfig().getPrimary_server_port());
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }));
                    break;
                case StatisConstans.MSG_QQUIP:
                    allUserDerviceBaen = (AllUserDerviceBaen) msg.obj;
                    mac = allUserDerviceBaen.getSpace().get(0).getDevice().getDevice_mac();
                    Log.d("mac", mac);
                    if (allUserDerviceBaen.getSpace() != null && allUserDerviceBaen.getSpace().size() > 0) {
                        space = allUserDerviceBaen.getSpace();
                        list = new ArrayList<>();
                        for (int i = 0; i < space.size(); i++) {
                            list.add(space.get(i).getUserRoom().getRoom_name());
                        }
                    }
                    if (list.size() > 0) {
                        tvRoom.setText(list.get(0).trim());
                        ivOffOnController.setVisibility(View.VISIBLE);
                        ivOffLineController.setVisibility(View.GONE);
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
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getSerializable("userData") != null) {
                userData = (UserData) bundle.getSerializable("userData");
            } else {
                LodingRequest();
            }
        } else {
            LodingRequest();
        }
        App.getInstance().addActivity(this);
        tvCity.setOnClickListener(this);
        btnSpeedSemll.setOnClickListener(this);
        btnSpeedAdd.setOnClickListener(this);
        tvRoom.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivOffOnController.setOnClickListener(this);
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
                    ListPopupWindow popupWindow = new ListPopupWindow(MainActivity.this, tvRoom, list, new ListPopupWindow.downOnclick() {
                        @Override
                        public void onDownItemClick(int position) {
                            mac = allUserDerviceBaen.getSpace().get(position).getDevice().getDevice_mac();
                            tvRoom.setText(list.get(position).trim());
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
                String string = tvHzValus.getText().toString().trim();
                if (!TextUtils.isEmpty(string) && !string.contains("-")) {
                    int hz = Integer.valueOf(tvHzValus.getText().toString().trim());
                    if (hz > 46) {
                        ToastUtils.show(MainActivity.this, "风量最大值", Toast.LENGTH_SHORT);
                        tvHzValus.setText("50");
                    } else {
                        hz = hz + 5;
                        tvHzValus.setText(hz + "");
                        isRevise = true;
                        hzNumeber = hz;
                        ScoketOFFeON.sendBlowingRate(socket, protocal, mac, hzNumeber);
                    }
                } else {
                    ToastUtils.show(MainActivity.this, "设备离线", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btn_speed_semll:
                String str = tvHzValus.getText().toString().trim();
                if (!TextUtils.isEmpty(str) && !str.contains("-")) {
                    int hz = Integer.valueOf(tvHzValus.getText().toString().trim());
                    if (hz < 14) {
                        ToastUtils.show(MainActivity.this, "风量最小值", Toast.LENGTH_SHORT);
                        tvHzValus.setText("10");
                    } else {
                        hz = hz - 5;
                        tvHzValus.setText(hz + "");
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
                ivOffOnController.toggle(!ivOffOnController.isChecked());
                if (protocal == null) {
                    protocal = new Protocal();
                }
                ScoketOFFeON.sendMessage(socket, protocal, mac, ivOffOnController.isChecked());
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
            StringBuilder stringBuilder = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    stringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                stringBuilder.append(address.getLocality()).append("_");
                stringBuilder.append(address.getPostalCode()).append("_");
                stringBuilder.append(address.getCountryCode()).append("_");
                stringBuilder.append(address.getCountryName()).append("_");
                System.out.println(stringBuilder.toString());
                if (!TextUtils.isEmpty(address.getLocality())) {
                    tvCity.setText(address.getLocality().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("province", address.getAdminArea().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("city", address.getLocality().substring(0, address.getLocality().length() - 1));
                    sharedPreferencesDB.setString("area", address.getSubLocality());
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "报错", Toast.LENGTH_LONG).show();
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

    public void request(String host, int location) {
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
            ScoketOFFeON.sendMessage(socket, protocal, mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
//        sharedPreferencesDB.setString("ip", "");
//        sharedPreferencesDB.setString("port", "");
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
}
