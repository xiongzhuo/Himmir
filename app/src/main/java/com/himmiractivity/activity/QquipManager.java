package com.himmiractivity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himmiractivity.Adapter.RvcAdapter;
import com.himmiractivity.Utils.DividerItemDecoration;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.Utils.Utils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.AllUserDerviceBaen;
import com.himmiractivity.entity.FirstEvent;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.AllDeviceInfoRequest;
import com.himmiractivity.request.DeleteDeviceRequest;
import com.himmiractivity.request.ModifyRoomNameRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.AlxRefreshLoadMoreRecyclerView;

import java.net.Socket;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 设备管理
 */

public class QquipManager extends BaseBusActivity implements AlxRefreshLoadMoreRecyclerView.LoadMoreListener, AlxRefreshLoadMoreRecyclerView.OnRefreshListener, RvcAdapter.OnItemClickLitener {
    @BindView(R.id.rv_list)
    AlxRefreshLoadMoreRecyclerView mRecyclerView;
    RvcAdapter rvcAdapter;
    AllUserDerviceBaen allUserDerviceBaen;
    AlertDialog alertDialog;
    private int navigationHeight;
    @BindView(R.id.btn_add_aqu)
    Button btnAddAqu;
    private int deletePosition = -1;
    String name;
    Protocal protocal;
    Socket socket;
    String mac;
    int onLinePos;
    String ip;
    String port;
    ThreadPoolUtils threadPoolUtils;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.FAIL:
                    ToastUtils.show(QquipManager.this, "校时失败，请检查网络连接", Toast.LENGTH_LONG);
                    break;
                case StatisConstans.MSG_DELETE:
                    ImageBean destr = (ImageBean) msg.obj;
                    if (deletePosition != -1) {
                        setResult(Activity.RESULT_OK);
                        rvcAdapter.removeData(deletePosition);
                        alertDialog.dismiss();
                    }
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    EventBus.getDefault().post(new FirstEvent("操作成功"));
                    // 发送 一个无序广播
//                    QquipManager.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_SUCC));
                    break;
                case StatisConstans.MSG_ENABLED_FAILING:
                    EventBus.getDefault().post(new FirstEvent("操作失败"));
                    // 发送 一个无序广播
//                    QquipManager.this.sendBroadcast(new Intent(StatisConstans.BROADCAST_HONGREN_KILL));
                    break;
                case StatisConstans.MSG_QUEST_SERVER:
                    PmAllData pmAllData = (PmAllData) msg.obj;
//                    Intent intentData = new Intent();
//                    intentData.setAction(StatisConstans.BROADCAST_HONGREN_DATA);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(StatisConstans.PM_ALL_DATA, pmAllData);
//                    // 要发送的内容
//                    intentData.putExtras(bundle);
//                    QquipManager.this.sendBroadcast(intentData);
                    EventBus.getDefault().post(pmAllData);
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
                                sendSocket();
                            }
//                    mRecyclerView.setLayoutManager(new LinearLayoutManager(QquipManager.this));
                            rvcAdapter = new RvcAdapter(QquipManager.this, allUserDerviceBaen.getSpace(), R.layout.list_item, true);
                            rvcAdapter.setPullLoadMoreEnable(false);
                            mRecyclerView.setPullLoadEnable(false);
                            mRecyclerView.setAdapter(rvcAdapter);
                            //设置Item增加、移除动画
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            //添加分割线
                            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                                    QquipManager.this, DividerItemDecoration.HORIZONTAL));
                            rvcAdapter.setOnItemClickLitener(QquipManager.this);
                            mRecyclerView.setLoadMoreListener(QquipManager.this);
                            mRecyclerView.setOnRefreshListener(QquipManager.this);
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
        return R.layout.quip_manager;
    }

    @Override
    @Subscribe
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("设备管理");
        initTitleBar();
//        registerBoradcastReceiver();
        //注册
        EventBus.getDefault().register(this);
        ip = sharedPreferencesDB.getString("ip", "");
        port = sharedPreferencesDB.getString("port", "");
        threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 10);
        if (!TextUtils.isEmpty(ip)) {
            threadPoolUtils.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    request(ip, Integer.valueOf(port));
                }
            }));
        }
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        btnAddAqu.setOnClickListener(this);
    }

