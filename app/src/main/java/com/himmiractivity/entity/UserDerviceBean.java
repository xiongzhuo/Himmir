package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class UserDerviceBean implements Serializable {
    List<UserDerviceSum> shareUserDevList;

    public List<UserDerviceSum> getShareUserDevList() {
        return shareUserDevList;
    }

    public void setShareUserDevList(List<UserDerviceSum> shareUserDevList) {
        this.shareUserDevList = shareUserDevList;
    }

    public class UserDerviceSum implements Serializable {
        String device_mac;
        String device_nickname;
        boolean onLine;

        public String getDevice_mac() {
            return device_mac;
        }

        public String getDevice_nickname() {
            return device_nickname;
        }

        public boolean isOnLine() {
            return onLine;
        }

        public void setOnLine(boolean onLine) {
            this.onLine = onLine;
        }

        public void setDevice_mac(String device_mac) {
            this.device_mac = device_mac;
        }

        public void setDevice_nickname(String device_nickname) {
            this.device_nickname = device_nickname;
        }
    }
}
