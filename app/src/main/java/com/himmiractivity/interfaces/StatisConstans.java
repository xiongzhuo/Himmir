package com.himmiractivity.interfaces;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface StatisConstans {
    String BROADCAST_HONGREN_SUCC = "suceess";//成功
    String BROADCAST_HONGREN_KILL = "fail";//失败
    String BROADCAST_HONGREN_DATA = "data";
    //相册权限
    int MY_PERMISSIONS_REQUEST_CAMERA = 11;
    //相册权限
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10;
    //定位权限
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10086;
    //定位权限
    int MY_PERMISSIONS_REQUEST_LOCATION = 24;
    int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 23;
    int MSG_IMAGE_REQUEST = 24;
    int MSG_SUCCCE_TURS = 244;
    /**
     * 时间request
     */
    int TIME_ONE_REQUEST = 101;
    int TIME_TWO_REQUEST = 102;
    int TIME_THREE_REQUEST = 103;

    String KEY_SAVE_LABLE = "dafalut_user_room";
    /**
     * 注册成功
     */
    int MSG_RECEIVED_CODE = 3;
    // 失败
    int MSG_RECEIVED_BOUND = 2;
    // 成功
    int MSG_RECEIVED_REGULAR = 1;
    // 删除成功
    int MSG_DELETE = 1008;
    // 重名成功
    int MSG_MODIFY_NAME = 1010;
    /**
     * 上传图片成功
     */
    int MSG_IMAGE_SUCCES = 100;

    int ImgLimit = 30;// 75
    // 获取服务器配置信息成功
    int CONFIG_REGULAR = 109;
    int MSG_QQUIP = 110;

    // 配置档成功
    int MSG_ENABLED_SUCCESSFUL = 37;
    // 配置档失败
    int MSG_ENABLED_FAILING = 38;
    int MSG_QUEST_SERVER = 45;
    // 循环发送
    int MSG_CYCLIC_TRANSMISSION = 1110;
}
