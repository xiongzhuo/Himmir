package com.himmiractivity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himmiractivity.App;
import com.himmiractivity.Utils.StringUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.view.widget.NumericwheelTwoAdapter;
import com.himmiractivity.view.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

/**
 * 设备管理
 */

public class EditTimeActivity extends BaseBusActivity {
    @BindView(R.id.time_on)
    LinearLayout timeOn;
    @BindView(R.id.time_off)
    LinearLayout timeOff;
    @BindView(R.id.tv_on_time)
    TextView tvOnTime;
    @BindView(R.id.tv_off_time)
    TextView tvOffTime;
    private WheelView hour;
    private WheelView mins;
    String time;

    @Override
    protected int getContentLayoutId() {
        return R.layout.edit_time;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("编辑事件段");
        initTitleBar();
        setRightView("确定", true);
        if (getIntent().getStringExtra("time") != null) {
            time = getIntent().getStringExtra("time");
            String split = " - ";
            try {
                List<String> arr = StringUtil.getSubString(time, split);
                tvOnTime.setText(arr.get(0));
                tvOffTime.setText(arr.get(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        timeOff.setOnClickListener(this);
        timeOn.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                Intent intent = new Intent();
                intent.putExtra("on", tvOnTime.getText().toString().trim());
                intent.putExtra("off", tvOffTime.getText().toString().trim());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.time_off:
                showTimeDialog("off", tvOffTime.getText().toString().trim());
                break;
            case R.id.time_on:
                showTimeDialog("on", tvOnTime.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    /**
     * 显示时间
     */
    private void showTimeDialog(final String offandon, String string) {
        final AlertDialog dialog = new AlertDialog.Builder(EditTimeActivity.this)
                .create();
        dialog.show();
        Window window = dialog.getWindow();
        // 设置布局
        window.setContentView(R.layout.timepick);
        // 设置宽高
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.AnimBottom);

        hour = (WheelView) window.findViewById(R.id.hour);
        initHour();
        mins = (WheelView) window.findViewById(R.id.mins);
        initMins();
        int ch = 0;
        int mc = 0;
        try {
            List<String> arr = StringUtil.getSubString(string, ":");
            ch = Integer.parseInt(arr.get(0));
            mc = Integer.parseInt(arr.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置当前时间
        hour.setCurrentItem(ch);
        mins.setCurrentItem(mc);
        hour.setVisibleItems(7);
        mins.setVisibleItems(7);

        // 设置监听
        Button ok = (Button) window.findViewById(R.id.set);
        Button cancel = (Button) window.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String str = hour.getCurrentItem() + ":" + mins.getCurrentItem();
                if (offandon.equals("off")) {
                    tvOffTime.setText(str);
                } else {
                    tvOnTime.setText(str);
                }
                dialog.cancel();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });
        LinearLayout cancelLayout = (LinearLayout) window.findViewById(R.id.view_none);
        cancelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialog.cancel();
                return false;
            }
        });
    }

    /**
     * 初始化时
     */
    private void initHour() {
        NumericwheelTwoAdapter numericWheelAdapter = new NumericwheelTwoAdapter(this, 0, 23, "%02d");
        numericWheelAdapter.setLabel(" 时");
        //		numericWheelAdapter.setTextSize(15);  设置字体大小
        hour.setViewAdapter(numericWheelAdapter);
        hour.setCyclic(true);
    }

    /**
     * 初始化分
     */
    private void initMins() {
        NumericwheelTwoAdapter numericWheelAdapter = new NumericwheelTwoAdapter(this, 0, 59, "%02d");
        numericWheelAdapter.setLabel(" 分");
//		numericWheelAdapter.setTextSize(15);  设置字体大小
        mins.setViewAdapter(numericWheelAdapter);
        mins.setCyclic(true);
    }
}