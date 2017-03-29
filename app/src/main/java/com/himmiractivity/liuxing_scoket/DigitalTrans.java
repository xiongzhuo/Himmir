package com.himmiractivity.liuxing_scoket;

public class DigitalTrans {
    /**
     * 数字字符串转ASCII码字符串
     * <p>
     * 字符串
     *
     * @return ASCII字符串
     */
    public static String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
    }

    /**
     * 数字字符串转ASCII码bytes
     *
     * @return ASCII字符串
     */
    public static byte[] StringToAsciiBytes(String content) {
        byte[] result = new byte[content.length()];
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            result[i] = (byte) (c);
        }
        return result;
    }

    //英文字串转Asciibyte数组
    public static byte[] stringToAsciiBytes(String content) {
        int max = content.length();
        byte[] result = new byte[max];
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            result[i] = (byte) (c & 0xff);
        }
        return result;
    }

    /**
     * 十六进制转字符串
     *
     * @param hexString  十六进制字符串
     * @param encodeType 编码类型4：Unicode，2：普通编码
     * @return 字符串
     */
    public static String hexStringToString(String hexString, int encodeType) {
        String result = "";
        int max = hexString.length() / encodeType;
        for (int i = 0; i < max; i++) {
            char c = (char) DigitalTrans.hexStringToAlgorism(hexString
                    .substring(i * encodeType, (i + 1) * encodeType));
            result += c;
        }
        return result;
    }

    /**
     * 十六进制字符串装十进制
     *
     * @param hex 十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }

    /**
     * 十六转二进制
     *
     * @param hex 十六进制字符串
     * @return 二进制字符串
     */
    public static String hexStringToBinary(String hex) {
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }

    /**
     * ASCII码字符串转数字字符串
     *
     * @return 字符串
     */
    public static String AsciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }

    /**
     * 将十进制转换为指定长度的十六进制字符串
     *
     * @param algorism  int 十进制数字
     * @param maxLength int 转换后的十六进制字符串长度
     * @return String 转换后的十六进制字符串
     */
    public static String algorismToHEXString(int algorism, int maxLength) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;
        }
        return patchHexString(result.toUpperCase(), maxLength);
    }

    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     *
     * @param bytearray byte[]
     * @return String
     */
    public static String bytetoString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }

    /**
     * 二进制字符串转十进制
     *
     * @param binary 二进制字符串
     * @return 十进制数值
     */
    public static int binaryToAlgorism(String binary) {
        int max = binary.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = binary.charAt(i - 1);
            int algorism = c - '0';
            result += Math.pow(2, max - i) * algorism;
        }
        return result;
    }

    /**
     * 十进制转换为十六进制字符串
     *
     * @param algorism int 十进制的数字
     * @return String 对应的十六进制字符串
     */
    public static String algorismToHEXString(int algorism) {
        String result = "";
        result = Integer.toHexString(algorism);

        if (result.length() % 2 == 1) {
            result = "0" + result;

        }
        result = result.toUpperCase();

        return result;
    }

    /**
     * HEX字符串前补0，主要用于长度位数不足。
     *
     * @param str       String 需要补充长度的十六进制字符串
     * @param maxLength int 补充后十六进制字符串的长度
     * @return 补充结果
     */
    public static String patchHexString(String str, int maxLength) {
        String temp = "";
        for (int i = 0; i < maxLength - str.length(); i++) {
            temp = "0" + temp;
        }
        str = (temp + str).substring(0, maxLength);
        return str;
    }

    /**
     * 将一个字符串转换为int
     *
     * @param s          String 要转换的字符串
     * @param defaultInt int 如果出现异常,默认返回的数字
     * @param radix      int 要转换的字符串是什么进制的,如16 8 10.
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt, int radix) {
        int i = 0;
        try {
            i = Integer.parseInt(s, radix);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 将一个十进制形式的数字字符串转换为int
     *
     * @param s          String 要转换的字符串
     * @param defaultInt int 如果出现异常,默认返回的数字
     * @return int 转换后的数字
     */
    public static int parseToInt(String s, int defaultInt) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            i = defaultInt;
        }
        return i;
    }

    /**
     * 十六进制字符串转为Byte数组,每两个十六进制字符转为一个Byte
     *
     * @param hex 十六进制字符串
     * @return byte 转换结果
     */
    public static byte[] hexStringToByte(String hex) {
        int max = hex.length() / 2;
        byte[] bytes = new byte[max];
        String binarys = DigitalTrans.hexStringToBinary(hex);
        for (int i = 0; i < max; i++) {
            bytes[i] = (byte) DigitalTrans.binaryToAlgorism(binarys.substring(
                    i * 8 + 1, (i + 1) * 8));
            if (binarys.charAt(8 * i) == '1') {
                bytes[i] = (byte) (0 - bytes[i]);
            }
        }
        return bytes;
    }

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static final byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;

            b[j] = new Integer(byteint).byteValue();
        }
        return b;
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param b byte[] 需要转换的字节数组
     * @return String 十六进制字符串
     */
    public static final String byte2hex(byte b[]) {
        if (b == null) {
            throw new IllegalArgumentException(
                    "Argument b ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    //#############

    /**
     * int整数转换为4字节的byte数组
     *
     * @param i 整数
     * @return byte数组
     */
    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    /**
     * int整数转换为4字节的byte数组  小端编码
     *
     * @param i 整数
     * @return byte数组
     */
    public static byte[] intToByte4_Litte(int i) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (i & 0xFF);
        targets[1] = (byte) (i >> 8 & 0xFF);
        targets[2] = (byte) (i >> 16 & 0xFF);
        targets[3] = (byte) (i >> 24 & 0xFF);
        return targets;
    }

    /**
     * long整数转换为8字节的byte数组
     *
     * @param lo long整数
     * @return byte数组
     */
    public static byte[] longToByte8(long lo) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((lo >>> offset) & 0xFF);
        }
        return targets;
    }

    /**
     * long整数转换为8字节的byte数组  小端编码
     *
     * @param lo long整数
     * @return byte数组
     */
    public static byte[] longToByte8_Litte(long lo) {
        byte[] targets = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = i * 8;
            targets[i] = (byte) ((lo >>> offset) & 0xFF);
        }
        return targets;
    }

    /**
     * short整数转换为2字节的byte数组
     *
     * @param s short整数
     * @return byte数组
     */
    public static byte[] unsignedShortToByte2(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s >> 8 & 0xFF);
        targets[1] = (byte) (s & 0xFF);
        return targets;
    }

    /**
     * short整数转换为2字节的byte数组 小端编码
     *
     * @param s short整数
     * @return byte数组
     */
    public static byte[] unsignedShortToByte2_Litte(int s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (s & 0xFF);
        targets[1] = (byte) (s >> 8 & 0xFF);
        return targets;
    }


    /**
     * int整数转换为4字节的byte数组
     *
     * @param s short整数
     * @return byte数组
     */
    public static byte[] unsignedIntToByte4(long s) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (s >> 24 & 0xFF);
        targets[1] = (byte) (s >> 16 & 0xFF);
        targets[2] = (byte) (s >> 8 & 0xFF);
        targets[3] = (byte) (s & 0xFF);
        return targets;
    }

    /**
     * int整数转换为4字节的byte数组  小端编码
     *
     * @param s short整数
     * @return byte数组
     */
    public static byte[] unsignedIntToByte4_Litte(long s) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (s & 0xFF);
        targets[1] = (byte) (s >> 8 & 0xFF);
        targets[2] = (byte) (s >> 16 & 0xFF);
        targets[3] = (byte) (s >> 24 & 0xFF);
        return targets;
    }

    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes byte数组
     * @return short整数
     */
    public static int byte2ToUnsignedShort(byte[] bytes) {
        int high = bytes[0];
        int low = bytes[1];
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }


    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes byte数组
     * @return short整数
     */
    public static int byte2ToUnsignedShort_LittetoBig(byte[] bytes) {
        int low = bytes[0];
        int high = bytes[1];
        return (high << 8 & 0xFF00) | (low & 0xFF);
    }


    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes byte数组
     * @return short整数
     */
    public static short byte2ToShort_LittetoBig(byte[] bytes) {
        short low = bytes[0];
        short high = bytes[1];
        return (short) ((high << 8 & 0xFF00) | (low & 0xFF));
    }

    /**
     * byte数组转换为无符号short整数
     *
     * @param bytes byte数组
     * @return short整数
     */
    public static long byte4ToUnsignedInt(byte[] bytes) {
        long l0 = bytes[0];
        long l1 = bytes[1];
        long l2 = bytes[2];
        long l3 = bytes[3];
        return (l0 << 24 & 0xFF000000) | (l1 << 16 & 0xFF000000) | (l2 << 8 & 0xFF000000) | (l3 & 0xFF);
    }

    /**
     * byte数组转换为int整数
     *
     * @param bytes byte数组
     * @param off   开始位置
     * @return int整数
     */
    public static int byte4ToInt(byte[] bytes, int off) {
        int b0 = bytes[off] & 0xFF;
        int b1 = bytes[off + 1] & 0xFF;
        int b2 = bytes[off + 2] & 0xFF;
        int b3 = bytes[off + 3] & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }
}  
    


