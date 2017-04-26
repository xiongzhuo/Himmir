package com.himmiractivity.mining.app.zxing;

import android.os.Handler;
import android.util.Log;

import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.service.Protocal;

import java.net.Socket;

/**
 * Created by Administrator on 2017/3/30.
 */

public class ScoketOFFeON {
    //发送查询
    public static void sendMessage(Socket s, Protocal p, String mac) throws Exception {
        // 2.创建Protocal对像
        if (p == null) {
            p = new Protocal();
        }
        // 3.用Protocal生成并发送请求数据
        Log.d("ConnectionManager", "AbsClient*****发送请求");
        p.sendRequest(s.getOutputStream(), mac);
        Log.d("ConnectionManager", "AbsClient*****请求发送成功");
    }

    //发送风量
    public static void sendBlowingRate(Socket s, Protocal p, String mac, int volum) throws Exception {
        try {
            // 2.创建Protocal对像
            if (p == null) {
                p = new Protocal();
            }
            // 3.用Protocal生成并发送请求数据
            Log.d("ConnectionManager", "AbsClient*****发送请求");
            p.sendBlowingRate(s.getOutputStream(), mac, volum);
            Log.d("ConnectionManager", "AbsClient*****请求发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送校时
    public static void sendTimingMessage(Socket s, Protocal p, String mac) throws Exception {
        // 2.创建Protocal对像
        if (p == null) {
            p = new Protocal();
        }
        // 3.用Protocal生成并发送请求数据
        Log.d("ConnectionManager", "AbsClient*****发送请求");
        p.sendTimingRequest(s.getOutputStream(), mac);
        Log.d("ConnectionManager", "AbsClient*****请求发送成功");
    }

    //发送定时
    public static void sendTimingCommand(Socket s, Protocal p, String mac, int timingMode, boolean t1Switch, boolean t2Switch, boolean t3Switch, int t1start, int t1Stop, int t2start, int t2Stop, int t3start, int t3Stop) throws Exception {
        // 2.创建Protocal对像
        if (p == null) {
            p = new Protocal();
        }
        // 3.用Protocal生成并发送请求数据
        Log.d("ConnectionManager", "AbsClient*****发送请求");
        p.sendTimingCommandRequest(s.getOutputStream(), mac, timingMode, t1Switch, t2Switch, t3Switch, t1start, t1Stop, t2start, t2Stop, t3start, t3Stop);
        Log.d("ConnectionManager", "AbsClient*****请求发送成功");
    }

    //发送智能
    public static void sendNoopsycheMode(Socket s, Protocal p, String mac, boolean muteMode, boolean coMode, boolean pmMode, int coNumber, int pmNumber) throws Exception {
        // 2.创建Protocal对像
        if (p == null) {
            p = new Protocal();
        }
        // 3.用Protocal生成并发送请求数据
        Log.d("ConnectionManager", "AbsClient*****发送请求");
        p.sendNoopsycheMode(s.getOutputStream(), mac, muteMode, coMode, pmMode, coNumber, pmNumber);
        Log.d("ConnectionManager", "AbsClient*****请求发送成功");
    }

    //发送开关
    public static void sendMessage(Socket s, Protocal p, String mac, boolean isoff) throws Exception {
        // 2.创建Protocal对像
        if (p == null) {
            p = new Protocal();
        }
        // 3.用Protocal生成并发送请求数据
        Log.d("ConnectionManager", "AbsClient*****发送请求");
        p.sendRequest(s.getOutputStream(), mac, isoff);
        Log.d("ConnectionManager", "AbsClient*****请求发送成功");
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
