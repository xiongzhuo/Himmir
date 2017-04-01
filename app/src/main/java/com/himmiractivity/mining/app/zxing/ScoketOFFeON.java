package com.himmiractivity.mining.app.zxing;

import android.os.Handler;
import android.util.Log;

import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.liuxing_scoket.Protocal;

import java.net.Socket;

/**
 * Created by Administrator on 2017/3/30.
 */

public class ScoketOFFeON {
    //发送查询
    public static void sendMessage(Socket s, Protocal p, String mac) {
        try {
            // 2.创建Protocal对像
            if (p == null) {
                p = new Protocal();
            }
            // 3.用Protocal生成并发送请求数据
            Log.d("ConnectionManager", "AbsClient*****发送请求");
            p.sendRequest(s.getOutputStream(), mac);
            Log.d("ConnectionManager", "AbsClient*****请求发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送校时
    public static void sendTimingMessage(Socket s, Protocal p, String mac) {
        try {
            // 2.创建Protocal对像
            if (p == null) {
                p = new Protocal();
            }
            // 3.用Protocal生成并发送请求数据
            Log.d("ConnectionManager", "AbsClient*****发送请求");
            p.sendTimingRequest(s.getOutputStream(), mac);
            Log.d("ConnectionManager", "AbsClient*****请求发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送开关
    public static void sendMessage(Socket s, Protocal p, String mac, boolean isoff) {
        try {
            // 2.创建Protocal对像
            if (p == null) {
                p = new Protocal();
            }
            // 3.用Protocal生成并发送请求数据
            Log.d("ConnectionManager", "AbsClient*****发送请求");
            p.sendRequest(s.getOutputStream(), mac, isoff);
            Log.d("ConnectionManager", "AbsClient*****请求发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //激活
    public static void sendMessage(Socket s, Protocal p, DataServerBean dataServerBean, String seriesNumber, String mac) {
        try {
            // 2.创建Protocal对像
            if (p == null) {
                p = new Protocal();
            }
            // 3.用Protocal生成并发送请求数据
            Log.d("ConnectionManager", "AbsClient*****发送请求");
            p.sendRequest(s.getOutputStream(), dataServerBean, seriesNumber, mac);
            Log.d("ConnectionManager", "AbsClient*****请求发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //接受数据
    public static void receMessage(Socket s, Protocal p, Handler handler) {
        try {
            if (p == null) {
                p = new Protocal();
            }
            // 4.用Protocal接收并解析响应数据
            Log.d("ConnectionManager", "AbsClient*****接收响应");
            p.receiveResponse(s, handler);
            Log.d("ConnectionManager", "AbsClient*****响应接收完成");
            // 2.创建Protocal对像
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
