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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himmiractivity.Adapter.RvcAdapter;
import com.himmiractivity.Adapter.UserFaclitiyAdapter;
import com.himmiractivity.App;
import com.himmiractivity.Utils.DividerItemDecoration;
import com.himmiractivity.Utils.GpsUtils;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.AllUserDerviceBaen;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.AllDeviceInfoRequest;
import com.himmiractivity.request.DeleteDeviceRequest;
import com.himmiractivity.request.ManagerShardRequest;
import com.himmiractivity.request.ModifyRoomNameRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;

import java.net.Socket;

import activity.hamir.com.himmir.R;
import butterknife.BindView;


public class UserFaclitiyActivity extends BaseBusActivity implements AlxRefreshLoadMoreRecyclerView.LoadMoreListener, AlxRefreshLoadMoreRecyclerView.OnRefreshListener, RvcAdapter.OnItemClickLitener {
    @BindView(R.id.rv_list)
    AlxRefreshLoadMoreRecyclerView mRecyclerView;
    RvcAdapter rvcAdapter;
    AllUserDerviceBaen allUserDerviceBaen;
    AlertDialog alertDialog;
    @BindView(R.id.btn_add_aqu)
    Button btnAddAqu;
    private int deletePosition = -1;
    String name;
    Protocal protocal;
    Socket socket;
    String mac;
    int onLinePos;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_DELETE:
                    ImageBean destr = (ImageBean) msg.obj;
                    if (deletePosition != -1) {
                        setResult(Activity.RESULT_OK);
                        rvcAdapter.removeData(deletePosition);
                        alertDialog.dismiss();
                    }
                    break;
                case StatisConstans.MSG_MODIFY_NAME:
                    ImageBean mostr = (ImageBean) msg.obj;
                    if (deletePosition != -1) {
                        setResult(Activity.RESULT_OK);
                        allUserDerviceBaen.getSpace().get(deletePosition).getUserdevice().setDevice_nickname(name);
                        rvcAdapter.setmDatas(allUserDerviceBaen.getSpace());
                    }
                    break;
                //成功
                case StatisConstans.MSG_QQUIP:
                    if (msg.obj != null) {
                        allUserDerviceBaen = (AllUserDerviceBaen) msg.obj;
                        if (allUserDerviceBaen.getSpace() != null && allUserDerviceBaen.getSpace().size() > 0) {
                            if (onLinePos < allUserDerviceBaen.getSpace().size()) {
                                Log.d("device_mac", allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac() + "onLinePos:" + onLinePos);
                                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                                threadPoolUtils.execute(new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
                                    }
                                }));
                            }
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(UserFaclitiyActicity.this));
                            rvcAdapter = new RvcAdapter(UserFaclitiyActivity.this, allUserDerviceBaen.getSpace(), R.layout.list_item, true);
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
        setMidTxt("设备管理");
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
        btnAddAqu.setOnClickListener(this);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_SUCC)) {
                ToastUtil.show(UserFaclitiyActivity.this, "操作成功");
            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_KILL)) {
                ToastUtil.show(UserFaclitiyActivity.this, "操作失败");
            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
                //在线，反之为离线
                if (onLinePos >= allUserDerviceBaen.getSpace().size())
                    return;
                PmAllData pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
                Log.d("device_mac", "pmAllData.getFanFreq()" + pmAllData.getFanFreq());
                if (pmAllData.getFanFreq() > 9) {
                    allUserDerviceBaen.getSpace().get(onLinePos).setOnLine(true);
                    rvcAdapter.setmDatas(allUserDerviceBaen.getSpace());
                }
                onLinePos++;
                if (onLinePos >= allUserDerviceBaen.getSpace().size())
                    return;
                Log.d("device_mac", allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac() + "onLinePos:" + onLinePos);
                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                threadPoolUtils.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
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
        filter.addAction(StatisConstans.BROADCAST_HONGREN_KILL);
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
            case R.id.btn_add_aqu:
                startActivity(new Intent(this, DeployWifiActivity.class));
                break;
            case R.id.tv_pick_phone:
//                startActivity(new Intent(this, EditTimeActivity.class));
                Log.d("device_mac", mac);
                if (allUserDerviceBaen.getSpace().get(deletePosition).isOnLine()) {
                    ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ScoketOFFeON.sendTimingMessage(socket, protocal, mac);
                        }
                    }));

                } else {
                    ToastUtil.show(UserFaclitiyActivity.this, "校时失败，请检查网络连接");
                }
                alertDialog.dismiss();
                break;
            case R.id.tv_pick_zone:
                Intent intent = new Intent(this, FixedTimeActivity.class);
                intent.putExtra("mac", mac);
                startActivity(intent);
                alertDialog.dismiss();
                break;
            case R.id.tv_cancel:
                alertDialog.dismiss();
                break;
            default:
                break;
        }
    }

    private void initRechclerView() {
        AllDeviceInfoRequest allDeviceInfoRequest = new AllDeviceInfoRequest(sharedPreferencesDB, this, mHandler);
        try {
            allDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("position" + position);
        mac = allUserDerviceBaen.getSpace().get(position).getDevice().getDevice_mac();
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
