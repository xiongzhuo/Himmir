package com.himmiractivity.Constant;

/**
 * 接口地址
 */

public class Configuration {
    public static final String HOST = "http://smart.hnliuxing.com:8086";
//    public static final String HOST = "http://192.168.0.88:8086";
//    public static final String HOST = "http://192.168.0.80:8080";
//    public static final String HOST = "http://hongren.bingjun.cn/";
    /**
     * 获取所有设备
     */
    public static final String URL_DERVICE = HOST
            + "/finalapi/user/user/getAllDeviceInfo";
    /**
     * 短信验证码
     */
    public static final String URL_SENDCODE = HOST
            + "/finalapi/user/user/sendCode";
    /**
     * 用户注册
     */
    public static final String URL_REGISTER = HOST
            + "/finalapi/user/user/register";
    /**
     * 3.用户登录
     */
    public static final String URL_LOGIN = HOST
            + "/finalapi/user/user/login";
    /**
     * 用户昵称修改
     */
    public static final String URL_MODIFYNAME = HOST
            + "/finalapi/user/user/modifyName";
    /**
     * 用户密码修改
     */
    public static final String URL_MODIFYPWD = HOST
            + "/finalapi/user/user/modifyPwd";
    /**
     * 用户重置密码
     */
    public static final String URL_RESETPWD = HOST
            + "/finalapi/user/user/resetPwd";
    /**
     * 上传图片
     */
    public static final String URL_GETIMAGEUPLOADS = HOST
            + "/finalapi/user/user/getImageUploads";
    /**
     * 获取默认房间地址
     */
//    public static final String URL_GETDEFAULTUSERROOM = HOST
//            + "/finalapi/user/user/getDefaultUserRoom";
    public static final String URL_GETDEFAULTUSERROOM = HOST
            + "/finalapi/user/user/getDefaultUserRoomsAndDevs";

    /**
     * 发送二维吗
     */
    public static final String URL_GETDEVICEINFO = HOST
            + "/finalapi/user/user/getDeviceInfo";
    /**
     * 获取服务器配置信息
     */
    public static final String URL_GETDATASERVERCONFIG = HOST
            + "/finalapi/user/user/getDataServerConfig";
    /**
     * 上传用户安装信息
     */
    public static final String URL_RECEIVEUSERDEVICE = HOST
            + "/finalapi/user/user/receiveUserDeviceInfo";
    /**
     * 删除设备
     */
    public static final String URL_DELETEDEVICE = HOST
            + "/finalapi/user/user/deleteDevice";
    /**
     * 用户房间重命名
     */
//    public static final String URL_MODIFY_ROOM_NAME = HOST
//            + "/finalapi/user/user/modifyRoomName";
    /**
     * 设备重命名
     */
    public static final String URL_MODIFY_ROOM_NAME = HOST
            + "/finalapi/user/user/modifDecNickName";

}
