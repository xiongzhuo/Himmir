package com.himmiractivity.mvp_presenter;

import com.himmiractivity.Utils.SharedPreferencesDB;

/**
 * Created by Administrator on 2017/5/27.
 */

public interface LoginInteractor {
    void confirmation(SharedPreferencesDB sharedPreferencesDB, String userName, String password);
}
