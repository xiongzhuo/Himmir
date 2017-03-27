package com.himmiractivity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DafalutUserRoom implements Serializable {
    boolean isChecK;
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

    public boolean isChecK() {
        return isChecK;
    }

    public void setChecK(boolean checK) {
        isChecK = checK;
    }
}
