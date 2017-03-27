package com.himmiractivity.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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

import com.himmiractivity.App;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

public class IntelligenceModeActivity extends BaseBusActivity {
    @BindView(R.id.ll_co)
    LinearLayout llCo2;
    @BindView(R.id.ll_pm)
    LinearLayout ll_pm;
    @BindView(R.id.tv_co_value)
    TextView tvCoValue;
    @BindView(R.id.tv_pm_value)
    TextView tvPmValus;

    @Override

    protected int getContentLayoutId() {
        return R.layout.intelligence_mode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("智能模式");
        initTitleBar();
        setRightView("确定", true);
        llCo2.setOnClickListener(this);
        ll_pm.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
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

    public void showinputPassdialog(String head, String headHit, final String isTool) {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_renname, null);
        Button btnComit = (Button) view.findViewById(R.id.btn_comit);
        Button btnCanel = (Button) view.findViewById(R.id.btn_canel);
        TextView tv_head = (TextView) view.findViewById(R.id.tv_head);
        final EditText etNewName = (EditText) view.findViewById(R.id.et_new_name);
        etNewName.setHint(headHit);
        tv_head.setText(head);
        etNewName.setInputType(InputType.TYPE_CLASS_NUMBER);
        final AlertDialog dialog = new AlertDialog.Builder(IntelligenceModeActivity.this)
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
                        tvPmValus.setText("1800 ppm");
                        ToastUtils.show(IntelligenceModeActivity.this, "CO₂的最大值为1800", Toast.LENGTH_LONG);
                    } else if (Integer.valueOf(etNewName.getText().toString().trim()) <= 800) {
                        tvPmValus.setText("800 ppm");
                        ToastUtils.show(IntelligenceModeActivity.this, "CO₂的最大值为800", Toast.LENGTH_LONG);
                    } else {
                        tvPmValus.setText(etNewName.getText().toString().trim() + "ppm");
                    }
                } else {
                    if (Integer.valueOf(etNewName.getText().toString().trim()) >= 1000) {
                        tvPmValus.setText("1000 ug/m³");
                        ToastUtils.show(IntelligenceModeActivity.this, "PM2.5的最大值为1000", Toast.LENGTH_LONG);
                    } else if (Integer.valueOf(etNewName.getText().toString().trim()) <= 10) {
                        tvPmValus.setText("10 ug/m³");
                        ToastUtils.show(IntelligenceModeActivity.this, "PM2.5的最小值为10", Toast.LENGTH_LONG);
                    } else {
                        tvPmValus.setText(etNewName.getText().toString().trim() + " ug/m³");
                    }
                }
                dialog.dismiss();
                dialog.cancel();
            }
        });

    }
}
