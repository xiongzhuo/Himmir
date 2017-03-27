package com.himmiractivity.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

/**
 * 时间计算类
 *
 * @author DengGuang
 */
@SuppressLint("SimpleDateFormat")
public class DatetimeUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 计算时间方法
     *
     * @return
     */
    public static long subDay(String beginDayStr, String endDayStr) {
        Date beginDate = null;
        Date endDate = null;

        long day = 0;
        try {
            beginDate = sdf.parse(beginDayStr);
            endDate = sdf.parse(endDayStr);

            // 计算天数
            day = (endDate.getTime() - beginDate.getTime())
                    / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 当天入住、当天离开算1天时间
        if (day == 0) {
            day = 1;
        }

        return day;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayStr() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMddhhmmss");

        // 订单编号
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayDate2() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd");

        // 订单编号
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyyMMdd");

        // 订单编号
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    public static String getTodayF() {
        SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        // 订单编号
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        String todayStr = format4.format(today);
        return todayStr;
    }

    /****
     * 获取系统当前的日期
     *
     * @return
     * @author chenjianfan
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public static String getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getToday());
        int week = cal.get(Calendar.DAY_OF_WEEK);
        String strWeek = "星期天";
        switch (week) {
            case 1:
                strWeek = "星期天";
                break;
            case 2:
                strWeek = "星期一";
                break;
            case 3:
                strWeek = "星期二";
                break;
            case 4:
                strWeek = "星期三";
                break;
            case 5:
                strWeek = "星期四";
                break;
            case 6:
                strWeek = "星期五";
                break;
            case 7:
                strWeek = "星期六";
                break;
            default:
                strWeek = "星期天";
                break;
        }
        return strWeek;
    }

    /****
     * 获取系统当前的时间
     *
     * @return
     * @author chenjianfan
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 格式转换
     *
     * @param str
     * @return
     */
    public static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToString(int time) {
        Date d = new Date(time * 1000L);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringTwo(int time) {
        Date d = new Date(time * 1000L);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    /* 时间戳转换成字符窜 */
    public static String getDateToStringTwo1(int time) {
        Date d = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        return sdf.format(d);
    }

    /**
     * 获得当天时间
     *
     * @param date
     * @param iDaydiff
     * @return
     */
    public static String getDay(Date date, int iDaydiff) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, iDaydiff);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static Date getToday() {
        /** 定义日期对象 */
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        return today;
    }

}
