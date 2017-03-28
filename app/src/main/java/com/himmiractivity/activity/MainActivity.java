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
import android.telephony.TelephonyManager;
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
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.UiUtil;
import com.himmiractivity.Utils.WifiUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.AllUserDerviceBaen;
import com.himmiractivity.entity.Space;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.AllDeviceInfoRequest;
import com.himmiractivity.request.LodingRequest;
import com.himmiractivity.view.ListPopupWindow;
import com.himmiractivity.view.PercentView;
import com.himmiractivity.view.SelectorImageView;

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
    @BindView(R.id.tv_hz_valus)
    TextView tvHzValus;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    private Double aimPercent = (24d / 225d) * 100d;
    Bundle bundle;
    UserData userData;
    //获取设备
    private List<String> list;
    private List<Space> space;
    public static MainActivity instans;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    userData = (UserData) msg.obj;
                    initRechclerView();
                    break;
                case StatisConstans.MSG_QQUIP:
                    AllUserDerviceBaen allUserDerviceBaen = (AllUserDerviceBaen) msg.obj;
                    if (allUserDerviceBaen.getSpace() != null && allUserDerviceBaen.getSpace().size() > 0) {
                        space = allUserDerviceBaen.getSpace();
                        list = new ArrayList<>();
                        for (int i = 0; i < space.size(); i++) {
                            list.add(space.get(i).getUserRoom().getRoom_name());
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
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
        percentView.setRankText("PM2.5室内", "24");
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
                            ToastUtil.show(MainActivity.this, "你点击了ITEM" + position);
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
                if (!TextUtils.isEmpty(tvHzValus.getText().toString().trim())) {
                    int hz = Integer.valueOf(tvHzValus.getText().toString().trim());
                    if (hz > 46) {
                        ToastUtils.show(MainActivity.this, "风量最大值", Toast.LENGTH_SHORT);
                        tvHzValus.setText("50");
                    } else {
                        tvHzValus.setText((hz + 5) + "");
                    }
                }
                break;
            case R.id.btn_speed_semll:
                if (!TextUtils.isEmpty(tvHzValus.getText().toString().trim())) {
                    int hz = Integer.valueOf(tvHzValus.getText().toString().trim());
                    if (hz < 14) {
                        ToastUtils.show(MainActivity.this, "风量最小值", Toast.LENGTH_SHORT);
                        tvHzValus.setText("10");
                    } else {
                        tvHzValus.setText((hz - 5) + "");
                    }
                }
                break;
            case R.id.iv_set:
//                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
//                threadPoolUtils.execute(new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                }));
                // login
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
                // Permission Denied
                Toast.makeText(MainActivity.this, "请允许才能进行定位", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void GPSLocation() {
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
            // TODO Auto-generated catch block
            Toast.makeText(this, "报错", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void initRechclerView() {
        AllDeviceInfoRequest allDeviceInfoRequest = new AllDeviceInfoRequest(sharedPreferencesDB, this, handler);
        try {
            allDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
