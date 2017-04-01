package com.himmiractivity.liuxing_scoket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketServerTherd extends Thread {
    Socket socket = null;

    public SocketServerTherd(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        DataOutputStream out = null;
        DataInputStream input = null;
        byte[] tempbytes = null;
        int tmeplen = 0;
        try {
            input = new DataInputStream(socket.getInputStream());
            // out= new DataOutputStream(socket.getOutputStream());
            byte[] inData = new byte[1024];
            while (true) {
                int len = input.read(inData);
                byte[] bytes = new byte[len];
                System.arraycopy(inData, 0, bytes, 0, len);
                if (tmeplen > 0) {
                    byte[] inUnPacketData = new byte[tmeplen + len];
                    System.arraycopy(tempbytes, 0, inUnPacketData, 0, tmeplen);
                    System.arraycopy(bytes, 0, inUnPacketData, tmeplen - 1, len);
//                    tempbytes = Protocol.DataUnPacket_Server(bytes, socket,ha);
                } else {
//                    tempbytes = Protocol.DataUnPacket_Server(bytes, socket);
                    tmeplen = tempbytes.length;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
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
