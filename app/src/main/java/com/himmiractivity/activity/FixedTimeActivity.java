package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.himmiractivity.App;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.interfaces.StatisConstans;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

/**
 * Created by Administrator on 2017/3/13.
 */

public class FixedTimeActivity extends BaseBusActivity {
    @BindViews({R.id.iv_time_one, R.id.iv_time_two, R.id.iv_time_three})
    List<ImageView> imageViews;
    @BindViews({R.id.cb_time_one, R.id.cb_time_two, R.id.cb_time_three})
    List<CheckBox> checkBoxes;
    Intent intent;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_fixed_time;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("定时");
        initTitleBar();
        setRightView("确定", true);
        setLinterens();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                ToastUtils.show(FixedTimeActivity.this, "你点击了右边的按钮", ToastUtils.LENGTH_SHORT);
                break;
            case R.id.iv_time_one:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra("time", checkBoxes.get(0).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_ONE_REQUEST);
                break;
            case R.id.iv_time_two:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra("time", checkBoxes.get(1).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_TWO_REQUEST);
                break;
            case R.id.iv_time_three:
                intent = new Intent();
                intent.setClass(this, EditTimeActivity.class);
                intent.putExtra("time", checkBoxes.get(2).getText().toString().trim());
                startActivityForResult(intent, StatisConstans.TIME_THREE_REQUEST);
                break;
            default:
                break;
        }
    }

    public void setcheckTex(CheckBox checkBox, String off, String on) {
        checkBox.setText(on + " - " + off);
    }
}
