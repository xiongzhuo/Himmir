package com.himmiractivity.Utils;

import java.io.IOException;
import java.net.Socket;

public class SocketSingle {
    private static Socket instance = null;

    public static Socket getInstance(String ip, int port, boolean isTool) {
        if (instance == null || isTool) {                              //line 12
            try {
                instance = new Socket(ip, port);          //line 13
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

}
