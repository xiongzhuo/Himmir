package com.himmiractivity.Utils;

import com.google.gson.Gson;
import com.himmiractivity.entity.JsonResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json转换工具类
 *
 * @author yb
 */
public class JsonUtils {

    private static Gson gson;
    private static Map mapresult = new HashMap();

    static {
        gson = new Gson();
    }

    /**
     * json字符串转单个简单对象
     *
     * @param jsonStr
     * @param t       Order.class
     * @return
     * @throws Exception
     */
    public static <T> T getObject(String jsonStr, Class<T> t) throws Exception {
        return gson.fromJson(jsonStr, t);
    }

    /**
     * 将Json对象转换成Map
     * <p>
     * json对象
     *
     * @return Map对象
     * @throws JSONException
     */
    public static Map toMap(String jsonString) {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator iterator = jsonObject.keys();
            String key = null;
            String value = null;

            while (iterator.hasNext()) {

                key = (String) iterator.next();

                value = jsonObject.getString(key);

                mapresult.put(key, value);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mapresult;
    }

    /**
     * json字符串转单个复杂对象
     *
     * @param jsonStr
     * @param type    new TypeToken<JsonResult<Order>>() { }.getType()
     * @return
     * @throws Exception
     */
    public static <T> T getObject(String jsonStr, Type type) {
        return gson.fromJson(jsonStr, type);
    }

    public static String toString(Object t) {
        return gson.toJson(t);
    }

    public static JsonResult parseJson(String json, Type type) throws JSONException {
        JsonResult jsonResult = new JsonResult();
        JSONObject jsonObject = new JSONObject(json);
        boolean flag = jsonObject.optBoolean("flag", false);
        String msg = jsonObject.optString("msg", "");
        if (flag) {
            String obj = jsonObject.optString("data");
            Object o = getObject(obj, type);
            jsonResult.setData(o);
        } else {
            jsonResult.setData(null);
        }
        jsonResult.setMsg(msg);
        jsonResult.setFlag(flag);
        return jsonResult;
    }

}
