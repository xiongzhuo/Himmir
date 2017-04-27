package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.Utils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ArticleInfo;
import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.DeviceInfoBean;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.entity.UserRoom;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.request.DataServerConfigRequest;
import com.himmiractivity.request.ReceiveUserDeviceInfoRequest;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;
import com.himmiractivity.view.ChangeAddressDialog;
import com.himmiractivity.view.ClearEditText;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

public class InformationComitActivity extends BaseBusActivity {
    @BindViews({R.id.et_username, R.id.et_full_address, R.id.bt_stroe, R.id.et_nice})
    List<ClearEditText> clearEditTexts;
    @BindView(R.id.btn_choose_room)
    Button btnChooseRoom;
    @BindViews({R.id.tv_unit_type, R.id.tv_manufacturing_date, R.id.tv_city_choise, R.id.tv_install_site, R.id.et_nice})
    List<TextView> textViews;
    DataServerBean dataServerBean;
    DeviceInfoBean deviceInfoBean;
    String server_number;
    String mac;
    String ip;
    public final int port = 8800;
    String provinceSite, citySite, areaSite;
    String provinceChoise, cityChoise, areaChoise;
    ChangeAddressDialog mChangeAddressDialog;
    ChangeAddressDialog dialog;
    public final int ACTIVITY_MORE_MESSAGE = 169;
    List<UserRoom> list;
    private ArticleInfo articleInfo;
    int position = -1;
    Socket socket;
    Protocal protocal;
    public boolean isdeploy = false;
    private Timer mTimer = null;
    ThreadPoolUtils threadPoolUtils;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.MSG_CYCLIC_TRANSMISSION:
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("ConnectionManager", "socketTWO");
                                ScoketOFFeON.sendMessage(socket, protocal, dataServerBean, server_number, mac);
                            } catch (Exception e) {
                                Log.d("ConnectionManager", "socketTWOException");
                                handler.sendEmptyMessage(StatisConstans.FAIL_TWO);
                                e.printStackTrace();
                            }
                        }
                    }));
                    break;
                case StatisConstans.FAIL_TWO:
                    if (!TextUtils.isEmpty(ip)) {
                        threadPoolUtils.execute(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (Utils.isNetworkAvailable(InformationComitActivity.this)) {
                                    try {
                                        if (socket != null) {
                                            socket.close();
                                            socket = null;
                                        }
                                        Log.d("ConnectionManager", "socket");
                                        socket = new Socket(ip, port);
                                        ScoketOFFeON.receMessage(socket, protocal, handler);
                                    } catch (Exception e) {
                                        Log.d("ConnectionManager", "socketException");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }));
                    }
                    break;
                case StatisConstans.CONFIG_REGULAR:
                    dataServerBean = (DataServerBean) msg.obj;
                    threadPoolUtils.execute(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            request(ip, port);
                        }
                    }));
                    break;
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    ImageBean imageBean = (ImageBean) msg.obj;
                    ToastUtil.show(InformationComitActivity.this, imageBean.getSuccess());
                    Intent intent = new Intent(InformationComitActivity.this, MainActivity.class);
                    intent.putExtra(StatisConstans.SUCCESS, "true");
                    startActivity(intent);
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    isdeploy = true;
                    stopTimer();
                    ToastUtil.show(InformationComitActivity.this, "激活配置档成功");
                    break;
                case StatisConstans.MSG_ENABLED_FAILING:
                    isdeploy = false;
                    ToastUtil.show(InformationComitActivity.this, "激活配置档失败");
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.information_comit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("信息确认");
        initTitleBar();
        setRightView("绑定", true);
        threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 10);
        textViews.get(2).setOnClickListener(this);
        textViews.get(3).setOnClickListener(this);
        btnChooseRoom.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            deviceInfoBean = (DeviceInfoBean) bundle.getSerializable(StatisConstans.DEVICE_INFO);
            server_number = bundle.getString(StatisConstans.SERVER_NUMBER);
            mac = bundle.getString(StatisConstans.MAC);
            ip = bundle.getString(StatisConstans.IP);
            Log.d("mac", "mac:" + mac);
            Log.d("mac", "ip:" + ip);
            textViews.get(0).setText(deviceInfoBean.getDeviceInfo().getDevice_type());
            textViews.get(1).setText(deviceInfoBean.getDeviceInfo().getDevice_shipmenttime());

        }
        try {
            DataServerConfigRequest dataServerConfigRequest = new DataServerConfigRequest(sharedPreferencesDB, handler, InformationComitActivity.this);
            dataServerConfigRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        startTimer();
    }

    @Override
    protected void initData() {
        String province = sharedPreferencesDB.getString(StatisConstans.PROVINCE, "");
        String city = sharedPreferencesDB.getString(StatisConstans.CITY, "");
        String area = sharedPreferencesDB.getString(StatisConstans.AREA, "");
        provinceSite = province;
        citySite = city;
        areaSite = area;
        provinceChoise = province;
        cityChoise = city;
        areaChoise = area;
        textViews.get(2).setText(provinceChoise + "省-" + cityChoise + "市-" + areaChoise);
        textViews.get(3).setText(provinceSite + "省-" + citySite + "市-" + areaSite);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_MORE_MESSAGE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().get(StatisConstans.KEY_SAVE_LABLE) != null) {
                Bundle bundle = data.getExtras();
                if (list != null && list.size() > 0) {
                    list.clear();
                }
                position = bundle.getInt("position");
                articleInfo = (ArticleInfo) bundle.get(StatisConstans.KEY_SAVE_LABLE);
                list = articleInfo.getUserRoom();
                String name = list.get(position).getRoom_name();
                for (int i = 0; i < articleInfo.getUserDevsNickname().size(); i++) {
                    if (articleInfo.getUserDevsNickname().get(i).getDevice_nickname().equals(name)) {
                        name = name + "-1";
                    }
                }
                textViews.get(4).setText(name);
                btnChooseRoom.setText(list.get(position).getRoom_name());
            }
        }
    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
