package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 绑定成功
 */
public class ArticleInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<UserRoom> UserRoom;
    private List<UserDevsNickname> userDevsNickname;

    public List<UserRoom> getUserRoom() {
        return UserRoom;
    }

    public void setUserRoom(List<UserRoom> userRoom) {
        UserRoom = userRoom;
    }

    public void setUserDevsNickname(List<UserDevsNickname> userDevsNickname) {
        this.userDevsNickname = userDevsNickname;
    }

    public List<UserDevsNickname> getUserDevsNickname() {
        return userDevsNickname;
    }

    public class UserDevsNickname implements Serializable {
        String device_nickname;

        public String getDevice_nickname() {
            return device_nickname;
        }

        public void setDevice_nickname(String device_nickname) {
            this.device_nickname = device_nickname;
        }
    }
}
