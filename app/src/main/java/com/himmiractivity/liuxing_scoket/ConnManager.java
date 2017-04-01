package com.himmiractivity.liuxing_scoket;


import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author chenlin
 * @version 1.0
 * @描述 使用socket实现长连接
 * @项目名称 App_Chat
 * @包名 com.android.chat.utils
 * @类名 TcpUtil
 * @date 2012年6月26日 下午4:06:43
 */
public class ConnManager {
    protected static final int STATE_FROM_SERVER_OK = 0;
    private static String dsName = "192.168.0.122";
    private static int dstPort = 8800;
    private static Socket socket;

    private static ConnManager instance;

    private ConnManager() {
    }

    public static ConnManager getInstance() {
        if (instance == null) {
            synchronized (ConnManager.class) {
                if (instance == null) {
                    instance = new ConnManager();
                }
            }
        }
        return instance;
    }

    /**
     * 连接
     *
     * @return
     */
    public boolean connect(final Handler handler) {

        if (socket == null || socket.isClosed()) {
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        socket = new Socket(dsName, dstPort);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException("连接错误: " + e.getMessage());
                    }

                    try {
                        // 输入流，为了获取客户端发送的数据
                        InputStream is = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            final String result = new String(buffer, 0, len);

                            Message msg = Message.obtain();
                            msg.obj = result;
                            msg.what = STATE_FROM_SERVER_OK;
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("getInputStream错误: " + e.getMessage());
                    }

                }
            }).start();
        }

        return true;
    }

    /**
     * 连接
     */
    public void connect() {
        if (socket == null || socket.isClosed()) {

            try {
                socket = new Socket(dsName, dstPort);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException("连接错误: " + e.getMessage());
            }

            try {
                // 输入流，为了获取客户端发送的数据
                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    final String result = new String(buffer, 0, len);
                    if (mListener != null) {
                        mListener.pushData(result);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("getInputStream错误: " + e.getMessage());
            }

        }

    }

    /**
     * 发送信息
     */
    public void sendMessage(boolean isOff) {
        OutputStream os = null;
        try {
            if (socket != null) {
                os = socket.getOutputStream();
                os.write(Protocol.PowerSwitch(isOff));
                os.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("发送失败:" + e.getMessage());
        }
    }

    /**
     * 发送信息
     *
     * @param auth
     */
    public void sendAuth(String auth) {
        OutputStream os = null;
        try {
            if (socket != null) {
                os = socket.getOutputStream();
                os.write(auth.getBytes());
                os.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("发送失败:" + e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public void disConnect() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭异常:" + e.getMessage());
            }
            socket = null;
        }
    }

    public interface ConnectionListener {
        void pushData(String str);
    }

    private ConnectionListener mListener;

    public void setConnectionListener(ConnectionListener listener) {
        this.mListener = listener;
    }
}
