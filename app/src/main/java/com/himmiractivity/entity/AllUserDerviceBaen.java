package com.himmiractivity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户所有设备
 */

public class AllUserDerviceBaen implements Serializable {


    private List<Space> space;

    public void setSpace(List<Space> space) {
        this.space = space;
    }

    public List<Space> getSpace() {
        return this.space;
    }
}

