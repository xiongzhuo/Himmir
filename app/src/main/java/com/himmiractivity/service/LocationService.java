/**
 * Copyright (C) 2014-2015 Imtoy Technologies. All rights reserved.
 *
 * @charset UTF-8
 * @author xiong_it
 */
package com.himmiractivity.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.himmiractivity.entity.Common;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationService extends IntentService implements LocationListener {
    private static final String TAG = "LocationService";
    private static final String SERVICE_NAME = "LocationService";

    private static final long MIN_TIME = 1000l;
    private static final float MIN_DISTANCE = 10f;

    private LocationManager locationManager;

    public LocationService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null || locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            Log.i(TAG, "正在定位");
            /*
             * 进行定位
			 * provider:用于定位的locationProvider字符串
			 * minTime:时间更新间隔，单位：ms
			 * minDistance:位置刷新距离，单位：m
			 * listener:用于定位更新的监听者locationListener
			 */
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            Log.i(TAG, "无法定位");
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            Toast.makeText(this, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //得到纬度
//		double latitude = location.getLatitude();
        double latitude = 29.33813;
        Log.i(TAG, "纬度 =" + latitude);
        //得到经度
//		double longitude = location.getLongitude();
        double longitude = 117.190753;
        Log.i(TAG, "经度 =" + longitude);

        List<Address> locationList = getLocationList(latitude, longitude);
        if (!locationList.isEmpty()) {
            Address address = locationList.get(0);
            Log.i(TAG, "address =" + address);
            String countryName = address.getCountryName();
            Log.i(TAG, "countryName = " + countryName);
            String countryCode = address.getCountryCode();
            Log.i(TAG, "countryCode = " + countryCode);
            String locality = address.getLocality();
            Log.i(TAG, "locality = " + locality);
            for (int i = 0; address.getAddressLine(i) != null; i++) {
                String addressLine = address.getAddressLine(i);
                Log.i(TAG, "addressLine = " + addressLine);
            }
        }
        // 通知Activity
        Intent intent = new Intent();
        intent.setAction(Common.LOCATION_ACTION);
        intent.putExtra(Common.LOCATION, location.toString());
        sendBroadcast(intent);
        Log.i(TAG, "sendBroadcast");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    private List<Address> getLocationList(double latitude, double longitude) {
        Log.i(TAG, "getLocationList");
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationList;
    }

}
