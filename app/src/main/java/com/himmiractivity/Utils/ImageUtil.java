package com.himmiractivity.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.himmiractivity.Constant.Configuration;
import com.himmiractivity.interfaces.StatisConstans;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ImageUtil {

    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    public static String uploadFile(File file, String RequestURL) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam = conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    return SUCCESS;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FAILURE;
    }

    public static String uploadFile(Map<String, File> files,
                                    Map<String, String> params) {

        String actionUrl = Configuration.URL_GETIMAGEUPLOADS;
        return null;

    }

    public static Bitmap comp(File file) throws FileNotFoundException {
        int option = 90;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        if (file.length() / 1024 > StatisConstans.ImgLimit) {// image.getByteCount()/1024>50
            // ByteArrayInputStream isBm = new
            // ByteArrayInputStream(baos.toByteArray());
            InputStream is = new FileInputStream(file);
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            /*** 尺寸缩放 */
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, newOpts);
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            float hh = 480, ww = 320;// 目标尺寸

            int be = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (w / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (h / hh);
            }
            if (be <= 0) {
                be = 1;
            }
            newOpts.inSampleSize = be;// 设置缩放比例
            newOpts.inJustDecodeBounds = false;
            // isBm = new ByteArrayInputStream(baos.toByteArray());
            // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);
            /*** 尺寸缩放结束 */
            // baos.reset();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            while (baos.toByteArray().length / 1024 > StatisConstans.ImgLimit
                    && option >= 60) {// while && option >= 50
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 10;// 每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(
                    baos.toByteArray());
            baos.reset();
            Bitmap bitmap1 = BitmapFactory.decodeStream(isBm, null, null);
            try {
                isBm.close();
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap1;
        } else {
            return BitmapFactory.decodeFile(file.getPath(), null);
        }
    }

    public static Bitmap comp(Bitmap image) {
        int option = 50;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 50) {// image.getByteCount()/1024>50
            ByteArrayInputStream isBm = new ByteArrayInputStream(
                    baos.toByteArray());
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            /*** 尺寸缩放 */
            newOpts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(isBm, null, newOpts);
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            float hh = 320f, ww = 240f;// 目标尺寸

            int be = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (w / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (h / hh);
            }
            if (be <= 0) {
                be = 1;
            }
            newOpts.inSampleSize = be;// 设置缩放比例
            newOpts.inJustDecodeBounds = false;
            isBm = new ByteArrayInputStream(baos.toByteArray());
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            /*** 尺寸缩放结束 */
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            if (baos.toByteArray().length / 1024 > 50 && option > 10) {// while
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 10;// 每次都减少10
            }
            isBm = new ByteArrayInputStream(baos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, null);
            return bitmap;
        } else {
            return image;
        }
    }

    public static String getUrl(String url, Map<String, String> params) {
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }

    /**
     * 上传文件(或图片)至Server
     *
     * @param actionUrl 图片上传地址
     * @param files     待上传文件
     * @param params    文本类型参数
     */
    public static String uploadFile(String actionUrl, Map<String, File> files,
                                    Map<String, String> params) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = UUID.randomUUID().toString();//"*****";
        InputStream is = null;
        DataOutputStream ds = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(actionUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
//			con.setChunkedStreamingMode(0);
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

			/* 设置传送的method=POST */
            con.setRequestMethod("POST");

			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            System.setProperty("sun.net.client.defaultConnectTimeout", "15000");
            System.setProperty("sun.net.client.defaultReadTimeout", "15000");
            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(twoHyphens);
                sb.append(boundary);
                sb.append(end);

                String key = entry.getKey();
                if (key.indexOf("comments") != -1) {
                    key = key.substring(0, key.indexOf("_"));
                }

                sb.append("Content-Disposition: form-data; name=\"" + key
                        + "\"" + end);
                sb.append(end);
                sb.append(entry.getValue());
                sb.append(end);
            }

			/* 设置DataOutputStream */
            ds = new DataOutputStream(con.getOutputStream());
            ds.write(sb.toString().getBytes());

            // 发送文件数据　
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(twoHyphens);
                    sb2.append(boundary);
                    sb2.append(end);
                    sb2.append("Content-Disposition: form-data; "
                            + "name=\"image\";filename=\"" + file.getValue().getName()
                            + "\"" + end);
//                    sb2.append("Content-Type: application/octet-stream;" + end);
                    sb2.append("Content-Type: image/jpeg;charset=" + CHARSET + end);
                    sb2.append(end);
                    ds.write(sb2.toString().getBytes());

					/* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(
                            file.getValue());

					/* 设置每次写入1024bytes */
                    int bufferSize = 1024 * 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;

					/* 从文件读取数据至缓冲区 */
                    while ((length = fStream.read(buffer)) != -1) {
                        /* 将资料写入DataOutputStream中 */
                        ds.write(buffer, 0, length);
                    }
                    fStream.close();
                    ds.write(end.getBytes());
                }
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();

			/* 取得Response内容 */
            is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

			/* 关闭DataOutputStream */
            ds.close();
            con.disconnect();
            Log.e("b", b.toString().trim());
            return b.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {

            }
            try {
                if (ds != null)
                    ds.close();
            } catch (Exception e) {

            }
            try {
                if (con != null)
                    con.disconnect();
            } catch (Exception e) {

            }
        }

        return "fail";
    }


    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url    Service net address
     * @param params text content
     * @param files  pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";


        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }


        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"image\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: image/jpeg; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());


                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }


                is.close();
                outStream.write(LINEND.getBytes());
            }
        }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        //----------------------------------------
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb1 = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb1.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
        //----------------------------------------

        //这里读成了乱码。。。。
//        StringBuilder sb2 = new StringBuilder();
//        if (res == 200) {
//            int ch;
//            while ((ch = in.read()) != -1) {
//                sb2.append((char) ch);
//            }
//        }
//        outStream.close();
//        conn.disconnect();
//        return sb2.toString();
    }


}
