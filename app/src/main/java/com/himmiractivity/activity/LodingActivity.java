package com.himmiractivity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.himmiractivity.Utils.CheckMobileAndEmail;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.LodingRequest;
import com.himmiractivity.view.ClearEditText;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

public class LodingActivity extends BaseBusActivity {
    @BindViews({R.id.et_password, R.id.et_user})
    List<ClearEditText> clearEditTexts;
    @BindViews({R.id.tv_register, R.id.tv_forgot})
    List<TextView> textViews;
    @BindView(R.id.btn_loding)
    Button btnLoding;
    String user, pass;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //成功
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    UserData userData = (UserData) msg.obj;
                    sharedPreferencesDB.setString("token", userData.getToken());
                    sharedPreferencesDB.setString("phone", userData.getUserMobile());
                    sharedPreferencesDB.setString("key", userData.getUserKey());
                    sharedPreferencesDB.setString("username", user);
                    sharedPreferencesDB.setString("userpwd", pass);
                    Intent intent = new Intent();
                    intent.setClass(LodingActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userData", userData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    break;
                case StatisConstans.MSG_RECEIVED_BOUND:
                    ToastUtil.show(LodingActivity.this, "请求失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.loding;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        textViews.get(0).setOnClickListener(this);
        textViews.get(1).setOnClickListener(this);
        btnLoding.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loding:
                confirmation();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forgot:
                startActivity(new Intent(this, ForgotPassActivity.class));
                break;
            default:
                break;
        }
    }

    //验证信息是否通过
    private void confirmation() {
        user = clearEditTexts.get(1).getText().toString().trim();
        pass = clearEditTexts.get(0).getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            ToastUtil.show(this, "手机号码不能为空!");
            return;
        } else if (!CheckMobileAndEmail.isMobileNO(user)) {
            ToastUtil.show(this, "手机号码格式错误");
            return;
        } else if (TextUtils.isEmpty(pass)) {
            ToastUtil.show(this, "密码不能为空");
            return;
        } else if (pass.length() < 6) {
            ToastUtil.show(this, "密码长度为6-16位");
            return;
        }
        LodingRequest lodingRequest = new LodingRequest(sharedPreferencesDB, LodingActivity.this, pass, user, sharedPreferencesDB.getString("userDeviceUuid", ""), handler);
        try {
            lodingRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
