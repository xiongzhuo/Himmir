package com.himmiractivity.entity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class ModifyNameData {
    private String token;

    private int useId;

    private String userName;

    private String userMobile;

    private String userValidateperiod;

    private int userQq;

    private boolean userActive;

    private String userImage;

    private String registerTime;

    private String loginLasttime;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setUseId(int useId) {
        this.useId = useId;
    }

    public int getUseId() {
        return this.useId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserMobile() {
        return this.userMobile;
    }

    public void setUserValidateperiod(String userValidateperiod) {
        this.userValidateperiod = userValidateperiod;
    }

    public String getUserValidateperiod() {
        return this.userValidateperiod;
    }

    public void setUserQq(int userQq) {
        this.userQq = userQq;
    }

    public int getUserQq() {
        return this.userQq;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }

    public boolean getUserActive() {
        return this.userActive;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterTime() {
        return this.registerTime;
    }

    public void setLoginLasttime(String loginLasttime) {
        this.loginLasttime = loginLasttime;
    }

    public String getLoginLasttime() {
        return this.loginLasttime;
    }
}
