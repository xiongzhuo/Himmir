package com.himmiractivity.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himmiractivity.App;
import com.himmiractivity.Utils.SocketSingle;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.circular_progress_bar.CircularProgressBar;
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

public class IntelligenceModeActivity extends BaseBusActivity {
    @BindView(R.id.progress)
    CircularProgressBar progressBar;
    @BindView(R.id.ll_content)
    LinearLayout linearLayout;
    @BindViews({R.id.ll_co, R.id.ll_pm})
    List<LinearLayout> linearLayouts;
    @BindViews({R.id.tv_co_value, R.id.tv_pm_value})
    List<TextView> textViews;
    @BindViews({R.id.cb_mute, R.id.cb_co, R.id.cb_dust})
    List<CheckBox> checkBoxs;
    Socket socket;
    String mac;
    Protocal protocal;
    PmAllData pmAllData;
    AlertDialog dialog;
    private boolean isFirst = true;//只有一次
    boolean muteMode;
    boolean coMode;
    boolean pmMode;
    int coNumber;
    int pmNumber;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.FAIL:
                    ToastUtil.show(IntelligenceModeActivity.this, "设备网络断开");
                    break;
                case StatisConstans.MSG_ENABLED_SUCCESSFUL:
                    linearLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (pmAllData != null) {
                        checkBoxs.get(0).setChecked(pmAllData.isMuteMode());
                        checkBoxs.get(1).setChecked(pmAllData.isCoMode());
                        checkBoxs.get(2).setChecked(pmAllData.isPmMode());
                        coNumber = pmAllData.getCo2Adj();
                        pmNumber = pmAllData.getPmAdj();
                        textViews.get(0).setText(coNumber + " ppm");
                        textViews.get(1).setText(pmNumber + " ug/m³");
                        isFirst = false;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.intelligence_mode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        mac = getIntent().getStringExtra(StatisConstans.MAC);
        setMidTxt("智能模式");
        initTitleBar();
        setRightView("确定", true);
        registerBoradcastReceiver();
        linearLayouts.get(0).setOnClickListener(this);
        linearLayouts.get(1).setOnClickListener(this);
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

    @Override
    protected void initData() {

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
            case R.id.ll_co:
                showinputPassdialog("CO₂调节值设置", "请输入(800-1800)", "co");
                break;
            case R.id.ll_pm:
                showinputPassdialog("PM2.5调节值设置", "请输入(10-1000)", "PM");
                break;
            default:
                break;
        }
    }

    private void doComit() {
        if (linearLayout.getVisibility() == View.GONE) {
            ToastUtil.show(IntelligenceModeActivity.this, "设备网络断开");
            return;
        }
        muteMode = checkBoxs.get(0).isChecked();
        coMode = checkBoxs.get(1).isChecked();
        pmMode = checkBoxs.get(2).isChecked();
        String[] arrCO = textViews.get(0).getText().toString().trim().split("\\ ");
        coNumber = Integer.parseInt(arrCO[0]);
        String[] arrPM = textViews.get(1).getText().toString().trim().split("\\ ");
        pmNumber = Integer.parseInt(arrPM[0]);
        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.CachedThread, 1);
        threadPoolUtils.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ScoketOFFeON.sendNoopsycheMode(socket, protocal, mac, muteMode, coMode, pmMode, coNumber, pmNumber);
                } catch (Exception e) {
                    handler.sendEmptyMessage(StatisConstans.FAIL);
                    e.printStackTrace();
                }
            }
        }));

    }

    public void showinputPassdialog(String head, String headHit, final String isTool) {
        //防止重复按按钮
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_renname, null);
        Button btnComit = (Button) view.findViewById(R.id.btn_comit);
        Button btnCanel = (Button) view.findViewById(R.id.btn_canel);
        TextView tv_head = (TextView) view.findViewById(R.id.tv_head);
        final EditText etNewName = (EditText) view.findViewById(R.id.et_new_name);
        etNewName.setHint(headHit);
        tv_head.setText(head);
        etNewName.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog = new AlertDialog.Builder(IntelligenceModeActivity.this)
                .create();
        Window w = dialog.getWindow();
        if (w != null) {
            w.setWindowAnimations(R.style.mystyle1);
        }
        dialog.show();
        dialog.getWindow().setContentView(view);
        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //加上下面这一行弹出对话框时软键盘随之弹出
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        dialog.getWindow().setLayout((int) (width * 0.8),
                ViewGroup.LayoutParams.WRAP_CONTENT);
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
                if (TextUtils.isEmpty(etNewName.getText().toString().trim())) {
                    ToastUtils.show(IntelligenceModeActivity.this, "输入的值不能为空", Toast.LENGTH_LONG);
                    return;
                }
                if (isTool.equals("co")) {
                    if (Integer.valueOf(etNewName.getText().toString().trim()) >= 1800) {
                        textViews.get(0).setText("1800 ppm");
                        ToastUtils.show(IntelligenceModeActivity.this, "CO₂的最大值为1800", Toast.LENGTH_LONG);
                    } else if (Integer.valueOf(etNewName.getText().toString().trim()) <= 800) {
                        textViews.get(0).setText("800 ppm");
                        ToastUtils.show(IntelligenceModeActivity.this, "CO₂的最小值为800", Toast.LENGTH_LONG);
                    } else {
                        textViews.get(0).setText(etNewName.getText().toString().trim() + " ppm");
                    }
                } else {
                    if (Integer.valueOf(etNewName.getText().toString().trim()) >= 1000) {
                        textViews.get(1).setText("1000 ug/m³");
                        ToastUtils.show(IntelligenceModeActivity.this, "PM2.5的最大值为1000", Toast.LENGTH_LONG);
                    } else if (Integer.valueOf(etNewName.getText().toString().trim()) <= 10) {
                        textViews.get(1).setText("10 ug/m³");
                        ToastUtils.show(IntelligenceModeActivity.this, "PM2.5的最小值为10", Toast.LENGTH_LONG);
                    } else {
                        textViews.get(1).setText(etNewName.getText().toString().trim() + " ug/m³");
                    }
                }
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }

    private void registerBoradcastReceiver() {
        IntentFilter filter = new IntentFilter(
                StatisConstans.BROADCAST_HONGREN_SUCC);
        filter.addAction(StatisConstans.BROADCAST_HONGREN_KILL);
        filter.addAction(StatisConstans.BROADCAST_HONGREN_DATA);
        IntelligenceModeActivity.this.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntelligenceModeActivity.this.unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_SUCC)) {
                finish();
            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_KILL)) {
                //失败
            } else if (action.equalsIgnoreCase(StatisConstans.BROADCAST_HONGREN_DATA)) {
                if (isFirst) {
                    //得到数据
                    pmAllData = (PmAllData) intent.getExtras().getSerializable(StatisConstans.PM_ALL_DATA);
                    if (pmAllData.getFanFreq() > 9) {
                        handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.show(IntelligenceModeActivity.this, "设备网络断开");
                    }
                }
            }
        }
    };

    public void request(String host, int location) {
        try {
            // 1.连接服务器
            socket = SocketSingle.getInstance(host, location, false);
            protocal = Protocal.getInstance();
            ScoketOFFeON.sendMessage(socket, protocal, mac);
        } catch (Exception e) {
            request(host, location);
            e.printStackTrace();
        }
    }
}
