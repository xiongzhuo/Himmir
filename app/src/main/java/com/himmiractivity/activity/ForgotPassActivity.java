package com.himmiractivity.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.himmiractivity.App;
import com.himmiractivity.Utils.CheckMobileAndEmail;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.Utils.ToastUtils;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.ModifyNameData;
import com.himmiractivity.entity.PhoneCode;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.CodeRequest;
import com.himmiractivity.request.ResetPwdRequest;
import com.himmiractivity.view.ClearEditText;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

/**
 * 忘记密码
 */

public class ForgotPassActivity extends BaseBusActivity {
    @BindViews({R.id.btn_identify, R.id.btn_rese_pass})
    List<Button> buttons;
    @BindViews({R.id.et_phone, R.id.et_code, R.id.et_pass, R.id.et_pass_comit})
    List<ClearEditText> clearEditTexts;
    private int seconds = 61;
    private Timer timer = new Timer();
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //重置成功
                case StatisConstans.MSG_RECEIVED_CODE:
                    ModifyNameData modifyNameData = (ModifyNameData) msg.obj;
                    ToastUtils.show(ForgotPassActivity.this, "重置密码成功", ToastUtils.LENGTH_SHORT);
                    finish();
                    break;
                // 验证码 手机号码已绑定其它账号
                case StatisConstans.MSG_RECEIVED_BOUND:
                    buttons.get(0).setText("获取验证码");
                    buttons.get(0).setEnabled(true);
                    break;
                // 验证码 手机号码正常
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    // 验证码校验
                    PhoneCode phoneCode = (PhoneCode) msg.obj;
                    seconds = 61;
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            updateSeconds(this);
                        }
                    }, 0, 1000);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.forgot_pass;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        App.getInstance().addActivity(this);
        setMidTxt("忘记密码");
        initTitleBar();
        buttons.get(1).setOnClickListener(this);
        buttons.get(0).setOnClickListener(this);
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
            case R.id.btn_identify:
                try {
                    identify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_rese_pass:
                confirmation();
                break;
            default:
                break;
        }
    }

    private void identify() {
        String phone = clearEditTexts.get(0).getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(this, "手机号码不能为空!");
        } else if (!CheckMobileAndEmail.isMobileNO(phone)) {
            ToastUtil.show(this, "手机号码格式错误");
        } else {
            buttons.get(0).setText("正在发送...");
            buttons.get(0).setEnabled(false);
            CodeRequest codeRequest = new CodeRequest(ForgotPassActivity.this, phone, "reset", handler);
            try {
                codeRequest.requestCode();// 调短信验证码接口
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //验证信息是否通过
    private void confirmation() {
        String phone = clearEditTexts.get(0).getText().toString().trim();
        String code = clearEditTexts.get(1).getText().toString().trim();
        String pass = clearEditTexts.get(2).getText().toString().trim();
        String passComit = clearEditTexts.get(3).getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(this, "手机号码不能为空!");
            return;
        } else if (!CheckMobileAndEmail.isMobileNO(phone)) {
            ToastUtil.show(this, "手机号码格式错误");
            return;
        } else if (TextUtils.isEmpty(code)) {
            ToastUtil.show(this, "验证码不能为空");
            return;
        } else if (TextUtils.isEmpty(pass)) {
            ToastUtil.show(this, "密码不能为空");
            return;
        } else if (pass.length() < 6) {
            ToastUtil.show(this, "密码长度为6-16位");
            return;
        } else if (TextUtils.isEmpty(passComit)) {
            ToastUtil.show(this, "确认密码不能为空");
            return;
        } else if (!pass.equals(passComit)) {
            ToastUtil.show(this, "两次密码不一致");
            return;
        }
        ResetPwdRequest resetPwdRequest = new ResetPwdRequest(ForgotPassActivity.this, phone, pass, sharedPreferencesDB.getString(StatisConstans.USERDEVICEUUID, ""), code, handler);
        try {
            resetPwdRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateSeconds(final TimerTask task) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (seconds == 0) {
                    buttons.get(0).setText(R.string.btn_get_identify);
                    task.cancel();
                    buttons.get(0).setEnabled(true);
                } else {
                    buttons.get(0).setText(--seconds
                            + getString(R.string.btn_seconds));
                }
            }
        });
    }
}
