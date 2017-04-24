package com.himmiractivity.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.App;
import com.himmiractivity.Utils.GpsUtils;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.circular_progress_bar.CircularProgressBar;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.entity.PmBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.OutdoorPMRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.PercentView;
import com.himmiractivity.view.SuperSwipeRefreshLayout;

import java.net.Socket;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;


public class FacilityInformationActivity extends BaseBusActivity implements SuperSwipeRefreshLayout.OnPullRefreshListener {
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.percent_view)
    PercentView percentView;
    @BindViews({R.id.tv_co2, R.id.tv_temp_indoor, R.id.tv_temp_out_side, R.id.tv_speed, R.id.tv_ug, R.id.tv_city})
    List<TextView> textViews;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private CircularProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    Socket socket;
    Protocal protocal;
    String mac;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_OUTDOOR_PM:
                    PmBean pmBean = (PmBean) msg.obj;
                    circularProgressBar.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                    textViews.get(4).setText(pmBean.getPm25());
                    ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ScoketOFFeON.sendMessage(socket, protocal, mac);
                        }
                    }));
                case StatisConstans.MSG_FAIL_PM:
                    ThreadPoolUtils thread = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                    thread.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ScoketOFFeON.sendMessage(socket, protocal, mac);
                        }
                    }));
                    break;
            }
            return false;
        }
    });


    @Override
    protected int getContentLayoutId() {
        return R.layout.facility_information;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("环境数据");
        initTitleBar();
        registerBoradcastReceiver();
        mac = getIntent().getStringExtra(StatisConstans.MAC);
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = sharedPreferencesDB.getString("ip", "");
                String port = sharedPreferencesDB.getString("port", "");
                request(ip, Integer.valueOf(port));
            }
        }));
        View child = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.layout_head, null);
        progressBar = (CircularProgressBar) child.findViewById(R.id.xlistview_header_progressbar);
        textView = (TextView) child.findViewById(R.id.xlistview_header_hint_textview);
        textView.setText("下拉刷新");
        imageView = (ImageView) child.findViewById(R.id.xlistview_header_arrow);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.xlistview_arrow);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setHeaderView(child);
        swipeRefreshLayout.setOnPullRefreshListener(this);
        try {
            String city = sharedPreferencesDB.getString(StatisConstans.CITY, "");
            textViews.get(5).setText(city);
            OutdoorPMRequest outdoorPMRequest = new OutdoorPMRequest(FacilityInformationActivity.this, sharedPreferencesDB, city, handler);
            outdoorPMRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void request(String host, int location) {
        while (GpsUtils.isServerClose(socket)) {
            try {
                // 1.连接服务器
                socket = SocketSingle.getInstance(host, location);
                Log.d("ConnectionManager", "AbsClient*****已经建立连接");
                protocal = Protocal.getInstance();
            } catch (Exception e) {
                request(host, location);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        textView.setText("正在加载.......");
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        System.out.println("debug:onRefresh");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                System.out.println("debug:stopRefresh");
                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                threadPoolUtils.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ScoketOFFeON.sendMessage(socket, protocal, mac);
                    }
                }));
            }
        }, 600);
    }

    @Override
    public void onPullDistance(int distance) {
        System.out.println("debug:distance = " + distance);
        //myAdapter.updateHeaderHeight(distance);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
                PmAllData pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
                if (pmAllData.getFanFreq() > 9) {
                    upData(pmAllData);
                } else {
                    restoreData();
                }
            }
        }
    };

    public void upData(PmAllData pmAllData) {
        double aimPercent = ((double) pmAllData.getIndoorPmThickness() / 225d) * 100d;
        double sensorIndoorTemp = (double) pmAllData.getSensorIndoorTemp() / 10;
        double sensorOutdoorTemp = (double) pmAllData.getSensorOutdoorTemp() / 10;
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", pmAllData.getIndoorPmThickness() + "");

        if (pmAllData.getCo2Thickness() > 1000) {
            textViews.get(1).setTextColor(ContextCompat.getColor(FacilityInformationActivity.this, R.color.mred));
        } else {
            textViews.get(1).setTextColor(ContextCompat.getColor(FacilityInformationActivity.this, R.color.green));
        }
        textViews.get(0).setText(pmAllData.getFanFreq() + "");
        textViews.get(0).setText(pmAllData.getCo2Thickness() + "");
        textViews.get(1).setText(sensorIndoorTemp + "");
        textViews.get(2).setText(sensorOutdoorTemp + "");
        textViews.get(3).setText(pmAllData.getBlowingRate() + "");
    }

    public void restoreData() {
        double aimPercent = -1d;
        percentView.setAngel(aimPercent);
        percentView.setRankText("PM2.5室内", "--");

        textViews.get(0).setTextColor(ContextCompat.getColor(FacilityInformationActivity.this, R.color.green));
        textViews.get(0).setText("--");
        textViews.get(1).setText("--");
        textViews.get(2).setText("--");
        textViews.get(3).setText("--");
    }

    private void registerBoradcastReceiver() {
        IntentFilter filter = new IntentFilter(
                StatisConstans.BROADCAST_HONGREN_SUCC);
        filter.addAction(StatisConstans.BROADCAST_HONGREN_DATA);
        FacilityInformationActivity.this.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onPullEnable(boolean enable) {
        textView.setText(enable ? "松开刷新数据" : "下拉刷新");
        imageView.setVisibility(View.VISIBLE);
        imageView.setRotation(enable ? 180 : 0);
    }
}
