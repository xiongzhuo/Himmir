package com.himmiractivity.mvp_view;

import com.himmiractivity.entity.UserData;

/**
 * Created by Administrator on 2017/5/27.
 */

public interface LoginView {

    void showToast(String string);

    void showProgress();

    void hideProgress();

    void navigateToHome(UserData userData);
}
