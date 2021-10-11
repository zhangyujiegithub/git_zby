package com.biaozhunyuan.tianyi.common.helper;

import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Created by wangAnMin on 2019/3/28.
 */

public class UploadHelperListener {
    private static ProgressListener progressListener;

    private final String TAG = UploadHelperListener.this.getClass().getName();
    private final int TIME_OUT = 20 * 1000; // 超时时间
    private final String CHARSET = "utf-8"; // 设置编码


    /**
     * 上传文件
     *
     * @param file       文件
     * @param methodName 方法名
     * @return 附件编号
     */
    public Attach uploadFile(File file, String methodName, String category) {
        String RequestURL = Global.BASE_JAVA_URL + methodName;
        String result = "";
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String SPLITE = PREFIX + BOUNDARY + LINE_END;
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        int transferred = 0;
        Attach attach = new Attach();
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
            conn.setRequestProperty("Cookie", CookieUtils.cookieHeader(RequestURL)); // 权限
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            if (file != null) {
                Logger.i(TAG + "文件不为空");
                /** 当文件不为空，把文件包装并且上传 */
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(SPLITE);
                Logger.i(TAG + "length:" + sb.length());
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"category\"" + LINE_END + LINE_END + category + LINE_END);
                sb.append(SPLITE);
                sb.append("Content-Disposition: form-data; name=\"Filedata\"; filename=\""
                        + URLEncoder.encode(file.getName(), "UTF-8") + "\"" + LINE_END);
                sb.append(SPLITE);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    if (progressListener != null) {
                        transferred += len;
                        int progress = (int) (transferred / (float) file.length() * 100);
                        progressListener.onProgressListener(progress);
                        if (progress == 100) {
                            progressListener = null;
                        }
                    }
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                dos.close();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Logger.e(TAG + "response code:" + res);
                // 获得服务端返回的响应
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                input.close();
                // 服务端返回的附件编号
                result = sb1.toString();
                Logger.d(TAG + "http响应 : " + result);
                result = result.trim();
                Logger.d(TAG + "http响应 : " + result);
                Attach attachs = GsonTool.jsonToEntity(JsonUtils.pareseData(result), Attach.class);
                if (attachs != null) {
                    attach = attachs;
                    Logger.e("uploadfileAttach" + "attach--id=" + attach.getUuid()
                            + " address:" + attach.Address);
                }
            }
        } catch (MalformedURLException e) {
            Logger.e("uploadfile" + ": " + e);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = result.replaceAll("\"", "");
        Logger.d(TAG + "服务端返回的附件编号: " + attach.getUuid());
        // return result;
        return attach;
    }

    public interface ProgressListener {
        void onProgressListener(int progress);
    }

    public void setOnProgressListener(ProgressListener listener) {
        progressListener = listener;
    }
}
