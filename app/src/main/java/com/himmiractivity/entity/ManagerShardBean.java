package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ManagerShardBean implements Serializable {
    List<ManagerShardSum> shareUserList;

    public List<ManagerShardSum> getShareUserList() {
        return shareUserList;
    }

    public void setShareUserList(List<ManagerShardSum> shareUserList) {
        this.shareUserList = shareUserList;
    }

    public class ManagerShardSum implements Serializable {
        String user_name;
        String user_key;
        String share_name;

        public String getShare_name() {
            return share_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_key() {
            return user_key;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setUser_key(String user_key) {
            this.user_key = user_key;
        }

        public void setShare_name(String share_name) {
            this.share_name = share_name;
        }
    }
}
