package com.himmiractivity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.ModifyPwdRequest;
import com.himmiractivity.view.ClearEditText;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

/**
 * 修改密码
 */

public class RevicePassActivity extends BaseBusActivity {
    @BindView(R.id.btn_revise_comit)
    Button btnReviseComit;
    @BindViews({R.id.et_old_pass, R.id.et_new_pass, R.id.et_pass_new_comit})
    List<ClearEditText> clearEditTexts;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //重置成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    ToastUtils.show(RevicePassActivity.this, "修改密码成功", ToastUtils.LENGTH_SHORT);
                    finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.revise_pass;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("修改密码");
        initTitleBar();
        btnReviseComit.setOnClickListener(this);
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
            case R.id.btn_revise_comit:
                confirmation();
                break;
            default:
                break;
        }
    }

    //验证是否通过
    private void confirmation() {
        String oldPwd = clearEditTexts.get(0).getText().toString().trim();
        String newPwd = clearEditTexts.get(1).getText().toString().trim();
        String newPwdCimt = clearEditTexts.get(2).getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtils.show(RevicePassActivity.this, "请输入您的旧密码", Toast.LENGTH_SHORT);
            return;
        } else if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.show(RevicePassActivity.this, "请输入您的新密码", Toast.LENGTH_SHORT);
            return;
        } else if (TextUtils.isEmpty(newPwdCimt)) {
            ToastUtils.show(RevicePassActivity.this, "请再次输入您的新密码", Toast.LENGTH_SHORT);
            return;
        } else if (oldPwd.length() < 6) {
            ToastUtils.show(RevicePassActivity.this, "密码长度为6-16位", Toast.LENGTH_SHORT);
            return;
        } else if (newPwd.length() < 6) {
            ToastUtils.show(RevicePassActivity.this, "密码长度为6-16位", Toast.LENGTH_SHORT);
            return;
        } else if (newPwd.length() < 6) {
            ToastUtils.show(RevicePassActivity.this, "密码长度为6-16位", Toast.LENGTH_SHORT);
            return;
        } else if (oldPwd.equals(newPwd)) {
            ToastUtil.show(this, "新密码不能和旧密码一致");
            return;
        } else if (!newPwd.equals(newPwdCimt)) {
            ToastUtil.show(this, "两次新密码不一致");
            return;
        }
        ModifyPwdRequest modifyPwdRequest = new ModifyPwdRequest(sharedPreferencesDB, RevicePassActivity.this, newPwd, oldPwd, handler);
        try {
            modifyPwdRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
