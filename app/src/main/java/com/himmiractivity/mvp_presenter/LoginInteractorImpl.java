package com.himmiractivity.mvp_presenter;

import android.text.TextUtils;

import com.himmiractivity.Utils.CheckMobileAndEmail;
import com.himmiractivity.Utils.SharedPreferencesDB;
import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.entity.UserData;
import com.himmiractivity.mvp_model.LoginModel;
import com.himmiractivity.mvp_model.LoginModelImpl;
import com.himmiractivity.mvp_view.LoginView;

/**
 * Created by Administrator on 2017/5/27.
 */

public class LoginInteractorImpl implements LoginInteractor, LoginModelImpl.OnLoginDataListener {
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginInteractorImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }

    @Override
    public void confirmation(SharedPreferencesDB sharedPreferencesDB, String userName, String password) {
        if (TextUtils.isEmpty(userName)) {
            loginView.showToast("手机号码不能为空!");
            return;
        } else if (!CheckMobileAndEmail.isMobileNO(userName)) {
            loginView.showToast("手机号码格式错误");
            return;
        } else if (TextUtils.isEmpty(password)) {
            loginView.showToast("密码不能为空");
            return;
        } else if (password.length() < 6) {
            loginView.showToast("密码长度为6-16位");
            return;
        }
        loginView.showProgress();
        loginModel.loadData(sharedPreferencesDB, password, userName, this);
    }

    @Override
    public void onSuccess(UserData userData) {
        System.out.println("onSuccess=========");
        if (null != userData) {
            loginView.navigateToHome(userData);
            loginView.hideProgress();
        }
    }

    @Override
    public void onToast(String string) {
        loginView.showToast(string);
        loginView.hideProgress();
    }
}
