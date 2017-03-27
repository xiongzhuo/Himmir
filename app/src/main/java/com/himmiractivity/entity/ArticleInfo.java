package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 绑定成功
 */
public class ArticleInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<DafalutUserRoom> dafalutUserRoom;

    public void setDafalutUserRoom(List<DafalutUserRoom> dafalutUserRoom) {
        this.dafalutUserRoom = dafalutUserRoom;
    }

    public List<DafalutUserRoom> getDafalutUserRoom() {
        return this.dafalutUserRoom;
    }
}
