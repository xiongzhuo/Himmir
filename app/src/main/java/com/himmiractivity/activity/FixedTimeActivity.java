package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.himmiractivity.App;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.circular_progress_bar.CircularProgressBar;
import com.himmiractivity.entity.FirstEvent;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mining.app.zxing.ScoketOFFeON;
import com.himmiractivity.service.Protocal;
import com.himmiractivity.util.ThreadPoolUtils;

import java.net.Socket;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class FixedTimeActivity extends BaseBusActivity {
    @BindView(R.id.progress)
    CircularProgressBar progressBar;
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindViews({R.id.iv_time_one, R.id.iv_time_two, R.id.iv_time_three})
    List<ImageView> imageViews;
    @BindViews({R.id.cb_time_one, R.id.cb_time_two, R.id.cb_time_three})
    List<CheckBox> checkBoxes;
    @BindViews({R.id.rb_allday, R.id.rb_once, R.id.rb_weekday})
    List<RadioButton> radioButtons;
    Intent intent;
    Protocal protocal;
    Socket socket;
    String mac;
    int timingMode;
    boolean t1Switch;
    boolean t2Switch;
    boolean t3Switch;
    int t1start;
    int t1Stop;
    int t2start;
    int t2Stop;
    int t3start;
    int t3Stop;
    PmAllData mPmAllData;//查询数据
    private boolean isFirst = true;//只有一次

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.FAIL:
                    ToastUtil.show(FixedTimeActivity.this, "设备网络断开");
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    linearLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mPmAllData = (PmAllData) msg.obj;
                    if (mPmAllData != null) {
                        if (mPmAllData.getTimeMode() == 1) {
                            radioButtons.get(0).setChecked(true);
                        } else if (mPmAllData.getTimeMode() == 2) {
                            radioButtons.get(1).setChecked(true);
                        } else if (mPmAllData.getTimeMode() == 4) {
                            radioButtons.get(2).setChecked(true);
                        }
                        String timerOneText = String.format("%02d", mPmAllData.getTimerOneStartHour()) + ":" + String.format("%02d", mPmAllData.getTimerOneStartMin()) + " - " + String.format("%02d", mPmAllData.getTimerOneEndHour()) + ":" + String.format("%02d", mPmAllData.getTimerOneEndMin());
                        String timerwoText = String.format("%02d", mPmAllData.getTimerTwoStartHour()) + ":" + String.format("%02d", mPmAllData.getTimerTwoStartMin()) + " - " + String.format("%02d", mPmAllData.getTimerTwoEndHour()) + ":" + String.format("%02d", mPmAllData.getTimerTwoEndMin());
                        String timerhreeText = String.format("%02d", mPmAllData.getTimerThreeStartHour()) + ":" + String.format("%02d", mPmAllData.getTimerThreeStartMin()) + " - " + String.format("%02d", mPmAllData.getTimerThreeEndHour()) + ":" + String.format("%02d", mPmAllData.getTimerThreeEndMin());
                        checkBoxes.get(0).setChecked(mPmAllData.isTimerOneState());
                        checkBoxes.get(0).setText(timerOneText);
                        checkBoxes.get(1).setChecked(mPmAllData.isTimerTwoState());
                        checkBoxes.get(1).setText(timerwoText);
                        checkBoxes.get(2).setChecked(mPmAllData.isTimerThreeState());
                        checkBoxes.get(2).setText(timerhreeText);
                    }
                    isFirst = false;
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_fixed_time;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        mac = getIntent().getStringExtra(StatisConstans.MAC);
        setMidTxt("定时");
        initTitleBar();
        setRightView("确定", true);
        setLinterens();
        registerBoradcastReceiver();
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                String ip = sharedPreferencesDB.getString(StatisConstans.IP, "");
                String port = sharedPreferencesDB.getString(StatisConstans.PORT, "");
                request(ip, Integer.valueOf(port));
            }
        }));
    }


    private void setLinterens() {
        imageViews.get(0).setOnClickListener(this);
        imageViews.get(1).setOnClickListener(this);
        imageViews.get(2).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StatisConstans.TIME_ONE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data.getStringExtra("on") != null && data.getStringExtra("off") != null) {
                setcheckTex(checkBoxes.get(0), data.getStringExtra("off"), data.getStringExtra("on"));
            }
        } else if (requestCode == StatisConstans.TIME_TWO_REQUEST && resultCode == Activity.RESULT_OK) {
            setcheckTex(checkBoxes.get(1), data.getStringExtra("off"), data.getStringExtra("on"));
        } else if (requestCode == StatisConstans.TIME_THREE_REQUEST && resultCode == Activity.RESULT_OK) {
            setcheckTex(checkBoxes.get(2), data.getStringExtra("off"), data.getStringExtra("on"));
        }
    }

    @Override
    public void onMultiClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                doComit();
                break;
            case R.id.iv_time_one:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra(StatisConstans.TIME, checkBoxes.get(0).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_ONE_REQUEST);
                break;
            case R.id.iv_time_two:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra(StatisConstans.TIME, checkBoxes.get(1).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_TWO_REQUEST);
                break;
            case R.id.iv_time_three:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra(StatisConstans.TIME, checkBoxes.get(2).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_THREE_REQUEST);
                break;
            default:
                break;
        }
    }

    private void doComit() {
        if (linearLayout.getVisibility() == View.GONE) {
            ToastUtil.show(FixedTimeActivity.this, "设备网络断开");
            return;
        }
        String[] cbOnearr = checkBoxes.get(0).getText().toString().trim().split(" - ");
        String[] cbTwoarr = checkBoxes.get(1).getText().toString().trim().split(" - ");
        String[] cbThreearr = checkBoxes.get(2).getText().toString().trim().split(" - ");
        t1start = Integer.parseInt(cbOnearr[0].replace(":", ""));
        t1Stop = Integer.parseInt(cbOnearr[1].replace(":", ""));
        t2start = Integer.parseInt(cbTwoarr[0].replace(":", ""));
        t2Stop = Integer.parseInt(cbTwoarr[1].replace(":", ""));
        t3start = Integer.parseInt(cbThreearr[0].replace(":", ""));
        t3Stop = Integer.parseInt(cbThreearr[1].replace(":", ""));
        if (radioButtons.get(0).isChecked()) {
            timingMode = 1;
        } else if (radioButtons.get(1).isChecked()) {
            timingMode = 2;
        } else if (radioButtons.get(2).isChecked()) {
            timingMode = 4;
        }
        t1Switch = checkBoxes.get(0).isChecked();
        t2Switch = checkBoxes.get(1).isChecked();
        t3Switch = checkBoxes.get(2).isChecked();
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ScoketOFFeON.sendTimingCommand(socket, protocal, mac, timingMode, t1Switch, t2Switch, t3Switch, t1start, t1Stop, t2start, t2Stop, t3start, t3Stop);
                } catch (Exception e) {
                    handler.sendEmptyMessage(StatisConstans.FAIL);
                    e.printStackTrace();
                }
            }
        }));
    }

    private void registerBoradcastReceiver() {
//        IntentFilter filter = new IntentFilter(
//                StatisConstans.BROADCAST_HONGREN_SUCC);
//        filter.addAction(StatisConstans.BROADCAST_HONGREN_KILL);
//        filter.addAction(StatisConstans.BROADCAST_HONGREN_DATA);
//        FixedTimeActivity.this.registerReceiver(mBroadcastReceiver, filter);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
//        FixedTimeActivity.this.unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_SUCC)) {
//                //编辑成功的广播
//                finish();
//            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_KILL)) {
//                //失败的广播
//            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
//                if (isFirst) {
//                    //得到数据的广播
//                    Bundle bundle = intent.getExtras();
//                    pmAllData = (PmAllData) bundle.getSerializable(StatisConstans.PM_ALL_DATA);
//                    if (pmAllData.getFanFreq() > 9) {
//                        handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL);
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        ToastUtil.show(FixedTimeActivity.this, "设备网络断开");
//                    }
//                }
//            }
//        }
//    };

    @Subscribe
    public void onEventMainThread(FirstEvent event) {
        String msg = event.getMsg();
        if (msg.contains("成功")) {
            finish();
        }
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onEventMainThread(PmAllData pmAllData) {
        if (isFirst) {
            //得到数据的广播
            if (pmAllData.getFanFreq() > 9) {
//                handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL);
                handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL, pmAllData));
            } else {
                progressBar.setVisibility(View.GONE);
                ToastUtil.show(FixedTimeActivity.this, "设备网络断开");
            }
        }
    }

    public void setcheckTex(CheckBox checkBox, String off, String on) {
        checkBox.setText(on + " - " + off);
    }

    public void request(String host, int location) {
        try {
            // 1.连接服务器
            socket = SocketSingle.getInstance(host, location, false);
            Log.d("ConnectionManager", "AbsClient*****已经建立连接");
            protocal = Protocal.getInstance();
            ScoketOFFeON.sendMessage(socket, protocal, mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
