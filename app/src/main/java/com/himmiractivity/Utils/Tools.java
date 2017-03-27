package com.himmiractivity.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @类名:Tools
 * @类描述:工具类
 * @作者:倪顺
 * @创建时间:2015-11-5-下午6:41:46
 * @修改人:
 * @修改时间:
 * @修改备注:
 * @版本:
 * @Copyright (c)-2015上海秉钧网络科技有限公司长沙分公司
 */
public class Tools {

    public final static String DATE_PATTERN_1 = "yyyy-MM-dd HH:mm:ss";// 定义标准日期格式1
    public final static String DATE_PATTERN_2 = "yyyy-MM-dd";// 定义标准日期格式2
    public final static String DATE_PATTERN_3 = "yyyy-MM-dd HH:mm";// 定义标准日期格式3

    private static long lastClickTime;

    /**
     * @param times
     * @return
     * @方法说明:防止控件被重复点击，如果点击间隔时间小于指定时间就点击无 @方法名称:isFastDoubleClick
     * @返回 boolean
     */
    public static boolean isFastDoubleClick(long times) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < times) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @return
     * @方法说明:把一个日期，按照某种格式 格式化输出
     * @方法名称:formatDate
     * @返回值:String
     */
    public static String formatDate(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }
        return "";
    }

    /**
     * @param context
     * @return
     * @方法说明:判断当前的手机是否联网
     * @方法名称:isMobileConnected
     * @返回值:boolean
     */
    public static boolean isMobileConnected(Context context) {
        Boolean bl = false;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mMobileNetworkInfo != null) {
                // return mMobileNetworkInfo.isAvailable();
                bl = mMobileNetworkInfo.isAvailable();
            }
        }
        return bl;
    }

    /**
     * @param obj
     * @return
     * @throws IOException
     * @方法说明:将对象转换成String类型
     * @方法名称:objToStr
     * @返回值:String
     */
    public static String objToStr(Object obj) {
        if (obj == null) {
            return "";
        }

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        String listString = "";
        try {
            // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
            baos = new ByteArrayOutputStream();
            // 然后将得到的字符数据装载到ObjectOutputStream
            oos = new ObjectOutputStream(baos);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
            oos.writeObject(obj);
            // 最后，用Base64.encode将字节文件转换成Base64编码，并以String形式保存
            listString = Base64.encodeToString(baos.toByteArray(),
                    Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭oos
                if (oos != null) {
                    oos.flush();
                    oos.close();
                    oos = null;
                }
                // 关闭baos
                if (baos != null) {
                    baos.flush();
                    baos.close();
                    baos = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listString.trim();
    }

    /**
     * @param str
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @方法说明:将String转换成Obj
     * @方法名称:strToObj
     * @返回值:Object
     */
    public static Object strToObj(String str) throws StreamCorruptedException,
            IOException {
        byte[] mByte = Base64.decode(str, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(mByte);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                ois = null;
            }

            if (bais != null) {
                bais.close();
                bais = null;
            }
        }
        return null;
    }

    /**
     * @param city
     * @return
     * @方法说明:城市定位——文字长度控制
     * @方法名称:setCity
     * @作者:倪顺
     * @返回值:String
     */
    public static String setCity(String city) {
        if (TextUtils.isEmpty(city)) {
            return "";
        }
        if (city.length() > 3) {
            return city.substring(0, 3) + "...";
        } else {
            return city;
        }
    }

    public static String subStringName(String str, int length) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() > length) {
            return str.substring(0, length) + "...";
        } else {
            return str;
        }
    }

    /**
     * @param str
     * @return
     * @方法说明:判断字符串是否为
     * @方法名称:isEmpty
     * @返回 boolean
     */
    public static boolean isEmpty(String str) {
        if (null == str || "null" == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }


    public static String setState(int state) {
        switch (state) {
            // case 0:
            // return "未认领";
            case 1:
                return "已认领";
            case 2:
                return "进行中";
            case 3:
                return "待审核";
            case 4:
                return "审核不通过";
            case 5:
                return "未提交";
            case 6:
                return "已完成";
            case 7:
                return "取消认领的任务";
            case -1:
                return "已取消的任务";
            case 8:
                return "申诉中";
            case 9:
                return "申诉未通过";
            default:
                break;
        }
        return "";
    }

    /**
     * @param time
     * @return
     * @方法说明:倒计时转换
     * @方法名称:changeTime
     * @作者:倪顺
     * @返回值:String
     */
    public static String changeTime(long time) {
        StringBuilder result = new StringBuilder("");
        long second = time % 60;
        long hour = time / 3600;
        long munite = (time - hour * 3600) / 60;
        if (hour != 0) {
            result.append(hour + "小时");
        }
        if (munite != 0) {
            result.append(munite + "分钟");
        }
        if (second != 0) {
            result.append(second + "秒");
        }
        if (hour == 0 && munite == 0 && second == 0) {
            result.append("抢购中");
        } else {
            result.append("后开始");
        }
        return result.toString();
    }

    /**
     * @param time
     * @return
     * @方法说明:倒计时转换
     * @方法名称:changeTime
     * @作者:倪顺
     * @返回值:String
     */
    public static String downTime(long time) {
        StringBuilder result = new StringBuilder("");
        long day = time / 86400;
        long second = time % 60;
        long hour = (time - day * 86400) / 3600;
        long munite = (time - day * 86400 - hour * 3600) / 60;
        if (day != 0) {
            result.append(day + "天");
        }
        if (hour != 0) {
            result.append(hour + "小时");
        }
        if (munite != 0) {
            result.append(munite + "分钟");
        }
        if (second != 0) {
            result.append(second + "秒");
        }
        return result.toString();
    }

    public static String endTimeLimit(long time) {
        StringBuilder result = new StringBuilder("任务提交时间剩余：");
        long day = time / 86400;
        long second = time % 60;
        long hour = (time - day * 86400) / 3600;
        long munite = (time - day * 86400 - hour * 3600) / 60;
        result.append(day + "天");
        result.append(hour + "小时");
        result.append(munite + "分钟");
        result.append(second + "秒");
        return result.toString();
    }

    public static String endTimeLimit(String title, long time) {
        StringBuilder result = new StringBuilder(title);
        long day = time / 86400;
        long second = time % 60;
        long hour = (time - day * 86400) / 3600;
        long munite = (time - day * 86400 - hour * 3600) / 60;
        result.append(day + "天");
        result.append(hour + "小时");
        result.append(munite + "分钟");
        result.append(second + "秒");
        return result.toString();
    }

    /**
     * @return
     * @方法说明:获取网络时间
     * @方法名称:getWebTime
     * @作者:倪顺
     * @返回值:String
     */
    public static String getWTime() {
        URL url;
        String stime = "";
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect(); // 发出连接
            long ld = uc.getDate(); // 取得网站日期时间
            Date date = new Date(ld); // 转换为标准时间对象
            SimpleDateFormat sdformat = new SimpleDateFormat(DATE_PATTERN_2);
            stime = sdformat.format(date);
            if ("1970".equals(stime.substring(0, 4))) {
                stime = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stime;
    }

    public static String getWebTime() {
        String time = "";
        do {
            time = getWTime();
        } while (time.length() == 0);
        return time;
    }

    /**
     * 判断是否安装程序
     *
     * @param context
     * @param packageName 程序包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
