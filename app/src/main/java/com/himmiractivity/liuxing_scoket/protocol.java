package com.himmiractivity.liuxing_scoket;

import android.os.Handler;
import android.util.Log;

import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.PmAllData;
import com.himmiractivity.interfaces.StatisConstans;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Protocol {
    public interface MsgType {
        byte DEV_SERVER_CMD_LOGIN = (byte) 0;
        byte SERVER_DEV_ACK_LOGIN = (byte) 1;
        byte DEV_SERVER_CMD_HEARTBEAT = (byte) 2;
        byte SERVER_DEV_ACK_HEARTBEAT = (byte) 3;
        byte DEV_SERVER_CMD_REPORT = (byte) 4;
        byte SERVER_DEV_CMD_CONTROL = (byte) 5;
        byte DEV_SERVER_ACK_CONTROL = (byte) 6;
        byte SERVER_DEV_CMD_SETUP = (byte) 7;
        byte DEV_SERVER_ACK_SETUP = (byte) 8;
        byte SERVER_DEV_CMD_DEMAND = (byte) 9;
        byte DEV_SERVER_ACK_DEMAND = (byte) 10;
        byte APP_DEV_CMD_ACTIVATE = (byte) 11;
        byte DEV_APP_ACK_ACTIVATE = (byte) 12;
        byte SERVER_DEV_CMD_READY = (byte) 13;
        byte APP_DEV_CMD_CONFIG = (byte) 14;
        byte DEV_APP_ACK_CONFIG = (byte) 15;
        byte APP_DEV_CMD_SETUP = (byte) 16;
        byte DEV_APP_ACK_SETUP = (byte) 17;
        byte APP_DEV_CMD_DEMAND = (byte) 18;
        byte DEV_APP_ACK_DEMAND = (byte) 19;
        byte DEV_SERVER_CMD_HISTORY = (byte) 20;
        byte APP_SERVER_CMD_DEMAND = (byte) 21;  //21 ：app 向服务器查询
        byte SERVER_APP_ACK_DEMAND = (byte) 22;  //22 ：服务器回 app 回查询
        byte APP_SERVER_CMD_CONTROL = (byte) 23; //23 ：app 向服务器 发控制命令
        byte SERVER_APP_ACK_CONTROL = (byte) 24; //24 ：服务器
    }

    public static interface PayloardKey {
        byte D_POWER = (byte) 0x10;// 开关机
        byte D_MODE = (byte) 0x20; // 模式
        byte D_EXHAUST_MODE = (byte) 0x22;// 新排风模式
        byte D_EXHAUST_RATIO = (byte) 0x23; // 排风比例
        byte D_LCD_BACKLIGHT = (byte) 0x21; // 背光时间
        byte D_AUX_HEATING = (byte) 0x24; // 辅热温度值
        byte D_CONST_TEMP_MODE = (byte) 0x25; // 恒温模式
        byte D_CONST_HUMI_MODE = (byte) 0x26; // 恒湿模式
        byte D_CONST_TEMP_PARA = (byte) 0x27; // 恒温参数
        byte D_CONST_HUMI_PARA = (byte) 0x28; // 恒湿参数
        byte D_FAN_FREQ = (byte) 0x31; // 风机频率
        byte D_BLOWER_SPEED = (byte) 0x32; // 送风机转速
        byte D_EXHAUST_SPEED = (byte) 0x33; // 排风机转速
        byte D_BLOWING_RATE = (byte) 0x34; // 风量
        byte D_THAW_START_TEMP = (byte) 0x41; // 融冰启动温度
        byte D_THAW_STOP_TEMP = (byte) 0x42; // 融冰停止温度
        byte D_THAW_TIME = (byte) 0x43; // 融冰时间
        byte D_THAW_INV = (byte) 0x44; // 融冰间隔时间
        byte D_DUSTWIPER_CLEAR_PERIOD = (byte) 0x45; // 除尘器清洗周期
        byte D_DUSTWIPER_CLEAR_REMAIN = (byte) 0x46; // 除尘器清洗周期剩余时间
        byte D_TIME_MODE = (byte) 0x50; // 定时模式
        byte D_TIMER1 = (byte) 0x51; // 定时器1
        byte D_TIMER2 = (byte) 0x52; // 定时器2
        byte D_TIMER3 = (byte) 0x53; // 定时器3
        byte D_CO2_ADJ = (byte) 0x61; // CO2调节值
        byte D_FUNC_STATUS = (byte) 0x62; // 功能状态
        byte D_PM_ADJ = (byte) 0x63; // PM调节

        byte D_SENSOR_INDOOR_TEMP = (byte) 0x71; // 室内温度
        byte D_SENSOR_OUTDOOR_TEMP = (byte) 0x72; // 室外温度
        byte D_SENSOR_FAN_TEMP = (byte) 0x73; // 新风温度
        byte D_SENSOR_EXHAUST_TEMP = (byte) 0x74; // 排风温度
        byte D_CO2_THICKNESS = (byte) 0x75; // CO2浓度
        byte D_INDOOR_PM_THICKNESS = (byte) 0x76; // 室内颗粒物浓度
        byte D_OUTDOOR_PM_THICKNESS = (byte) 0x77; // 室外颗粒物浓度
        byte D_INDOOR_HUMI = (byte) 0x78; // 室内湿度
        byte D_SYSTEM_CLOCK = (byte) 0x01; // 系统时间校准
        byte D_FAULT_STATUS = (byte) 0x0f; // 故障状态

    }

    public static interface Key_IV {
        byte[] Public_Key = {0x3a, 0x46, 0x72, 0x2b, 0x45, 0x5d, 0x4b, 0x73, 0x7e, 0x65, 0x21,
                0x42, 0x6e, 0x6c, 0x46, 0x3d};
        byte[] Public_IV = {0x4f, 0x3c, 0x64, 0x39, 0x3a, 0x76, 0x2a, 0x32, 0x45, 0x3f, 0x41, 0x22,
                0x2e, 0x37, 0x2b, 0x5e};
        byte[] Private_KEY = {0x3a, 0x46, 0x72, 0x2b, 0x45, 0x5d, 0x4b, 0x73, 0x7e, 0x65, 0x21,
                0x42, 0x6e, 0x6c, 0x46, 0x3d};
        byte[] Private_IV = {0x4f, 0x3c, 0x64, 0x39, 0x3a, 0x76, 0x2a, 0x32, 0x45, 0x3f, 0x41,
                0x22, 0x2e, 0x37, 0x2b, 0x5e};
    }

    private static interface Packe_Def {
        String ConstHeader = "smart.hnliuxing.com";
        byte ConstHeaderLength = 15;
        byte ConstSaveDataLength = 4;
        byte DEV_OFF_ON = (byte) 0xea; // 设备在线状态
        byte DEV_Q_DATA = (byte) 0xda; // 设备所有状态
        //包类型
        short DIR_SVR_DEV = (short) 0xab00; // 服务器至设备
        short DIR_DEV_SVR = (short) 0xba00; // 设备至服务器
        short DIR_APP_DEV = (short) 0xcb00; // app至设备
        short DIR_DEV_APP = (short) 0xbc00; // 设备至app
        short DIR_APP_SVR = (short) 0xae00;// app至服务器
        short DIR_SVR_APP = (short) 0xea00;// 服务器至app
        short DIR_MASK = (short) 0xff00;

        short PACKET_TYPE_CMD = (short) 0x0000; // 命令包
        short PACKET_TYPE_ACK = (short) 0x0080; // 回应包
        short PACKET_TYPE_MASK = (short) 0x00f0;
        short ENCRYPT_TYPE_PUBLIC = (short) 0x0000; // 公钥加密
        short ENCRYPT_TYPE_ACTIVATE = (short) 0x0002; // 激活用密钥
        short ENCRYPT_TYPE_MASK = (short) 0x000f; // 密码MASK

        //包类型
        //1.Serverto Dev
        short ptype_Server_Dev_Cmd_Pub = (short) (DIR_SVR_DEV | PACKET_TYPE_CMD | ENCRYPT_TYPE_PUBLIC);
        short ptype_Server_Dev_Cmd_Act = (short) (DIR_SVR_DEV | PACKET_TYPE_CMD | ENCRYPT_TYPE_ACTIVATE);
        short ptype_Server_Dev_Ack_Pub = (short) (DIR_SVR_DEV | PACKET_TYPE_ACK | ENCRYPT_TYPE_ACTIVATE);
        short ptyte_App_Dev_Act_Act = (short) (DIR_APP_DEV | PACKET_TYPE_CMD | ENCRYPT_TYPE_PUBLIC);
        //2.Dev to Server
        short ptype_Dev_Server_Ack_Pub = (short) (DIR_DEV_SVR | PACKET_TYPE_ACK | ENCRYPT_TYPE_PUBLIC); //上报用什么？
        short ptype_Dev_Server_Cmd_Pub = (short) (DIR_DEV_SVR | PACKET_TYPE_CMD | ENCRYPT_TYPE_PUBLIC); //上报用什么？
        //3.server to app
        short ptype_Server_App_Ack_Pub = (short) (DIR_SVR_APP | PACKET_TYPE_ACK | ENCRYPT_TYPE_PUBLIC);
        short ptype_App_Server_Cmd_Pub = (short) (DIR_APP_SVR | PACKET_TYPE_ACK | ENCRYPT_TYPE_PUBLIC);

    }

    private static void test003() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        System.out.println("ByteBuffer :");
        System.out.println("capacity:" + buffer.capacity());
        buffer.put(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15});
        System.out.println("put byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15} into buffer.");
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        buffer.flip();// 数据由写转为读取  
        System.out.println("ByteBuffer 执行flip，转为读取");
        byte[] dst = new byte[10];
        buffer.get(dst, 0, dst.length);
        System.out.println(String.format(
                "byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dst[0], dst[1], dst[2],
                dst[3], dst[4], dst[5], dst[6], dst[7], dst[8], dst[9]));
        System.out.println("读取完10个字节的数据后:");
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        buffer.rewind();
        System.out.println("执行rewind，重新读取数据");
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        byte[] dt = new byte[10];
        buffer.get(dt, 0, dst.length);
        System.out.println(String.format(
                "byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dt[0], dt[1], dt[2],
                dt[3], dt[4], dt[5], dt[6], dt[7], dt[8], dt[9]));
        System.out.println("读取完10个字节的数据后:");
        System.out.println("limit:" + buffer.limit());
        System.out.println("position:" + buffer.position());
        System.out.println("在当前位置做标记mark");
        buffer.mark();
        buffer.get();
        buffer.get();
        buffer.get();
        System.out.println("读取3个字节后position:" + buffer.position());
        // buffer.rewind();  
        buffer.reset();
        System.out.println("执行reset后position的位置:" + buffer.position());
        // buffer.clear();  
        // System.out.println(buffer.get(3));  
        buffer.compact();
        System.out.println("取出10个字节后，执行完compact后ByteBuffer第一个字节:" + buffer.get(0));
    }
    //login 回包

    public static byte[] LoginAckPacket(byte[] time_stamp) {
        byte[] data = new byte[]{0x01};
        byte[] loginAckData = Packet_t(Packe_Def.ptype_Server_Dev_Ack_Pub, MsgType.SERVER_DEV_ACK_LOGIN, time_stamp, data);
        return loginAckData;
    }

    //取开关机payload
    public static byte[] getPowerSwitchPayload(boolean isOn) {
        if (isOn == true) {
            byte[] rett = {0x10, 0x01};
            return rett;
        } else {
            byte[] retf = {0x10, 0x00};
            return retf;
        }
    }

    //取激活payload
    public static byte[] getSensitize(DataServerBean dataServerBean, String seriesNumber, String mac) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //序列号
        byte[] snBytes = new byte[14];
        byte[] snTemp = DigitalTrans.stringToAsciiBytes(seriesNumber);
        int snLen = snTemp.length;
        System.arraycopy(snTemp, 0, snBytes, 0, snLen);
        buffer.put(snBytes);

        //主服务器
        byte[] mainServerBytes = new byte[32];
        byte[] mainServerTemp = DigitalTrans.stringToAsciiBytes(dataServerBean.getDataServerConfig().getPrimary_server_address());
        int mainServerLen = mainServerTemp.length;
        System.arraycopy(mainServerTemp, 0, mainServerBytes, 0, mainServerLen);
        buffer.put(mainServerBytes);

        //主服务器端口号
        byte[] hostBytes = new byte[2];
        byte[] hostTemp = DigitalTrans.unsignedShortToByte2_Litte(dataServerBean.getDataServerConfig().getPrimary_server_port());
        int hostLen = hostTemp.length;
        System.arraycopy(hostTemp, 0, hostBytes, 0, hostLen);
        buffer.put(hostBytes);

        //主服务器链接类型
        byte[] typeBytes = new byte[1];
        byte[] typeTemp = new byte[1];
        typeTemp[0] = (byte) dataServerBean.getDataServerConfig().getPrimary_server_connection();
        int typeLen = typeTemp.length;
        System.arraycopy(typeTemp, 0, typeBytes, 0, typeLen);
        buffer.put(typeBytes);

        //次服务器地址
        byte[] degreeBytes = new byte[32];
        byte[] degreeTemp = DigitalTrans.stringToAsciiBytes(dataServerBean.getDataServerConfig().getSecondary_server_address());
        int degreeLen = degreeTemp.length;
        System.arraycopy(degreeTemp, 0, degreeBytes, 0, degreeLen);
        buffer.put(degreeBytes);

        //次服务器端口号
        byte[] degreeHostBytes = new byte[2];
        byte[] degreeHostTemp = DigitalTrans.unsignedShortToByte2_Litte(dataServerBean.getDataServerConfig().getSecondary_server_port());
        int degreeHostLen = degreeHostTemp.length;
        System.arraycopy(degreeHostTemp, 0, degreeHostBytes, 0, degreeHostLen);
        buffer.put(degreeHostBytes);

        //次服务器链接类型
        byte[] degreeTypeBytes = new byte[1];
        byte[] degreeTypeTemp = new byte[1];
        degreeTypeTemp[0] = (byte) dataServerBean.getDataServerConfig().getSecondary_server_connection();
        int degreeTypeLen = degreeTypeTemp.length;
        System.arraycopy(degreeTypeTemp, 0, degreeTypeBytes, 0, degreeTypeLen);
        buffer.put(degreeTypeBytes);

        //心跳包内容
        byte[] heartBytes = new byte[16];
        byte[] heartTemp = DigitalTrans.StringToAsciiBytes(mac);
        int heartLen = heartTemp.length;
        System.arraycopy(heartTemp, 0, heartBytes, 0, heartLen);
        buffer.put(heartBytes);

        //心跳包间隔
        byte[] heartSpaceBytes = new byte[1];
        byte[] heartSpaceTemp = new byte[1];
        heartSpaceTemp[0] = (byte) dataServerBean.getDataServerConfig().getHeart_package_interval();
        int heartSpaceLen = heartSpaceTemp.length;
        System.arraycopy(heartSpaceTemp, 0, heartSpaceBytes, 0, heartSpaceLen);
        buffer.put(heartSpaceBytes);

        //服务器链接超时时间
        byte[] timeServerBytes = new byte[2];
        byte[] timeServerTemp = DigitalTrans.unsignedShortToByte2_Litte(dataServerBean.getDataServerConfig().getServer_connection_timeout());
        int timeServerLen = timeServerTemp.length;
        System.arraycopy(timeServerTemp, 0, timeServerBytes, 0, timeServerLen);
        buffer.put(timeServerBytes);

        //读设备状态间隔时间
        byte[] timeFlxingBytes = new byte[2];
        byte[] timeFlxingTemp = DigitalTrans.unsignedShortToByte2_Litte(dataServerBean.getDataServerConfig().getRead_device_interval());
        int timeFlxingLen = timeFlxingTemp.length;
        System.arraycopy(timeFlxingTemp, 0, timeFlxingBytes, 0, timeFlxingLen);
        buffer.put(timeFlxingBytes);

        //模块主动上报间隔时间
        byte[] timemodouBytes = new byte[2];
        byte[] timemodouTemp = DigitalTrans.unsignedShortToByte2_Litte(dataServerBean.getDataServerConfig().getModule_report_interval());
        int timemodouLen = timemodouTemp.length;
        System.arraycopy(timemodouTemp, 0, timemodouBytes, 0, timemodouLen);
        buffer.put(timemodouBytes);

        //校时间隔
        byte[] timeProofreadBytes = new byte[1];
        byte[] timeProofreadTemp = new byte[1];
        timeProofreadTemp[0] = (byte) dataServerBean.getDataServerConfig().getProofread_time_interval();
        int timeProofreadLen = timeProofreadTemp.length;
        System.arraycopy(timeProofreadTemp, 0, timeProofreadBytes, 0, timeProofreadLen);
        buffer.put(timeProofreadBytes);

        //上传阔值时间
        byte[] timeUploadBytes = new byte[1];
        byte[] timeUploadTemp = new byte[1];
        timeUploadTemp[0] = (byte) dataServerBean.getDataServerConfig().getUpload_threshold_time();
        int timeUploadLen = timeUploadTemp.length;
        System.arraycopy(timeUploadTemp, 0, timeUploadBytes, 0, timeUploadLen);
        buffer.put(timeUploadBytes);

        //AP模式开光
        byte[] timeAPBytes = new byte[1];
        byte[] timeAPTemp = new byte[1];
        timeAPTemp[0] = (byte) dataServerBean.getDataServerConfig().getAp_module_switch();
        int timeAPLen = timeAPTemp.length;
        System.arraycopy(timeAPTemp, 0, timeAPBytes, 0, timeAPLen);
        buffer.put(timeAPBytes);
        buffer.flip();
        int datalen = buffer.remaining();
        byte[] retData = new byte[datalen];
        buffer.get(retData);
        return retData;
    }

    //取开关机payload
    public static byte[] getPowerSwitchPayloadBymac(boolean isOn, String Mac) {
        if (Mac.length() > 12) {
            return null;
        }
        byte[] macCmdBytes = new byte[14];
        byte[] mac = DigitalTrans.stringToAsciiBytes(Mac);
        System.arraycopy(mac, 0, macCmdBytes, 0, Mac.length());
        if (isOn == true) {
            byte[] cmd = {0x10, 0x01};
            System.arraycopy(cmd, 0, macCmdBytes, 12, 2);
            return macCmdBytes;
        } else {
            byte[] cmd = {0x10, 0x00};
            System.arraycopy(cmd, 0, macCmdBytes, 12, 2);
            return macCmdBytes;
        }
    }

    //开关机数据包
    public static byte[] PowerSwitchByMac(boolean isOn, String Mac) {
        byte[] plBytes = getPowerSwitchPayloadBymac(isOn, Mac);
        System.out.println("plBytes:" + DigitalTrans.byte2hex(plBytes));
        byte[] sendBytes = Packet(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, plBytes);
        return sendBytes;
    }

    //开关机数据包
    public static byte[] PowerSwitch(boolean isOn) {
        byte[] plBytes = getPowerSwitchPayload(isOn);
        System.out.println(DigitalTrans.byte2hex(plBytes));
        byte[] sendBytes = Packet(Packe_Def.ptype_Server_Dev_Cmd_Pub, (byte) MsgType.SERVER_DEV_CMD_CONTROL, plBytes);
        return sendBytes;
    }

    //取校时payload
    public static byte[] timingPlayload(String mac) {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        byte[] macBytesTemp = DigitalTrans.StringToAsciiBytes(mac);
//        byte[] macBytes = new byte[12];
//        int macLen = macBytesTemp.length;
//        System.arraycopy(macBytesTemp, 0, macBytes, 0, macLen);
        byte[] keyBytes = new byte[]{0x01};
//        buffer.put(macBytes);
        buffer.put(keyBytes);
        buffer.flip();
        byte[] plBytes = new byte[buffer.remaining()];
        buffer.get(plBytes);
        System.out.println(DigitalTrans.byte2hex(plBytes));
        byte[] sendBytes = Packet_Mac(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, mac, plBytes);
        return sendBytes;
    }

    //智能指令
    public static byte[] noopsycheMode(String mac, boolean muteMode, boolean coMode, boolean pmMode, int coNumber, int pmNumber) {
        byte tb = (byte) 0x00;
        //定时器开关
        if (muteMode) {
            tb |= (byte) 0x01;
        }
        if (coMode) {
            tb |= (byte) (0x01 << 1);
        }
        if (pmMode) {
            tb |= (byte) (0x01 << 2);
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put(macBytes);
        buffer.put((byte) (0x20));
        buffer.put(tb);
        buffer.put((byte) 0x61);
        byte[] coBytes = new byte[2];
        byte[] coTemp = DigitalTrans.unsignedShortToByte2(coNumber);
        int coLen = coTemp.length;
        System.arraycopy(coTemp, 0, coBytes, 0, coLen);
        buffer.put(coBytes);
        buffer.put((byte) 0x63);
        byte[] pmBytes = new byte[2];
        byte[] pmTemp = DigitalTrans.unsignedShortToByte2(pmNumber);
        int pmLen = pmTemp.length;
        System.arraycopy(pmTemp, 0, pmBytes, 0, pmLen);
        buffer.put(pmBytes);
        buffer.flip();
        byte[] plBytes = new byte[buffer.remaining()];
        buffer.get(plBytes);
        byte[] sendBytes = Packet_Mac(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, mac, plBytes);
        return sendBytes;
    }

    //定时指令
    public static byte[] TimingCommand(String mac, int timingMode, boolean t1Switch, boolean t2Switch, boolean t3Switch, int t1start, int t1Stop, int t2start, int t2Stop, int t3start, int t3Stop) {
//        byte[] macBytesTemp = DigitalTrans.StringToAsciiBytes(mac);
//        byte[] macBytes = new byte[12];
//        int macLen = macBytesTemp.length;
//        System.arraycopy(macBytesTemp, 0, macBytes, 0, macLen);
        byte tb = (byte) 0x00;
        //mode设定
        switch (timingMode) {
            case 1:
                tb |= (byte) 0x01;
                break;
            case 2:
                tb |= (byte) 0x01 << 1;
                break;
            case 4:
                tb |= (byte) 0x01 << 2;
                break;
        }
        //定时器开关
        if (t1Switch) {
            tb |= (byte) 0x01 << 3;
        }
        if (t2Switch) {
            tb |= (byte) (0x01 << 4);
        }
        if (t3Switch) {
            tb |= (byte) (0x01 << 5);
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put(macBytes);
        buffer.put((byte) (0x50));
        buffer.put(tb);
        buffer.put((byte) 0x51);
        buffer.put((byte) (t1start / 100));
        buffer.put((byte) (t1start % 100));
        buffer.put((byte) (t1Stop / 100));
        buffer.put((byte) (t1Stop % 100));
        buffer.put((byte) 0x52);
        buffer.put((byte) (t2start / 100));
        buffer.put((byte) (t2start % 100));
        buffer.put((byte) (t2Stop / 100));
        buffer.put((byte) (t2Stop % 100));
        buffer.put((byte) 0x53);
        buffer.put((byte) (t3start / 100));
        buffer.put((byte) (t3start % 100));
        buffer.put((byte) (t3Stop / 100));
        buffer.put((byte) (t3Stop % 100));
        buffer.flip();
        byte[] plBytes = new byte[buffer.remaining()];
        buffer.get(plBytes);
        byte[] sendBytes = Packet_Mac(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, mac, plBytes);


        return sendBytes;
    }
//    public static byte[] timingPlayload(String mac) {
//        if (mac.length() > 12) {
//            return null;
//        }
//        byte[] macCmdBytes = new byte[13];
//        byte[] macs = DigitalTrans.stringToAsciiBytes(mac);
//        System.arraycopy(macs, 0, macCmdBytes, 0, mac.length());
//        byte[] cmd = {0x01};
//        System.arraycopy(cmd, 0, macCmdBytes, 12, 1);
//        byte[] sendBytes = Packet(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, macCmdBytes);
//        return sendBytes;
//    }

    //调整风量plaload
    public static byte[] adjustAirVolum(String mac, int volum) {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        byte[] macBytesTemp = DigitalTrans.StringToAsciiBytes(mac);
//        byte[] macBytes = new byte[12];
//        int macLen = macBytesTemp.length;
//        System.arraycopy(macBytesTemp, 0, macBytes, 0, macLen);
        byte[] keyBytes = new byte[]{0x31};
        byte[] volumBytes = new byte[]{(byte) volum};
//        buffer.put(macBytes);
        buffer.put(keyBytes);
        buffer.put(volumBytes);
        buffer.flip();
        byte[] plBytes = new byte[buffer.remaining()];
        buffer.get(plBytes);
        System.out.println(DigitalTrans.byte2hex(plBytes));
        byte[] sendBytes = Packet_Mac(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_CONTROL, mac, plBytes);
        return sendBytes;
    }

    //查询数据包
    public static byte[] quest() {
        byte[] plBytes = new byte[]{};
        byte[] sendBytes = Packet(Packe_Def.ptype_Server_Dev_Cmd_Pub, (byte) MsgType.APP_DEV_CMD_DEMAND, plBytes);
        return sendBytes;
    }

    //查询数据包
    public static byte[] questServer(String mac) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] macBytes = new byte[12];
        byte[] macBytesTemp = DigitalTrans.StringToAsciiBytes(mac);
        int macLen = macBytesTemp.length;
        System.arraycopy(macBytesTemp, 0, macBytes, 0, macLen);
        byte[] keyBytes = new byte[]{(byte) 0xda};
        buffer.put(macBytes);
        buffer.put(keyBytes);
        buffer.flip();
        byte[] plBytes = new byte[buffer.remaining()];
        buffer.get(plBytes);
        byte[] sendBytes = Packet(Packe_Def.ptype_App_Server_Cmd_Pub, (byte) MsgType.APP_SERVER_CMD_DEMAND, plBytes);
        return sendBytes;
    }

    //激活数据包
    public static byte[] sensitize(DataServerBean dataServerBean, String seriesNumber, String mac) {
        try {
            byte[] plBytes = getSensitize(dataServerBean, seriesNumber, mac);
            byte[] sendBytes = Packet(Packe_Def.ptyte_App_Dev_Act_Act, (byte) MsgType.APP_DEV_CMD_ACTIVATE, plBytes);

            return sendBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //数据打包
    public static byte[] Packet_t(short packTpe, byte cmdType, byte[] timeStamp, byte[] payLoadSrc) {
        //数据组包--时间戳  命令类型  playload
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] cmdBytes = {cmdType};
        buffer.put(timeStamp);
        buffer.put(cmdBytes);
        buffer.put(payLoadSrc);

        //取校验码
        buffer.flip();
        byte[] tmpBytes = new byte[buffer.remaining()];
        buffer.get(tmpBytes);
        byte xorByte = xor(tmpBytes);
        //存校验码
        buffer.clear();
        buffer.put(tmpBytes);
        buffer.put(xorByte);

        //取数据加密码
        buffer.flip();
        byte[] inEncryptBytes = new byte[buffer.remaining()];
        buffer.get(inEncryptBytes);
        byte[] encrypOutBytes = encrypt_CRT(Key_IV.Public_Key, Key_IV.Public_IV, inEncryptBytes);

        //取数据加密数据长度
        int len = encrypOutBytes.length;
        //最后组包
        ByteBuffer Endbuffer = ByteBuffer.allocate(1024);
        byte[] cmdTypeBytes = DigitalTrans.unsignedShortToByte2_Litte(packTpe);
        byte[] lenBytes = DigitalTrans.unsignedShortToByte2_Litte(len);
        Endbuffer.put(cmdTypeBytes);
        Endbuffer.put(lenBytes);
        Endbuffer.put(encrypOutBytes);

        //取完成处理的包
        Endbuffer.flip();
        byte[] SendBytes = new byte[Endbuffer.remaining()];
        Endbuffer.get(SendBytes);
        return SendBytes;
    }

    //数据打包
    public static byte[] Packet(short packTpe, byte cmdType, byte[] payLoadSrc) {
        //数据组包--时间戳  命令类型  playload
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long time_stamp = System.currentTimeMillis();
        //long time_stamp = 123456;
        byte[] timeStampBytes = DigitalTrans.unsignedIntToByte4_Litte(time_stamp);
        byte[] cmdBytes = {cmdType};
        buffer.put(timeStampBytes);
        buffer.put(cmdBytes);
        buffer.put(payLoadSrc);

        //取校验码
        buffer.flip();
        int len = buffer.remaining();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        byte xorByte = xor(tmpBytes);

        //存校验码
        buffer.clear();
        buffer.put(tmpBytes);
        buffer.put(xorByte);

        //取数据加密码
        buffer.flip();
        byte[] inEncryptBytes = new byte[buffer.remaining()];
        buffer.get(inEncryptBytes);
        byte[] encrypOutBytes = encrypt_CRT(Key_IV.Public_Key, Key_IV.Public_IV, inEncryptBytes);

        //取数据加密数据长度
        int len1 = encrypOutBytes.length;
        //最后组包
        ByteBuffer Endbuffer = ByteBuffer.allocate(1024);
        byte[] cmdTypeBytes = DigitalTrans.unsignedShortToByte2_Litte(packTpe);
        byte[] lenBytes = DigitalTrans.unsignedShortToByte2_Litte(len1);
        Endbuffer.put(cmdTypeBytes);
        Endbuffer.put(lenBytes);
        Endbuffer.put(encrypOutBytes);

        //取完成处理的包
        Endbuffer.flip();
        byte[] SendBytes = new byte[Endbuffer.remaining()];
        Endbuffer.get(SendBytes);
        Log.d("ConnectionManager", DigitalTrans.byte2hex(SendBytes));
        return SendBytes;

    }

    //数据打包
    public static byte[] Packet_Mac(short packTpe, byte cmdType, String mac, byte[] payLoadSrc) {
        //get mac bytes
        byte[] macBytes = new byte[12];
        byte[] macBytesTemp = DigitalTrans.StringToAsciiBytes(mac);
        int macLen = macBytesTemp.length;
        System.arraycopy(macBytesTemp, 0, macBytes, 0, macLen);


        //数据组包--时间戳  命令类型  playload
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        long time_stamp = System.currentTimeMillis();
        //long time_stamp = 123456;
        byte[] timeStampBytes = DigitalTrans.unsignedIntToByte4_Litte(time_stamp);
        byte[] cmdBytes = {cmdType};
        buffer.put(timeStampBytes);
        buffer.put(cmdBytes);
        buffer.put(macBytes);
        buffer.put(payLoadSrc);

        //取校验码
        buffer.flip();
        int len = buffer.remaining();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        byte xorByte = xor(tmpBytes);

        //存校验码
        buffer.clear();
        buffer.put(tmpBytes);
        buffer.put(xorByte);

        //取数据加密码
        buffer.flip();
        byte[] inEncryptBytes = new byte[buffer.remaining()];
        buffer.get(inEncryptBytes);
        byte[] encrypOutBytes = encrypt_CRT(Key_IV.Public_Key, Key_IV.Public_IV, inEncryptBytes);

        //取数据加密数据长度
        int len1 = encrypOutBytes.length;
        //最后组包
        ByteBuffer Endbuffer = ByteBuffer.allocate(1024);
        byte[] cmdTypeBytes = DigitalTrans.unsignedShortToByte2_Litte(packTpe);
        byte[] lenBytes = DigitalTrans.unsignedShortToByte2_Litte(len1);
        Endbuffer.put(cmdTypeBytes);
        Endbuffer.put(lenBytes);
        Endbuffer.put(encrypOutBytes);

        //取完成处理的包
        Endbuffer.flip();
        byte[] SendBytes = new byte[Endbuffer.remaining()];
        Endbuffer.get(SendBytes);
        Log.d("ConnectionManager", DigitalTrans.byte2hex(SendBytes));
        return SendBytes;

    }

    //粘包处理
    //private static final byte[]
    public static byte[] DataUnPacket(byte[] inBytes, Handler handler) {
        int dataLen = inBytes.length;
        int todataLen = dataLen;
        int end = dataLen;
        int pos = 0;
        ByteBuffer buffer = ByteBuffer.allocate(todataLen);
        while (pos < end) {
            if (dataLen > 4) {
                buffer.clear();
                //取包类型信息
                buffer.rewind();
                buffer.put(inBytes, pos, 2);
                //buffer.order(ByteOrder.BIG_ENDIAN);
                //buffer.order(ByteOrder.LITTLE_ENDIAN);//java 默认为BIG_ENDIAN，arm为小端，所以ByteOrder.LITTLE_ENDIAN反转
                buffer.flip();
                byte[] head = new byte[2];
                buffer.get(head);

                //取包数据大小
                buffer.rewind();
                buffer.put(inBytes, pos + 2, 2);
                // buffer.order(ByteOrder.LITTLE_ENDIAN);//java 默认为BIG_ENDIAN，arm为小端，所以ByteOrder.LITTLE_ENDIAN反转
                buffer.flip();
                byte[] keyDatalen = new byte[2];
                buffer.get(keyDatalen);

                short inhead = DigitalTrans.byte2ToShort_LittetoBig(head);
                short iKeylen = DigitalTrans.byte2ToShort_LittetoBig(keyDatalen);

                System.out.println("head小端序：" + DigitalTrans.byte2hex(head));
                System.out.println("head 传输方向：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0xff00)));
                System.out.println("head 命令类型：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0x00f0)));
                System.out.println("head 秘钥类型：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0x000f)));
                System.out.println("head 包的长度：" + Integer.toString(iKeylen));

                //头检查
                if (((short) (inhead & 0xff00) == Packe_Def.DIR_DEV_SVR || (short) (inhead & 0xff00) == Packe_Def.DIR_DEV_APP || (short) (inhead & 0xff00) == Packe_Def.DIR_APP_SVR || (short) (inhead & 0xff00) == Packe_Def.DIR_SVR_APP)
                        && ((short) (inhead & 0x00f0) == (short) 0x0000 || (short) (inhead & 0x00f0) == (short) 0x0080) && ((short) (inhead & 0x00f) == (short) 0x0000 || (short) (inhead & 0x00f) == (short) 0x0001 || (short) (inhead & 0x00f) == (short) 0x0002)) {

                    if (todataLen - pos >= iKeylen) {
                        //数据解密
                        byte[] packData = new byte[iKeylen];
                        System.arraycopy(inBytes, pos + 4, packData, 0, iKeylen);
                        byte[] deData = decrypt_CTR(Key_IV.Public_Key, Key_IV.Public_IV, packData);

                        if (xor(deData) == 0x00) {
                            //数据分析
                            int playlen = deData.length - 1;
                            byte[] playData = new byte[playlen];
                            System.arraycopy(deData, 0, playData, 0, playlen);//去校验码
                            payloadAnalysis(playData, handler);
                        }
                        if (iKeylen == 0) {
                            iKeylen = 1;
                        }
                        pos = pos + 4 + iKeylen;
                        dataLen = todataLen - pos;
                    } else {
                        // EndTmp = recoverData[pos:]
                        byte[] tmpData = new byte[todataLen - pos];
                        System.arraycopy(inBytes, pos, tmpData, 0, todataLen - pos);
                        return tmpData;
                    }

                } else {
                    pos++;
                    dataLen = todataLen - pos;
                }

            } else {
                byte[] tmpData = new byte[todataLen - pos];
                System.arraycopy(inBytes, pos, tmpData, 0, todataLen - pos);
                return tmpData;
            }
        }
        return new byte[]{};
    }

    //粘包处理
    //private static final byte[]
    public static byte[] DataUnPacket_Server(byte[] inBytes, Socket devSocket, Handler handler) throws IOException, InterruptedException {
        Log.d("ConnectionManager", "进行了粘包处理");
        int dataLen = inBytes.length;
        int todataLen = dataLen;
        int end = dataLen;
        int pos = 0;
        ByteBuffer buffer = ByteBuffer.allocate(todataLen);
        while (pos < end) {
            if (dataLen > 4) {
                buffer.clear();
                //取包类型信息
//			   buffer.rewind();
//			   buffer.put(inBytes, pos, 2);			  
//			   buffer.flip();
//			   byte[] head =new byte[2];			   
//			   buffer.get(head);
                byte[] head = new byte[2];
                System.arraycopy(inBytes, pos, head, 0, 2);


                //取包数据大小
//			   buffer.rewind();
//			   buffer.put(inBytes, pos+2, 2);
//			   buffer.flip();
//			   byte[] keyDatalen =new byte[2];
//			   buffer.get(keyDatalen);
                byte[] keyDatalen = new byte[2];
                System.arraycopy(inBytes, pos + 2, keyDatalen, 0, 2);

                short inhead = DigitalTrans.byte2ToShort_LittetoBig(head);
                short iKeylen = DigitalTrans.byte2ToShort_LittetoBig(keyDatalen);

                System.out.println("head小端序：" + DigitalTrans.byte2hex(head));
                System.out.println("head 传输方向：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0xff00)));
                System.out.println("head 命令类型：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0x00f0)));
                System.out.println("head 秘钥类型：" + DigitalTrans.byte2hex(DigitalTrans.unsignedShortToByte2(inhead & 0x000f)));
                System.out.println("head 包的长度：" + Integer.toString(iKeylen));

                //头检查
                if (((short) (inhead & 0xff00) == Packe_Def.DIR_DEV_SVR || (short) (inhead & 0xff00) == Packe_Def.DIR_DEV_APP || (short) (inhead & 0xff00) == Packe_Def.DIR_APP_SVR || (short) (inhead & 0xff00) == Packe_Def.DIR_SVR_APP)
                        && ((short) (inhead & 0x00f0) == (short) 0x0000 || (short) (inhead & 0x00f0) == (short) 0x0080) && ((short) (inhead & 0x00f) == (short) 0x0000 || (short) (inhead & 0x00f) == (short) 0x0001 || (short) (inhead & 0x00f) == (short) 0x0002)) {

                    if (todataLen - pos >= iKeylen) {
                        //数据解密
                        byte[] packData = new byte[iKeylen];
                        System.arraycopy(inBytes, pos + 4, packData, 0, iKeylen);
                        byte[] deData = decrypt_CTR(Key_IV.Public_Key, Key_IV.Public_IV, packData);
                        Log.d("ConnectionManager", "数据分析");
                        if (xor(deData) == 0x00) {
                            //数据分析
                            int playlen = deData.length - 1;
                            byte[] playData = new byte[playlen];
                            System.arraycopy(deData, 0, playData, 0, playlen);// 去校验码
                            try {
                                payloadAnalysis_server(playData, devSocket, handler);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (iKeylen == 0) {
                            iKeylen = 1;
                        }
                        pos = pos + 4 + iKeylen;
                        dataLen = todataLen - pos;
                    } else {
                        // EndTmp = recoverData[pos:]
                        byte[] tmpData = new byte[todataLen - pos];
                        System.arraycopy(inBytes, pos, tmpData, 0, todataLen - pos);
                        return tmpData;
                    }

                } else {
                    pos++;
                    dataLen = todataLen - pos;
                }

            } else {
                byte[] tmpData = new byte[todataLen - pos];
                System.arraycopy(inBytes, pos, tmpData, 0, todataLen - pos);
                return tmpData;
            }
        }
        return new byte[]{};
    }

    public static void payloadAnalysis(byte[] deData, Handler handler) {
        if (deData.length < 7) {
            return;
        }
        byte cmd = deData[4];
        switch (cmd) {
            case MsgType.DEV_SERVER_CMD_LOGIN:
                break;
            case MsgType.DEV_SERVER_CMD_REPORT:
                int playlen = deData.length - 5;
                byte[] playload = new byte[playlen];
                System.arraycopy(deData, 5, playload, 0, playlen);
                PayloadUnPacket(playload, handler);
                break;
            case MsgType.SERVER_APP_ACK_DEMAND:
                byte demandType = deData[5];
                if (demandType == Packe_Def.DEV_OFF_ON) {
                    if (deData[6] == 0x00) {
                        System.out.println("设备离线");
                    } else {
                        System.out.println("设备在线");
                    }
                } else if (demandType == Packe_Def.DEV_Q_DATA) {
                    int plen = deData.length - 5;
                    byte[] pload = new byte[plen];
                    System.arraycopy(deData, 5, pload, 0, plen);
                    PayloadUnPacket(pload, handler);
                }
                break;
        }
    }

    public static void payloadAnalysis_server(byte[] deData, Socket devSocket, Handler handler) throws InterruptedException {
        if (deData.length < 6) {
            return;
        }
        byte cmd = deData[4];
        Log.d("ConnectionManager", "CmdType key" + cmd);
        switch (cmd) {
            case MsgType.DEV_SERVER_CMD_LOGIN:
                DataOutputStream out;
                try {
                    out = new DataOutputStream(devSocket.getOutputStream());
                    byte[] time_stamp = new byte[4];
                    System.arraycopy(deData, 0, time_stamp, 0, 4);
                    byte[] ackData = LoginAckPacket(time_stamp);
                    out.write(ackData);
//		    	 byte[] PowerSwitchData=PowerSwitch(false);
//		    	 out.write(PowerSwitch(false));
//		    	 Thread.sleep(5);
//		    	 out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            case MsgType.DEV_SERVER_CMD_REPORT:
                int playlen = deData.length - 5;
                byte[] playload = new byte[playlen];
                System.arraycopy(deData, 5, playload, 0, playlen);
                PayloadUnPacket(playload, handler);
                break;
            case MsgType.SERVER_APP_ACK_DEMAND:
//                byte demandType = deData[5];
//                if (demandType == Packe_Def.DEV_OFF_ON) {
//                    if (deData[6] == 0x00) {
//                        System.out.println("设备离线");
//                    } else {
//                        System.out.println("设备在线");
//                    }
//                } else if (demandType == Packe_Def.DEV_Q_DATA) {
//                    int plen = deData.length - 5;
//                    byte[] pload = new byte[plen];
//                    System.arraycopy(deData, 5, pload, 0, plen);
//                    PayloadUnPacket(pload);
//                }
                Log.d("ConnectionManager", "我终于得到你");
                byte demandType = deData[5];
                if (demandType == Packe_Def.DEV_OFF_ON) {
                    byte ackByte = deData[6];
                } else if (demandType == Packe_Def.DEV_Q_DATA) {
                    int dLen = deData.length - 6;
                    byte[] pBytes = new byte[dLen];
                    System.arraycopy(deData, 6, pBytes, 0, dLen);
                    PayloadUnPacket(pBytes, handler);
                }

                break;
            case MsgType.DEV_SERVER_ACK_DEMAND:
                int playlenA = deData.length - 5;
                byte[] playloadA = new byte[playlenA];
                System.arraycopy(deData, 5, playloadA, 0, playlenA);
                PayloadUnPacket(playloadA, handler);
                break;
            case MsgType.DEV_APP_ACK_ACTIVATE:
                byte isAck = deData[5];
                if (isAck == (byte) 1) {
                    handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL);
                    Log.d("ConnectionManager", "配置成功");
                } else {
                    handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_FAILING);
                    Log.d("ConnectionManager", "配置失败");
                }

                break;
            case MsgType.SERVER_APP_ACK_CONTROL:
                byte isTiming = deData[5];
                Log.d("ConnectionManager", "校时" + isTiming);
                if (isTiming == (byte) 1) {
                    handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_SUCCESSFUL);
                } else {
                    handler.sendEmptyMessage(StatisConstans.MSG_ENABLED_FAILING);
                }
                break;
        }

    }

    //Payload解包
    public static void PayloadUnPacket(byte[] payLoadBody, Handler handler) {

        int end = payLoadBody.length;
        int pos = 0;
        PmAllData pmAllData = new PmAllData();
        while (pos < end) {
            byte key = payLoadBody[pos];
            Log.d("ConnectionManager", "进行了粘包处理" + key);
            switch (key) {
//		   case:
//			   break;
                case PayloardKey.D_POWER: //开关机
                    pos++;
                    if (payLoadBody[pos] == (byte) 0x01) {
                        pmAllData.setoNstate("开机");
                        System.out.println("开关状态：开启\r\n");
                    } else {
                        pmAllData.setoNstate("关机");
                        System.out.println("开关状态：关机\r\n");
                        //devInfo.IsOpen = false
                    }
                    break;
                case PayloardKey.D_MODE: //模式
                    pos++;
                    //devInfo.SmartModel = uint8(payLoadBody[pos])
                    //System.out.println("开机模式:")
                    if (((byte) payLoadBody[pos] & (byte) (0x01)) == (byte) (0x01)) {
                        pmAllData.setMuteMode(true);
                        System.out.println("静音");
                    }

                    if (((byte) payLoadBody[pos] & (byte) (0x01 << 1)) == (0x01 << 1)) {
                        pmAllData.setCoMode(true);
                        System.out.println("co2模式");
                    }

                    if (((byte) payLoadBody[pos] & (byte) (0x01 << 2)) == 0x01 << 2) {
                        pmAllData.setPmMode(true);
                        System.out.println("粉尘模式");
                    }
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_EXHAUST_MODE: //新排风模式
                    pos++;
                    System.out.println("新排风模式:");
                    switch ((byte) (payLoadBody[pos])) {
                        case 1:
                            pmAllData.setExhaustMode("单新风");
                            System.out.println("单新风");
                            break;
                        case 2:
                            pmAllData.setExhaustMode("单排风");
                            System.out.println("单排风");
                            break;
                        case 3:
                            pmAllData.setExhaustMode("新排风");
                            System.out.println("新排风");
                            break;
                    }
                    System.out.println("\r\n");
                    break;

                case PayloardKey.D_EXHAUST_RATIO: //排风比例
                    pos++;
                    //System.out.println(pos)
                    byte[] pBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, pBytes, 0, 2);
                    pmAllData.setExhaustRatio(DigitalTrans.byte2ToUnsignedShort(pBytes));
                    System.out.printf("排风比例:%d", DigitalTrans.byte2ToUnsignedShort(pBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_LCD_BACKLIGHT: //背光时间

                    pos++;
                    System.out.println("背光时间:");
                    switch (payLoadBody[pos]) {
                        case 0:
                            pmAllData.setLcdBacklight("10s");
                            System.out.println("10s");
                            break;
                        case 1:
                            pmAllData.setLcdBacklight("30s");
                            System.out.println("30s");
                            break;
                        case 2:
                            pmAllData.setLcdBacklight("60s");
                            System.out.println("60s");
                            break;
                        case 3:
                            pmAllData.setLcdBacklight("常亮");
                            System.out.println("常亮");
                            break;
                    }
                    System.out.println("\r\n");
                    break;

                case PayloardKey.D_AUX_HEATING: //辅热温度值

                    pos++;
                    pmAllData.setAuxHeating(payLoadBody[pos]);
                    System.out.printf("辅热温度:%d", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;

                case PayloardKey.D_CONST_TEMP_MODE: //恒温模式
                    pos++;
                    System.out.println("恒温模式:");
                    if (payLoadBody[pos] == 0) {
                        pmAllData.setConstTempMode("关");
                        System.out.println("关");
                    } else {
                        pmAllData.setConstTempMode("开");
                        System.out.println("开");
                    }
                    System.out.println("\r\n");
                    break;

                case PayloardKey.D_CONST_HUMI_MODE: //恒湿模式

                    pos++;
                    System.out.println("恒湿模式:");
                    if (payLoadBody[pos] == 0) {
                        pmAllData.setConstHumiMode("关");
                        System.out.println("关");
                    } else {
                        pmAllData.setConstHumiMode("开");
                        System.out.println("开");
                    }
                    System.out.println("\r\n");
                    break;

                case PayloardKey.D_CONST_TEMP_PARA: //恒温参数
                    pos++;
                    pmAllData.setConstTempPara(payLoadBody[pos]);
                    System.out.printf("恒温参数:%d", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_CONST_HUMI_PARA: //恒湿参数
                    pos++;
                    pmAllData.setConstHumiPara(payLoadBody[pos]);
                    System.out.printf("恒湿参数:%d", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_FAN_FREQ: //风机频率
                    pos++;
                    pmAllData.setFanFreq((payLoadBody[pos]));
                    System.out.printf("风机频率:%d", (payLoadBody[pos]));
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_BLOWER_SPEED: //送风机转速
                    pos++;
                    byte[] sBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, sBytes, 0, 2);
                    pmAllData.setBlowerSpeed(DigitalTrans.byte2ToUnsignedShort(sBytes));
                    System.out.printf("送风机转速:%d", DigitalTrans.byte2ToUnsignedShort(sBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_EXHAUST_SPEED: //排风机转速

                    pos++;
                    byte[] prBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, prBytes, 0, 2);
                    pmAllData.setExhaustSpeed(DigitalTrans.byte2ToUnsignedShort(prBytes));
                    System.out.printf("排风机转速:%d", DigitalTrans.byte2ToUnsignedShort(prBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_BLOWING_RATE: //风量
                    pos++;
                    byte[] zBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, zBytes, 0, 2);
                    pmAllData.setBlowingRate(DigitalTrans.byte2ToUnsignedShort(zBytes));
                    System.out.printf("风量:%d", DigitalTrans.byte2ToUnsignedShort(zBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_THAW_START_TEMP: //融冰启动温度
                    pos++;
                    pmAllData.setThawStartTemp(payLoadBody[pos]);
                    System.out.printf("融冰启动温度:%d", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_THAW_STOP_TEMP://融冰停止温度

                    pos++;
                    pmAllData.setThawStopTemp(payLoadBody[pos]);
                    System.out.printf("融冰停止温度:%d", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_THAW_TIME: //融冰时间

                    pos++;
                    pmAllData.setThawTime(payLoadBody[pos]);
                    System.out.printf("融冰时间:%d分钟", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_THAW_INV: //融冰间隔时间

                    pos++;
                    pmAllData.setThawInv(payLoadBody[pos]);
                    System.out.printf("融冰间隔时间:%d分钟", payLoadBody[pos]);
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_DUSTWIPER_CLEAR_PERIOD: //除尘器清洗周期
                    pos++;
                    byte[] clBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, clBytes, 0, 2);
                    pmAllData.setDustwiperClearPeriod(DigitalTrans.byte2ToUnsignedShort(clBytes));
                    System.out.printf("除尘器清洗周期:%d小时", DigitalTrans.byte2ToUnsignedShort(clBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_DUSTWIPER_CLEAR_REMAIN://除尘器清洗周期剩余时间
                    pos++;
                    byte[] dlBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, dlBytes, 0, 2);
                    pmAllData.setDustwiperClearRemain(DigitalTrans.byte2ToUnsignedShort(dlBytes));
                    System.out.printf("除尘器清洗周期剩余时间:%d小时", DigitalTrans.byte2ToUnsignedShort(dlBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_TIME_MODE: //定时模式
                    pos++;
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01)) != 0) {
                        pmAllData.setTimeMode(1);
                        System.out.println("定时模式1开启:每天");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x02)) != 0) {
                        pmAllData.setTimeMode(2);
                        System.out.println("定时模式2开启:仅一次");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x04)) != 0) {
                        pmAllData.setTimeMode(4);
                        System.out.println("定时模式3开启:工作日");
                        System.out.println("\r\n");
                    }

                    if (((byte) (payLoadBody[pos]) & (byte) (0x08)) != 0) {
                        pmAllData.setTimerOneState(true);
                        System.out.println("定时器1状态:开");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x10)) != 0) {
                        pmAllData.setTimerTwoState(true);
                        System.out.println("定时器2状态:开");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x20)) != 0) {
                        pmAllData.setTimerThreeState(true);
                        System.out.println("定时器3状态:开");
                        System.out.println("\r\n");
                    }
                    break;
                case PayloardKey.D_TIMER1: //定时器1
                    pos += 1;
                    System.out.println("定时器1:");
                    pmAllData.setTimerOneStartHour(payLoadBody[pos]);
                    System.out.printf("开始时间小时值:%d", (byte) payLoadBody[pos]);
                    pmAllData.setTimerOneStartMin(payLoadBody[pos + 1]);
                    System.out.printf("开始时间分钟值:%d", (byte) payLoadBody[pos + 1]);
                    pmAllData.setTimerOneEndHour(payLoadBody[pos + 2]);
                    System.out.printf("结束时间小时值:%d", (byte) payLoadBody[pos + 2]);
                    pmAllData.setTimerOneEndMin(payLoadBody[pos + 3]);
                    System.out.printf("结束时间分钟值:%d", (byte) payLoadBody[pos + 3]);
                    System.out.println("\r\n");
                    pos += 3;
                    break;
                case PayloardKey.D_TIMER2: //定时器2
                    pos++;
                    System.out.println("定时器2:");
                    pmAllData.setTimerTwoStartHour(payLoadBody[pos]);
                    System.out.printf("开始时间小时值:%d", (byte) payLoadBody[pos]);
                    pmAllData.setTimerTwoStartMin(payLoadBody[pos + 1]);
                    System.out.printf("开始时间分钟值:%d", (byte) payLoadBody[pos + 1]);
                    pmAllData.setTimerTwoEndHour((byte) payLoadBody[pos + 2]);
                    System.out.printf("结束时间小时值:%d", (byte) payLoadBody[pos + 2]);
                    pmAllData.setTimerTwoEndMin(payLoadBody[pos + 3]);
                    System.out.printf("结束时间分钟值:%d", (byte) payLoadBody[pos + 3]);
                    System.out.println("\r\n");
                    pos += 3;
                    break;
                case PayloardKey.D_TIMER3: //定时器3
                    pos++;
                    System.out.println("定时器3:");
                    pmAllData.setTimerThreeStartHour(payLoadBody[pos]);
                    System.out.printf("开始时间小时值:%d", payLoadBody[pos]);
                    pmAllData.setTimerThreeStartMin(payLoadBody[pos + 1]);
                    System.out.printf("开始时间分钟值:%d", payLoadBody[pos + 1]);
                    pmAllData.setTimerThreeEndHour(payLoadBody[pos + 2]);
                    System.out.printf("结束时间小时值:%d", payLoadBody[pos + 2]);
                    pmAllData.setTimerThreeEndMin(payLoadBody[pos + 3]);
                    System.out.printf("结束时间分钟值:%d", payLoadBody[pos + 3]);
                    System.out.println("\r\n");
                    pos += 3;
                    break;
                case PayloardKey.D_CO2_ADJ: //CO2调节值
                    pos++;
                    byte[] cBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, cBytes, 0, 2);
                    pmAllData.setCo2Adj(DigitalTrans.byte2ToUnsignedShort(cBytes));
                    System.out.printf("CO2调节值:%d", DigitalTrans.byte2ToUnsignedShort(cBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_FUNC_STATUS: //功能状态
                    pos++;
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01)) != 0) {
                        pmAllData.setFaultStatus("集尘器开启");
                        System.out.println("集尘器开启");
                        System.out.println("\r\n");
                    } else {
                        pmAllData.setFaultStatus("集尘器关闭");
                        System.out.println("集尘器关闭");
                        System.out.println("\r\n");
                    }

                    if (((byte) (payLoadBody[pos]) & (byte) (0x02)) != 0) {
                        pmAllData.setFaultStatus("负离子开启");
                        System.out.println("负离子开启");
                        System.out.println("\r\n");
                    } else {
                        pmAllData.setFaultStatus("集尘器关闭");
                        System.out.println("集尘器关闭");
                        System.out.println("\r\n");
                    }
                    System.out.println("\r\n");
                    break;
                case PayloardKey.D_PM_ADJ: //PM调节
                    pos++;
                    byte[] pmBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, pmBytes, 0, 2);
                    pmAllData.setPmAdj(DigitalTrans.byte2ToUnsignedShort(pmBytes));
                    System.out.printf("PM调节值:%d", (int) (DigitalTrans.byte2ToUnsignedShort(pmBytes) & 0xffff));
                    System.out.println("\r\n");
                    pos++;
                    break;

                case PayloardKey.D_SENSOR_INDOOR_TEMP: //室内温度
                    pos++;
                    byte[] co2Bytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, co2Bytes, 0, 2);
                    pmAllData.setSensorIndoorTemp(DigitalTrans.byte2ToUnsignedShort(co2Bytes));
                    System.out.printf("室内温度:%d", DigitalTrans.byte2ToUnsignedShort(co2Bytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_SENSOR_OUTDOOR_TEMP: //室外温度
                    pos++;
                    byte[] ico2Bytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, ico2Bytes, 0, 2);
                    pmAllData.setSensorOutdoorTemp(DigitalTrans.byte2ToUnsignedShort(ico2Bytes));
                    System.out.printf("室外温度:%d", DigitalTrans.byte2ToUnsignedShort(ico2Bytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_SENSOR_FAN_TEMP: //新风温度
                    pos++;
                    byte[] newBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, newBytes, 0, 2);
                    pmAllData.setSensorFanTemp(DigitalTrans.byte2ToUnsignedShort(newBytes));
                    System.out.printf("新风温度:%d", DigitalTrans.byte2ToUnsignedShort(newBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_SENSOR_EXHAUST_TEMP: //排风温度
                    pos++;
                    byte[] pwBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, pwBytes, 0, 2);
                    pmAllData.setSensorExhaustTemp(DigitalTrans.byte2ToUnsignedShort(pwBytes));
                    System.out.printf("排风温度:%d", DigitalTrans.byte2ToUnsignedShort(pwBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_CO2_THICKNESS: //CO2浓度
                    pos++;
                    byte[] co2lBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, co2lBytes, 0, 2);
                    pmAllData.setCo2Thickness(DigitalTrans.byte2ToUnsignedShort(co2lBytes));
                    System.out.printf("CO2浓度:%d", DigitalTrans.byte2ToUnsignedShort(co2lBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_INDOOR_PM_THICKNESS: //室内颗粒物浓度
                    pos++;
                    byte[] ipmBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, ipmBytes, 0, 2);
                    pmAllData.setIndoorPmThickness(DigitalTrans.byte2ToUnsignedShort(ipmBytes));
                    System.out.printf("室内颗粒物浓度:%d", DigitalTrans.byte2ToUnsignedShort(ipmBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_OUTDOOR_PM_THICKNESS: //室外颗粒物浓度
                    pos++;
                    byte[] outRoomPMBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, outRoomPMBytes, 0, 2);
                    pmAllData.setOutdoorPmThickness(DigitalTrans.byte2ToUnsignedShort(outRoomPMBytes));
                    System.out.printf("室外颗粒物浓度:%d", DigitalTrans.byte2ToUnsignedShort(outRoomPMBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_INDOOR_HUMI: //室内湿度
                    pos++;
                    byte[] inRoomHumPMBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, inRoomHumPMBytes, 0, 2);
                    System.out.printf("室内湿度:%d", DigitalTrans.byte2ToUnsignedShort(inRoomHumPMBytes));
                    System.out.println("\r\n");
                    pos++;
                    break;
                case PayloardKey.D_SYSTEM_CLOCK: //系统时间校准
                    pos++;
                    byte[] stBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, stBytes, 0, 2);
                    pmAllData.setSystemClockHour(DigitalTrans.byte2ToUnsignedShort(stBytes));
                    System.out.printf("系统时间年:%d", DigitalTrans.byte2ToUnsignedShort(stBytes));
                    pmAllData.setSystemClockMouth(payLoadBody[pos + 2]);
                    System.out.printf("系统时间月:%d", payLoadBody[pos + 2]);
                    pmAllData.setSystemClockDay(payLoadBody[pos + 3]);
                    System.out.printf("系统时间日:%d", payLoadBody[pos + 3]);
                    pmAllData.setSystemClockHour(payLoadBody[pos + 4]);
                    System.out.printf("系统时间时:%d", payLoadBody[pos + 4]);
                    pmAllData.setSystemClockMin(payLoadBody[pos + 5]);
                    System.out.printf("系统时间分:%d", payLoadBody[pos + 5]);
                    System.out.println("\r\n");
                    pos += 5;
                    break;
                case PayloardKey.D_FAULT_STATUS: //故障状态
                    pos++;
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01)) == (0x01)) {
                        pmAllData.setFaultStatus("CO2传感器故障");
                        System.out.println("CO2传感器故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 1)) == (0x01 << 1)) {
                        pmAllData.setFaultStatus("室内温度探头故障");
                        System.out.println("室内温度探头故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 2)) == (0x01 << 2)) {
                        pmAllData.setFaultStatus("室外温度探头故障");
                        System.out.println("室外温度探头故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 3)) == (0x01 << 3)) {
                        pmAllData.setFaultStatus("新风温度探头故障");
                        System.out.println("新风温度探头故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 4)) == (0x01 << 4)) {
                        pmAllData.setFaultStatus("排风温度探头故障");
                        System.out.println("排风温度探头故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 5)) == (0x01 << 5)) {
                        pmAllData.setFaultStatus("室内粉尘传感器故障");
                        System.out.println("室内粉尘传感器故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 6)) == (0x01 << 6)) {
                        pmAllData.setFaultStatus("室外粉尘传感器故障");
                        System.out.println("室外粉尘传感器故障");
                        System.out.println("\r\n");
                    }
                    if (((byte) (payLoadBody[pos]) & (byte) (0x01 << 7)) == (0x01 << 7)) {
                        pmAllData.setFaultStatus("湿度传感器故障");
                        System.out.println("湿度传感器故障");
                        System.out.println("\r\n");
                    }

                    byte[] errBytes = new byte[2];
                    System.arraycopy(payLoadBody, pos, errBytes, 0, 2);
                    if (DigitalTrans.byte2ToUnsignedShort(errBytes) == 0xFFFF) {
                        pmAllData.setFaultStatus("未知的故障");
                        System.out.println("未知的故障");
                        System.out.println("\r\n");

                    }
                    pos++;
                    break;

            }
            pos++;
        }
        handler.sendMessage(handler.obtainMessage(StatisConstans.MSG_QUEST_SERVER, pmAllData));
    }

    //字节异或处理
    public static byte xor(byte[] inBytes) {
        byte retB;
        int max = inBytes.length;
        if (max > 0) {
            retB = inBytes[0];
            for (int i = 1; i < max; i++) {
                retB ^= inBytes[i];
            }
        } else {
            retB = (byte) 0xff;
        }
        return retB;
    }

    //AES CTR没有加密
    public static byte[] encrypt_CRT(byte[] key, byte[] initVector, byte[] value) {
        try {
            System.out.println("key:\t" + Arrays.toString(key));
            System.out.println("iv:\t" + Arrays.toString(initVector));//UTF-8 ,GBK,ISO8859-1
//            IvParameterSpec iv = new IvParameterSpec(initVector);
//            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
//            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//            byte[] encrypted = cipher.doFinal(value);
            byte[] encrypted = value;
            System.out.println(Arrays.toString(encrypted));
            //System.out.println("encrypted string: "
            //        + Base64.encodeBase64String(encrypted));

            //return byte2HexStr(encrypted);
            //return Base64.encodeBase64String(encrypted);
            System.out.println(DigitalTrans.byte2hex(encrypted));
            return encrypted;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //AES CTR 没有解密
    public static byte[] decrypt_CTR(byte[] key, byte[] initVector, byte[] encrypted) {
        try {
//            IvParameterSpec iv = new IvParameterSpec(initVector);
//            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CTR/NOPADDING");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
//            byte[] original = cipher.doFinal(encrypted);
            byte[] original = encrypted;
            System.out.println(DigitalTrans.byte2hex(original));
            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     */

    public static byte[] hexStr2Bytes(String hexStr) {
        System.out.println("in len :" + hexStr.length());
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        System.out.println("out len :" + bytes.length);
        System.out.println("ddd" + Arrays.toString(bytes));
        return bytes;
    }

    /**
     * bytes转换成十六进制字符串
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            // if (n<b.length-1) hs=hs+":";
        }
        return hs.toUpperCase();
    }

    public static String encrypt_CBC(String key, String initVector, String value) {
        try {
            System.out.println("key:\t" + Arrays.toString(key.getBytes("UTF-8")));
            System.out.println("iv:\t" + Arrays.toString(initVector.getBytes("UTF-8")));
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            //Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println(Arrays.toString(encrypted));
            //System.out.println("encrypted string: "
            //        + Base64.encodeBase64String(encrypted));

            return byte2HexStr(encrypted);
            //return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt_CBC(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            byte[] original = cipher.doFinal(hexStr2Bytes(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}
