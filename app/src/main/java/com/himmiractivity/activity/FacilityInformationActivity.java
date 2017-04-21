package com.himmiractivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.himmiractivity.App;
import com.himmiractivity.Utils.GpsUtils;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.view.SuperSwipeRefreshLayout;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;

import javax.net.ssl.SSLSocket;

import activity.hamir.com.himmir.R;


public class FacilityInformationActivity extends BaseBusActivity {
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    SSLSocket socket;
    Protocal protocal;


    @Override
    protected int getContentLayoutId() {
        return R.layout.facility_information;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("设备信息");
        initTitleBar();
        swipeRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        View child = LayoutInflater.from(swipeRefreshLayout.getContext())
                .inflate(R.layout.layout_head, null);
        progressBar = (ProgressBar) child.findViewById(R.id.pb_view);
        textView = (TextView) child.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) child.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.down_arrow);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setHeaderView(child);
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                    @Override
                    public void onRefresh() {
                        textView.setText("正在刷新");
                        imageView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        System.out.println("debug:onRefresh");
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                progressBar.setVisibility(View.GONE);
                                System.out.println("debug:stopRefresh");
                            }
                        }, 2000);
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        System.out.println("debug:distance = " + distance);
                        //myAdapter.updateHeaderHeight(distance);
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        textView.setText(enable ? "松开刷新" : "下拉刷新");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setRotation(enable ? 180 : 0);
                    }
                });

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

    public void request(final String host, final int location) {
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
                        ScoketOFFeON.receMessage(socket, protocal, new Handler());
                    }
                }));
            } catch (Exception e) {
                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                threadPoolUtils.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request(host, location);
                    }
                }));
                Log.d("ConnectionManager", "AbsClient*****已经建立连接");
                e.printStackTrace();
            }
        }
    }
}
