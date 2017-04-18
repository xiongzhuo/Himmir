package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class UserData implements Serializable {
    private List<UserRoom> userDevs;

    private String token;

    private int useId;

    private String userName;

    private String userMobile;
    private String userKey;

    private String userValidateperiod;

    private int userQq;

    private boolean userActive;

    private String userImage;

    private String registerTime;

    private String loginLasttime;
    private String userShareName;

    private String userShareCode;

    public List<UserRoom> getUserDevs() {
        return userDevs;
    }

    public void setUserDevs(List<UserRoom> userDevs) {
        this.userDevs = userDevs;
    }

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

    public String getUserShareName() {
        return userShareName;
    }

    public String getUserShareCode() {
        return userShareCode;
    }

    public void setUserShareName(String userShareName) {
        this.userShareName = userShareName;
    }

    public void setUserShareCode(String userShareCode) {
        this.userShareCode = userShareCode;
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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
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

    public class UserRoom implements Serializable {
        private String device_mac;

        private int user_device_id;

        private String user_key;

        private String device_nickname;
        private String user_token;

        public String getDevice_mac() {
            return device_mac;
        }

        public int getUser_device_id() {
            return user_device_id;
        }

        public String getUser_key() {
            return user_key;
        }

        public String getDevice_nickname() {
            return device_nickname;
        }

        public String getUser_token() {
            return user_token;
        }

        public void setDevice_mac(String device_mac) {
            this.device_mac = device_mac;
        }

        public void setUser_device_id(int user_device_id) {
            this.user_device_id = user_device_id;
        }

        public void setUser_key(String user_key) {
            this.user_key = user_key;
        }

        public void setDevice_nickname(String device_nickname) {
            this.device_nickname = device_nickname;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }

    }
}