//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_SUCC)) {
//                ToastUtil.show(QquipManager.this, "操作成功");
//            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_KILL)) {
//                ToastUtil.show(QquipManager.this, "操作失败");
//            }
//            else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
//                if (allUserDerviceBaen.getSpace() != null) {
//                    //在线，反之为离线
//                    if (onLinePos >= allUserDerviceBaen.getSpace().size())
//                        return;
//                    PmAllData pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
//                    Log.d("device_mac", "pmAllData.getFanFreq()" + pmAllData.getFanFreq());
//                    if (pmAllData.getFanFreq() > 9) {
//                        allUserDerviceBaen.getSpace().get(onLinePos).setOnLine(true);
//                        rvcAdapter.setmDatas(allUserDerviceBaen.getSpace());
//                    }
//                    onLinePos++;
//                    if (onLinePos >= allUserDerviceBaen.getSpace().size())
//                        return;
//                    Log.d("device_mac", allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac() + "onLinePos:" + onLinePos);
//                    threadPoolUtils.execute(new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }));
//                }
//            }
//}
//    };

    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        String msg = event.getMsg();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onEventMainThread(PmAllData pmAllData) {
        if (allUserDerviceBaen.getSpace() != null) {
            //在线，反之为离线
            if (onLinePos >= allUserDerviceBaen.getSpace().size())
                return;
//            PmAllData pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
            Log.d("device_mac", "pmAllData.getFanFreq()" + pmAllData.getFanFreq());
            if (pmAllData.getFanFreq() > 9) {
                allUserDerviceBaen.getSpace().get(onLinePos).setOnLine(true);
                rvcAdapter.setmDatas(allUserDerviceBaen.getSpace());
            }
            onLinePos++;
            if (onLinePos >= allUserDerviceBaen.getSpace().size())
                return;
            Log.d("device_mac", allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac() + "onLinePos:" + onLinePos);
            threadPoolUtils.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        QquipManager.this.unregisterReceiver(mBroadcastReceiver);
        //注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLinePos = 0;
        initRechclerView();
    }

//    private void registerBoradcastReceiver() {
//        IntentFilter filter = new IntentFilter(
//                StatisConstans.BROADCAST_HONGREN_SUCC);
//        filter.addAction(StatisConstans.BROADCAST_HONGREN_KILL);
//        filter.addAction(StatisConstans.BROADCAST_HONGREN_DATA);
//        QquipManager.this.registerReceiver(mBroadcastReceiver, filter);
//    }

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
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ScoketOFFeON.sendTimingMessage(socket, protocal, mac);
                            } catch (Exception e) {
                                mHandler.sendEmptyMessage(StatisConstans.FAIL);
                                e.printStackTrace();
                            }
                        }
                    }));

                } else {
                    ToastUtil.show(QquipManager.this, "校时失败，请检查网络连接");
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
            case R.id.tv_mode:
                Intent intentMode = new Intent(this, IntelligenceModeActivity.class);
                intentMode.putExtra("mac", mac);
                startActivity(intentMode);
                alertDialog.dismiss();
                break;
            case R.id.tv_rename:
                showinputPassdialog("请输入新的名称", "", "取消", "确定", "rename");
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

    private void openAlertDialog(int pos) {
        deletePosition = pos;
        //防止重复按按钮
        if (alertDialog != null && alertDialog.isShowing()) {
            return;
        }
        //alertDialog
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_popupwindow, null);
        alertDialog = new AlertDialog.Builder(QquipManager.this, R.style.Theme_Light_Dialog)
                .create();
        setOnPopupViewClick(view);
        alertDialog.show();
        Window w = alertDialog.getWindow();
        w.setWindowAnimations(R.style.AnimBottom);
        w.setGravity(Gravity.BOTTOM);
        w.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = w.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        w.setAttributes(lp);
        w.setContentView(view);
//        w.setLayout(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel, tv_rename, tv_mode, tv_delete;
        LinearLayout ll_dimis = (LinearLayout) view.findViewById(R.id.ll_dimis);
        tv_pick_phone = (TextView) view.findViewById(R.id.tv_pick_phone);
        tv_pick_zone = (TextView) view.findViewById(R.id.tv_pick_zone);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_rename = (TextView) view.findViewById(R.id.tv_rename);
        tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        tv_mode = (TextView) view.findViewById(R.id.tv_mode);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showinputPassdialog("您确定要删除该设备吗？", "删除后将无法控制设备", "暂不", "删除", "delete");
                alertDialog.dismiss();
            }
        });
        ll_dimis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tv_mode.setOnClickListener(this);
        tv_pick_phone.setOnClickListener(this);
        tv_pick_zone.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_rename.setOnClickListener(this);
    }

    // 重命名
    public void showinputPassdialog(String head, String body, String no, String yes, final String choose) {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_renname, null);
        TextView tvHead = (TextView) view.findViewById(R.id.tv_head);
        Button btnComit = (Button) view.findViewById(R.id.btn_comit);
        Button btnCanel = (Button) view.findViewById(R.id.btn_canel);
        final EditText etNewName = (EditText) view.findViewById(R.id.et_new_name);
        TextView tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        if (choose.equals("rename")) {
            tv_hint.setVisibility(View.GONE);
            etNewName.setVisibility(View.VISIBLE);
            btnComit.setTextColor(ContextCompat.getColor(this, R.color.royalblue));
            btnCanel.setTextColor(ContextCompat.getColor(this, R.color.royalblue));

        } else {
            tv_hint.setVisibility(View.VISIBLE);
            etNewName.setVisibility(View.GONE);
            tv_hint.setText(body);
            btnComit.setTextColor(ContextCompat.getColor(this, R.color.black));
            btnCanel.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        tvHead.setText(head);
        btnCanel.setText(no);
        btnComit.setText(yes);
        final AlertDialog dialog = new AlertDialog.Builder(QquipManager.this)
                .create();
        Window w = dialog.getWindow();
        w.setWindowAnimations(R.style.mystyle1);
        dialog.show();
        dialog.getWindow().setContentView(view);
        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //加上下面这一行弹出对话框时软键盘随之弹出
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        dialog.getWindow().setLayout((int) (width * 0.8),
                LayoutParams.WRAP_CONTENT);
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
        btnComit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etNewName.getText().toString().trim();
                if (choose.equals("rename")) {
                    if (TextUtils.isEmpty(name)) {
                        ToastUtils.show(QquipManager.this, "输入的姓名不能为空", Toast.LENGTH_LONG);
                        return;
                    }
                    for (int i = 0; i < allUserDerviceBaen.getSpace().size(); i++) {
                        if (name.equals(allUserDerviceBaen.getSpace().get(i).getUserdevice().getDevice_nickname())) {
                            name = name + "-1";
                        }
                    }
                    try {
                        ModifyRoomNameRequest modifyRoomNameRequest = new ModifyRoomNameRequest(QquipManager.this, sharedPreferencesDB, name, allUserDerviceBaen.getSpace().get(deletePosition).getUserdevice().getDevice_sn(), mHandler);
                        modifyRoomNameRequest.requestCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        DeleteDeviceRequest deleteDeviceRequest = new DeleteDeviceRequest(QquipManager.this, sharedPreferencesDB, allUserDerviceBaen.getSpace().get(deletePosition).getBuyAddress().getDevice_sn() + "", allUserDerviceBaen.getSpace().get(deletePosition).getUserRoom().getUser_room_id() + "", mHandler);
                        deleteDeviceRequest.requestCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("position" + position);
        mac = allUserDerviceBaen.getSpace().get(position).getDevice().getDevice_mac();
        openAlertDialog(position);
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
        try {
            // 1.连接服务器
            socket = SocketSingle.getInstance(host, location, false);
            Log.d("ConnectionManager", "AbsClient*****已经建立连接");
            protocal = Protocal.getInstance();
            ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
            threadPoolUtils.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    ScoketOFFeON.receMessage(socket, protocal, mHandler);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSocket() {
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
                } catch (Exception e) {
                    if (Utils.isNetworkAvailable(QquipManager.this)) {
                        try {
                            socket.close();
                            socket = null;
                            socket = SocketSingle.getInstance(ip, Integer.valueOf(port), true);
                            threadPoolUtils.execute(new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ScoketOFFeON.receMessage(socket, protocal, mHandler);
                                }
                            }));
                            ScoketOFFeON.sendMessage(socket, protocal, allUserDerviceBaen.getSpace().get(onLinePos).getDevice().getDevice_mac());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                }
            }
        }));
    }
}