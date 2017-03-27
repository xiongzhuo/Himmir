package com.himmiractivity.entity;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 7142017721130791109L;
    private boolean flag = false;
    private String msg = "没有数据!";
    private T data;

    public JsonResult() {
    }

    public boolean isFlag() {
        return flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult [flag=" + flag + ", msg=" + msg
                + ", data=" + data + "]";
    }


}
