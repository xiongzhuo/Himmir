package com.himmiractivity.interfaces;

/**
 * Created by Administrator on 2017/3/14.
 */

public interface StatisConstans {
    int MSG_IMAGE_REQUEST = 24;
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
}