//                startActivity(new Intent(InformationComitActivity.this, EditTimeActivity.class));
                break;
            case R.id.btn_right:
                confirmation();
                break;
            case R.id.btn_choose_room:
                Intent intent = new Intent(InformationComitActivity.this,
                        InstallRoomActivity.class);
                Bundle extras = new Bundle();
                if (articleInfo != null) {
                    extras.putSerializable("articleInfo", (Serializable) articleInfo);
                }
                extras.putInt("position", position);
                intent.putExtras(extras);
                InformationComitActivity.this.startActivityForResult(intent,
                        ACTIVITY_MORE_MESSAGE);
                break;
            case R.id.tv_city_choise:
                if (mChangeAddressDialog == null) {
                    mChangeAddressDialog = new ChangeAddressDialog(
                            InformationComitActivity.this);
                }
                mChangeAddressDialog.setAddress(provinceChoise, cityChoise, areaChoise);
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                provinceChoise = province;
                                cityChoise = city;
                                areaChoise = area;
                                textViews.get(2).setText(provinceChoise + "省-" + cityChoise + "市-" + areaChoise);
                            }
                        });
                break;
            case R.id.tv_install_site:
                if (dialog == null) {
                    dialog = new ChangeAddressDialog(
                            InformationComitActivity.this);
                }
                dialog.setAddress(provinceSite, citySite, areaSite);
                dialog.show();
                dialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                provinceSite = province;
                                citySite = city;
                                areaSite = area;
                                textViews.get(3).setText(provinceSite + "省-" + citySite + "市-" + areaSite);
                            }
                        });
                break;
            default:
                break;
        }
    }


    //验证信息是否通过
    private void confirmation() {
        String name = clearEditTexts.get(0).getText().toString().trim();
        String address = clearEditTexts.get(1).getText().toString().trim();
        String stroe = clearEditTexts.get(2).getText().toString().trim();
        String room = btnChooseRoom.getText().toString().trim();
        String device = textViews.get(4).getText().toString().trim();
        if (!isdeploy) {
            ToastUtil.show(this, "请重新激活配置档!");
            return;
        } else if (TextUtils.isEmpty(name)) {
            ToastUtil.show(this, "用户姓名不能为空!");
            return;
        } else if (TextUtils.isEmpty(stroe)) {
            ToastUtil.show(this, "门店名称不能为空!");
            return;
        } else if (TextUtils.isEmpty(address)) {
            ToastUtil.show(this, "详细地址不能为空");
            return;
        } else if (TextUtils.isEmpty(room) || room.contains("选择安装房间")) {
            ToastUtil.show(this, "请选择安装房间");
            return;
        } else if (TextUtils.isEmpty(device)) {
            ToastUtil.show(this, "请输入设备昵称");
            return;
        }
        for (int i = 0; i < articleInfo.getUserDevsNickname().size(); i++) {
            if (articleInfo.getUserDevsNickname().get(i).getDevice_nickname().equals(name)) {
                name = name + "-1";
                textViews.get(4).setText(name);
                ToastUtil.show(InformationComitActivity.this, "设备名称重复");
                return;
            }
        }

        ReceiveUserDeviceInfoRequest receiveUserDeviceInfoRequest = new ReceiveUserDeviceInfoRequest(sharedPreferencesDB, InformationComitActivity.this, handler, room, name, mac, deviceInfoBean.getDeviceInfo().getDevice_sn(), device);
        try {
            receiveUserDeviceInfoRequest.setBuyAddress(provinceChoise + "省", cityChoise + "市", areaChoise, stroe);
            receiveUserDeviceInfoRequest.setinstallAddress(provinceSite + "省", citySite + "市", areaSite, address);
            receiveUserDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopTimer();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void request(String host, int location) {
        try {
            // 1.连接服务器
            socket = new Socket(host, location);
            Log.d("ConnectionManager", "AbsClient*****已经建立连接");
            protocal = new Protocal();
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
            }, 0, 3000);
        }
    }


    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }
}
