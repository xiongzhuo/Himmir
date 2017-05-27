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
import com.himmiractivity.base.BaseBusNoSocllowActivity;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.mvp_presenter.LoginInteractor;
import com.himmiractivity.mvp_presenter.LoginInteractorImpl;
import com.himmiractivity.mvp_view.LoginView;
import com.himmiractivity.request.LodingRequest;
import com.himmiractivity.view.ClearEditText;
import com.himmiractivity.view.HomeDialog;

import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;
import butterknife.BindViews;

public class LodingActivity extends BaseBusNoSocllowActivity implements LoginView {
    @BindViews({R.id.et_password, R.id.et_user})
    List<ClearEditText> clearEditTexts;
    @BindViews({R.id.tv_register, R.id.tv_forgot})
    List<TextView> textViews;
    @BindView(R.id.btn_loding)
    Button btnLoding;
    String user, pass;
    private LoginInteractor loginInteractor;
    private HomeDialog.DialogView dialogView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.loding;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(sharedPreferencesDB.getString(StatisConstans.USERNAME, ""))) {
            clearEditTexts.get(1).setText(sharedPreferencesDB.getString(StatisConstans.USERNAME, ""));
        }
        textViews.get(0).setOnClickListener(this);
        textViews.get(1).setOnClickListener(this);
        btnLoding.setOnClickListener(this);
        loginInteractor = new LoginInteractorImpl(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onMultiClick(View view) {
        switch (view.getId()) {
            case R.id.btn_loding:
                user = clearEditTexts.get(1).getText().toString().trim();
                pass = clearEditTexts.get(0).getText().toString().trim();
                loginInteractor.confirmation(sharedPreferencesDB, user, pass);
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

    @Override
    public void showProgress() {
        if (null == dialogView) {
            dialogView = new HomeDialog.DialogView(LodingActivity.this);
            dialogView.show();
            dialogView.setMessage("加载中");
        }
    }

    @Override
    public void hideProgress() {
        if (null != dialogView) {
            dialogView.cancel();
            dialogView = null;
        }
    }

    @Override
    public void navigateToHome(UserData userData) {
        sharedPreferencesDB.setString(StatisConstans.TOKEN, userData.getToken());
        sharedPreferencesDB.setString(StatisConstans.PHONE, userData.getUserMobile());
        sharedPreferencesDB.setString(StatisConstans.KEY, userData.getUserKey());
        sharedPreferencesDB.setString(StatisConstans.USERNAME, user);
        sharedPreferencesDB.setString(StatisConstans.USERPWD, pass);
        Intent intent = new Intent();
        intent.setClass(LodingActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userData", userData);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
