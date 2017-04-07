package com.himmiractivity.service;

import android.os.Handler;
import android.util.Log;

import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.liuxing_scoket.Protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Protocal {

    // 保存从服务器响应的结果
//    private String data;

//    public String getData() {
//        return data;
//    }

    /********
     * 发送开关机
     ***************************************************/
    /**
     * 单例对象实例
     */
    private static Protocal instance = null;

    public static Protocal getInstance() {
        if (instance == null) {                              //line 12
            instance = new Protocal();          //line 13

        }
        return instance;

    }

    public void sendRequest(OutputStream out, String mac, boolean isoff) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + isoff);
        out.write(Protocol.PowerSwitchByMac(isoff, mac));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    //风量
    public void sendBlowingRate(OutputStream out, String mac, int volum) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + volum);
        out.write(Protocol.adjustAirVolum(mac, volum));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    /********
     * 发送查询指令
     ***************************************************/
    public void sendRequest(OutputStream out, String mac) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + mac);
        out.write(Protocol.questServer(mac));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    /********
     * 发送校时指令
     ***************************************************/
    public void sendTimingRequest(OutputStream out, String mac) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + mac);
        out.write(Protocol.timingPlayload(mac));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    /********
     * 发送定时指令
     ***************************************************/
    public void sendTimingCommandRequest(OutputStream out, String mac, int timingMode, boolean t1Switch, boolean t2Switch, boolean t3Switch, int t1start, int t1Stop, int t2start, int t2Stop, int t3start, int t3Stop) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + mac);
        out.write(Protocol.TimingCommand(mac, timingMode, t1Switch, t2Switch, t3Switch, t1start, t1Stop, t2start, t2Stop, t3start, t3Stop));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    /********
     * 发送智能指令
     ***************************************************/
    public void sendNoopsycheMode(OutputStream out, String mac, boolean muteMode, boolean coMode, boolean pmMode, int coNumber, int pmNumber) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in" + mac);
        out.write(Protocol.noopsycheMode(mac, muteMode, coMode, pmMode, coNumber, pmNumber));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    public void sendRequest(OutputStream out, DataServerBean dataServerBean, String seriesNumber, String mac) throws Exception {
        Log.d("ConnectionManager", "AbsClient===in");
        out.write(Protocol.sensitize(dataServerBean, seriesNumber, mac));
        out.flush();
//        if (null != out) {
//            out.close();
//        }
    }

    /***********
     * 接收并解析响应数据
     ***********************************/
    public void receiveResponse(Socket socket, Handler handler) throws Exception {
        DataInputStream input = null;
        byte[] tempbytes = null;
        int tmeplen = 0;
        try {
            Log.d("ConnectionManager", "socket" + socket.toString());
            input = new DataInputStream(socket.getInputStream());
            byte[] inData = new byte[1024];
            while (true) {
                Log.d("ConnectionManager", "read" + inData.length);
                int len = input.read(inData);
                Log.d("ConnectionManager", "过来了");
                byte[] bytes = new byte[len];
                System.arraycopy(inData, 0, bytes, 0, len);
                if (tmeplen > 0) {
                    Log.d("ConnectionManager", "过来了");
                    byte[] inUnPacketData = new byte[tmeplen + len];
                    System.arraycopy(tempbytes, 0, inUnPacketData, 0, tmeplen);
                    System.arraycopy(bytes, 0, inUnPacketData, tmeplen - 1, len);
                    tempbytes = Protocol.DataUnPacket_Server(bytes, socket, handler);
                } else {
                    Log.d("ConnectionManager", "过来了");
                    tempbytes = Protocol.DataUnPacket_Server(bytes, socket, handler);
                    tmeplen = tempbytes.length;
                }
            }


        } catch (SocketException e) {
            Log.d("ConnectionManager", "Socket失败了");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("ConnectionManager", "失败了");
            e.printStackTrace();

        } finally {
            Log.d("ConnectionManager", "失败了");
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }
}
