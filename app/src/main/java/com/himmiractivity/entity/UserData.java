package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class UserData implements Serializable {
    private List<UserRoom> userRoom;

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

    public void setUserRoom(List<UserRoom> userRoom) {
        this.userRoom = userRoom;
    }

    public List<UserRoom> getUserRoom() {
        return this.userRoom;
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

    class UserRoom implements Serializable {
        private String room_name;

        private int user_room_id;

        private String create_time;

        private String user_key;

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_name() {
            return this.room_name;
        }

        public void setUser_room_id(int user_room_id) {
            this.user_room_id = user_room_id;
        }

        public int getUser_room_id() {
            return this.user_room_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public void setUser_key(String user_key) {
            this.user_key = user_key;
        }

        public String getUser_key() {
            return this.user_key;
        }
    }
}
