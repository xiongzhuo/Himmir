package com.himmiractivity.Utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 判断是否是手机号
     *
     * @param str 验证字符串
     * @return true/false
     */
    public static boolean isMobileNo(String str) {
        Boolean isMobileNo = false;
        try {
            Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isMobileNo;
    }

    public static ArrayList<String> getSubString(String string, String split) throws Exception {
        StringTokenizer token = new StringTokenizer(string, split);
        ArrayList<String> arr = new ArrayList<String>();
        while (token.hasMoreTokens()) {
            arr.add(token.nextToken());
        }
        return arr;
    }

    /**
     * 判断邮箱是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 判断是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String retrrnErn(Map<String, String> jsonStr) {
//		String params = "";
//		for (Map.Entry<String, String> p : map.entrySet()) {
//			params += p.getKey()+ "="+p.getValue()+"&";
//		}
//		LogUtil.e("params",params);
        Set<String> set = jsonStr.keySet();
        String sign = "";
        JSONObject jsonObject = new JSONObject();
        String[] array = new String[set.size()];
        String[] keys = set.toArray(array);
        Arrays.sort(keys);
        for (int i = 0; i < keys.length; i++) {
            String value = jsonStr.get(keys[i]);
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            if (i > 0) {

                sign += "&" + keys[i] + "=" + value;
            } else {
                sign += keys[i] + "=" + value;
            }
//            String mess = jsonStr.get(keys[i]).toString();
//
//            try {
//                mess = new String(mess.getBytes(), "UTF-8").toString();
//                if (!"sign_key".equals(keys[i])) {
//                    jsonObject.put(keys[i], mess);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return sign;
    }

    public static String toCalendar(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 判断是否是字符和数字的组合
     *
     * @param str 验证字符串
     * @return true/false
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 日期格式处理
     */
    public static String toMMCDate(String date) {
        // Pattern p=Pattern.compile("/[\\d\\w]{3,}");
        // Matcher m=p.matcher(date);
        // date=m.replaceAll("").trim();
        // String[] dates=date.split("/");
        // StringBuilder sb=new StringBuilder(dates[0]);
        // sb.append("年").append(dates[1]).append("月");
        // return sb.toString();
        return date;
    }

    /**
     * 日期格式处理
     */
    public static String toMMCDate1(String date) {
        date.trim();
        int i = date.indexOf(" ");
        if (i != -1) {
            return date.substring(0, i + 1);
        }
        return date;
    }

    /**
     * 追加字符串
     *
     * @param src 目标字符串
     * @param add 追加字符串
     * @return 合并后字符串
     */
    public static String appendString(String src, String add) {
        if (src.indexOf(add) == -1) {
            src += add;
        }
        return src;
    }

    /**
     * 从JSON对象获取指定键值
     *
     * @param jo    目标JSON对象
     * @param key   键名
     * @param value 默认值
     * @param add   追加字符串
     * @return 处理后字符串
     */
    public static String getJSONString(JSONObject jo, String key, String value,
                                       String add) {
        String result;
        try {
            result = jo.getString(key).trim();
            if (result == null || result == "null" || result.length() < 1) {
                return value;
            } else {
                if (add != null) {
                    return appendString(result, add);
                } else {
                    return result;
                }
            }
        } catch (JSONException e) {
            return value;
        }
    }

    /**
     * 获取字符串
     *
     * @param src   原始字符串
     * @param value 默认值
     * @param add   追加值
     * @return 字符串
     */
    public static String getString(String src, String value, String add) {
        if (src == "" || src == null || src.length() < 1) {
            return value;
        } else {
            if (add != null) {
                return appendString(src, add);
            } else {
                return src;
            }
        }
    }

    /**
     * 去除字符串的BOM头
     *
     * @param arg 待处理字符串
     * @return 处理后字符串
     */
    public static String removeBOM(String arg) {
        if (arg != null && arg.startsWith("\ufeff")) {
            return arg.substring(1);
        } else {
            return arg;
        }
    }


    /**
     * 设置TextView显示两种字体颜色跟大小
     *
     * @param str1     前段字体
     * @param str2     后端字体
     * @param color1   前段字体颜色
     * @param color2   后段字体颜色
     * @param textView
     */
    public static void setSpannableString(String str1, String str2, int color1, int color2, TextView textView) {
        SpannableString ss = new SpannableString(str1 + str2);
        ss.setSpan(new RelativeSizeSpan(1.0f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(1.1f), str1.length(), str1.length() + str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(color1), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(color2), str1.length(), str1.length() + str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.append(ss);
        textView.setText(ss);
    }

    public static void setSpannableString(String[] str, int[] color, TextView textView) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }
        SpannableString ss = new SpannableString(sb.toString());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (i == 0) {
                ss.setSpan(new ForegroundColorSpan(color[i]), 0, str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ss.setSpan(new ForegroundColorSpan(color[i]), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            ss.setSpan(new RelativeSizeSpan(1.0f), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            stringBuffer.append(str[i]);
        }
        textView.append(ss);
        textView.setText(ss);
    }

    public static void setSpannableString(String[] str, int[] color, float[] size, TextView textView) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }
        SpannableString ss = new SpannableString(sb.toString());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            ss.setSpan(new RelativeSizeSpan(size[i]), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(color[i]), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            stringBuffer.append(str[i]);
        }
        textView.append(ss);
        textView.setText(ss);
    }

    public static void setSpannableString(String[] str, float[] size, TextView textView) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sb.append(str[i]);
        }
        SpannableString ss = new SpannableString(sb.toString());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            ss.setSpan(new RelativeSizeSpan(size[i]), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//    			ss.setSpan(new ForegroundColorSpan(color[i]), stringBuffer.length(), stringBuffer.length() + str[i].length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            stringBuffer.append(str[i]);
        }
        textView.append(ss);
        textView.setText(ss);
    }

    public static final String RICHTEXT_STRING = "string";
    public static final String RICHTEXT_COLOR = "color";
    public static final String RICHTEXT_SIZE = "size";
    public static final String RICHTEXT_RSIZE = "relativesize";
    public static final String RICHTEXT_DELETE = "delete";


    /**
     * 根据传入的hashmaplist组成富文本返回,key在RichTextUtil里
     *
     * @param list
     * @return
     */
    public static SpannableStringBuilder getSpannableStringFromList(List<HashMap<String, Object>> list) {

        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> map = list.get(i);
            try {
                String st = (String) map.get(RICHTEXT_STRING);
                ssb.append(st);
                int len = st.length();

                if (map.containsKey(RICHTEXT_COLOR)) {
                    int color = ((Integer) map.get(RICHTEXT_COLOR)).intValue();
                    ssb.setSpan(new ForegroundColorSpan(color), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                if (map.containsKey(RICHTEXT_SIZE)) {
                    int size = ((Integer) map.get(RICHTEXT_SIZE)).intValue();
                    ssb.setSpan(new AbsoluteSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (map.containsKey(RICHTEXT_RSIZE)) {
                    float size = ((Float) map.get(RICHTEXT_RSIZE)).floatValue();
                    ssb.setSpan(new RelativeSizeSpan(size), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (map.containsKey(RICHTEXT_DELETE)) {
                    ssb.setSpan(new StrikethroughSpan(), position, position + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                // android.text.style.RelativeSizeSpan
                position = position + len;

            } catch (Exception e) {
                return null;
            }

        }

        return ssb;
    }


    /**
     * 验证网址Url
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    /**
     * 验证网盘
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IspanUrl(String str) {
        try {
//    		String regex = "^(http|https)[A-Za-z]{0,1}://";
            String regex = "^(http|https)://(.)*";
            return match(regex, str);
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
