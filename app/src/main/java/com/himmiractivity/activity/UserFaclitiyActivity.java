package com.himmiractivity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.himmiractivity.Adapter.UserDerviceRvcAdapter;
import com.himmiractivity.Utils.DividerItemDecoration;
import com.himmiractivity.Utils.GpsUtils;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.entity.UserDerviceBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.UserDerviceRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;

import java.net.Socket;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class UserFaclitiyActivity extends BaseBusActivity implements AlxRefreshLoadMoreRecyclerView.LoadMoreListener, AlxRefreshLoadMoreRecyclerView.OnRefreshListener, UserDerviceRvcAdapter.OnItemClickLitener {
    @BindView(R.id.recyclerview)
    AlxRefreshLoadMoreRecyclerView mRecyclerView;
    UserDerviceRvcAdapter rvcAdapter;
    UserDerviceBean userDerviceBean;
    Protocal protocal;
    Socket socket;
    String mac;
    int onLinePos;
    String shareUserKey;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    if (msg.obj != null) {
                        userDerviceBean = (UserDerviceBean) msg.obj;
                        if (userDerviceBean.getShareUserDevList() != null && userDerviceBean.getShareUserDevList().size() > 0) {
                            if (onLinePos < userDerviceBean.getShareUserDevList().size()) {
                                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                                threadPoolUtils.execute(new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScoketOFFeON.sendMessage(socket, protocal, userDerviceBean.getShareUserDevList().get(onLinePos).getDevice_mac());
                                    }
                                }));
                            }
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(UserFaclitiyActicity.this));
                            rvcAdapter = new UserDerviceRvcAdapter(UserFaclitiyActivity.this, userDerviceBean.getShareUserDevList(), R.layout.list_item, true);
                            rvcAdapter.setPullLoadMoreEnable(false);
                            mRecyclerView.setPullLoadEnable(false);
                            mRecyclerView.setAdapter(rvcAdapter);
                            //设置Item增加、移除动画
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            //添加分割线
                            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                                    UserFaclitiyActivity.this, DividerItemDecoration.HORIZONTAL));
                            rvcAdapter.setOnItemClickLitener(UserFaclitiyActivity.this);
                            mRecyclerView.setLoadMoreListener(UserFaclitiyActivity.this);
                            mRecyclerView.setOnRefreshListener(UserFaclitiyActivity.this);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContentLayoutId() {
        return R.layout.user_faclitiy;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("用户设备");
        initTitleBar();
        registerBoradcastReceiver();
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = sharedPreferencesDB.getString("ip", "");
                String port = sharedPreferencesDB.getString("port", "");
                request(ip, Integer.valueOf(port));
            }
        }));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
                //在线，反之为离线
                if (onLinePos >= userDerviceBean.getShareUserDevList().size())
                    return;
                PmAllData pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
                Log.d("device_mac", "pmAllData.getFanFreq()" + pmAllData.getFanFreq());
                if (pmAllData.getFanFreq() > 9) {
                    userDerviceBean.getShareUserDevList().get(onLinePos).setOnLine(true);
                    rvcAdapter.setmDatas(userDerviceBean.getShareUserDevList());
                }
                onLinePos++;
                if (onLinePos >= userDerviceBean.getShareUserDevList().size())
                    return;
                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                threadPoolUtils.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ScoketOFFeON.sendMessage(socket, protocal, userDerviceBean.getShareUserDevList().get(onLinePos).getDevice_mac());
                    }
                }));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserFaclitiyActivity.this.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLinePos = 0;
        initRechclerView();
    }

    private void registerBoradcastReceiver() {
        IntentFilter filter = new IntentFilter(
                StatisConstans.BROADCAST_HONGREN_SUCC);
        filter.addAction(StatisConstans.BROADCAST_HONGREN_DATA);
        UserFaclitiyActivity.this.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    private void initRechclerView() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra(StatisConstans.SHARE_USER_KEY))) {
            shareUserKey = getIntent().getStringExtra(StatisConstans.SHARE_USER_KEY);
            UserDerviceRequest userDerviceRequest = new UserDerviceRequest(sharedPreferencesDB, this, shareUserKey, mHandler);
            try {
                userDerviceRequest.requestCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (userDerviceBean.getShareUserDevList().get(position).isOnLine()) {
            Intent intent = new Intent();
            mac = userDerviceBean.getShareUserDevList().get(position).getDevice_mac();
            intent.setClass(UserFaclitiyActivity.this, FacilityInformationActivity.class);
            intent.putExtra(StatisConstans.MAC, mac);
            startActivity(intent);
        } else {
            ToastUtil.show(UserFaclitiyActivity.this, "设备离线，不能查看");
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {//模拟两秒网络延迟
            @Override
            public void run() {
                onLinePos = 0;
                initRechclerView();
                mRecyclerView.stopRefresh();
            }
        }, 2000);
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

}
