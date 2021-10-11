package com.biaozhunyuan.tianyi.common.utils;



import android.util.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by wangAnMin on 2018/4/20.
 */

public class ByteUtils {
    /**
     * int转byte[]
     */
    public static byte[] intToBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xff);
        bytes[1] = (byte) ((i >> 8) & 0xff);
        bytes[2] = (byte) ((i >> 16) & 0xff);
        bytes[3] = (byte) ((i >> 24) & 0xff);
        return bytes;
    }


    /**
     * byte[]转int
     */
    public static int bytesToInt(byte[] bytes) {
        int i;
        i = (int) ((bytes[0] & 0xff) | ((bytes[1] & 0xff) << 8)
                | ((bytes[2] & 0xff) << 16) | ((bytes[3] & 0xff) << 24));
        return i;
    }

    public static String toString(byte[] bytes) throws Exception {
        if (bytes == null)
            throw new Exception("The byte array must have at least 1 byte.");
        return new String(bytes, "UTF-8"); //以GB2312类型接收
    }

    /**
     * 本地图片转换成base64字符串
     * @param imgFile	图片本地路径
     * @return
     *
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:40:46
     */
    public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        try {
            return URLEncoder.encode(Base64.encodeToString(data,Base64.DEFAULT),"UTF-8"); // 返回Base64编码过的字节数组字符串
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
