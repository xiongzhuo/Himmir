package com.himmiractivity.mvp_model;

import android.content.Context;
import android.os.Handler;

import com.himmiractivity.Utils.SharedPreferencesDB;

/**
 * Created by Administrator on 2017/5/27.
 */

public interface LoginModel {
    void loadData(SharedPreferencesDB sharedPreferencesDB, String pwd, String mobile, LoginModelImpl.OnLoginDataListener listener);
}
