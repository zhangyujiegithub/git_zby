package com.biaozhunyuan.tianyi.common.helper;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadHelper {

    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 20 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码

    private static UploadHelper mInstance;
    private static Handler mHandler;
    private static ProgressListener progressListener;

    private UploadHelper() {
        mHandler = new Handler(Looper.getMainLooper());
        progressListener = null;
    }

    public static UploadHelper getInstance() {
        if (mInstance == null) {
            mInstance = new UploadHelper();
        }
        return mInstance;
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param methodName 方法名
     * @return 附件编号
     */
    private static Attach uploadFile(File file, String methodName, String category) {
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
                byte[] bytes = new byte[1024 * 1024 * 5];
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

    /**
     * android上传文件到服务器 HTTP方式请求只能上传小文件
     *
     * @param file 需要上传的文件
     * @return 返回响应的内容:list集合 图片，路径
     */
    public static String uploadFileByHttp(File file) {
        String methodName = GlobalMethord.上传附件;
        // String methodName = "FileUpDownload/FileUploadGetPath/";
        Attach attach = uploadFile(file, methodName, "");
        return attach.Address;
    }

    /**
     * android上传一个文件到服务器,返回附件实体类
     *
     * @param file 需要上传的文件
     * @return 返回附件实体类
     */
    public static Attach uploadFileByHttpGetAttach(File file) {
        String methodName = GlobalMethord.上传附件;
        Attach attach = uploadFile(file, methodName, "");
        return attach;
    }

    /**
     * android上传一个文件到服务器,返回附件实体类
     *
     * @param file     需要上传的文件
     * @param listener 进度监听
     * @return 返回附件实体类
     */
    public static Attach uploadFileByProgressListener(File file, ProgressListener listener) {
        progressListener = listener;
        String methodName = GlobalMethord.上传附件;
        Attach attach = uploadFile(file, methodName, "");
        return attach;
    }

    /**
     * android上传文件到服务器 HTTP方式请求只能上传小文件
     *
     * @param file 需要上传的文件
     * @return 返回响应的内容:附件编号
     */
    public static String uploadFileGetAttachId(String type, File file) {
        // String methodName = "FileUpDownload/attachupload/";
        // 以后的接口全部使用fileupload
        String methodName = GlobalMethord.上传附件;// + "?category=android_" + type
        Attach attach = uploadFile(file, methodName, type);
        Logger.i(TAG + attach.toString());
        return attach.getUuid() + "";
    }

    /**
     * 上传附件一组图片
     *
     * @param filePathList
     * @return 返回一组上传图片的附件号
     * @author KJX update 2016-5-18
     */
    public static String uploadMultipleFiles(final String type, final List<String> filePathList, final boolean isFile,
                                             final IOnUploadMultipleFileListener onUploadMultipleFileListener) {
        final StringBuilder sbAttachIds = new StringBuilder(); // 存储照片上传后返回的附件号
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        /**
         * 将生产新的异步任务与使用已完成任务的结果分离开来的服务。生产者 submit 执行的任务。使用者 take
         * 已完成的任务，并按照完成这些任务的顺序处理它们的结果。
         */
        CompletionService<String> completionService = new ExecutorCompletionService<String>(
                threadPool);
        int count = 0;

        if (onUploadMultipleFileListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onUploadMultipleFileListener.onStartUpload(filePathList
                            .size());
                }
            });
        }

        for (int i = 0; i < filePathList.size(); i++) {
            final String path = filePathList.get(i);
            Logger.i("attachcurrentThread" + path);
            completionService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String thumbPath = "";
                    File file = new File(path);
                    if (file.exists()) {
                        //如果是图片类型
                        if (checkEndsWithInStringArray(file.getName(), BoeryunApp.getInstance().getResources()
                                .getStringArray(R.array.fileEndingImage))) {
                            thumbPath = BitmapHelper.createThumbBitmap(path);
                        } else {
                            thumbPath = path;
                        }
                    }
                    /*if (isFile) {
                        thumbPath = BitmapHelper.createThumbBitmap(path);
                    } else {
                        thumbPath = path;
                    }*/
                    String attachId = UploadHelper
                            .uploadFileGetAttachId(type, new File(thumbPath));
                    Logger.i("attachcurrentThread" + Thread.currentThread()
                            .getName() + "---->" + attachId);
                    return attachId;
                }
            });

            String attachId;
            try {
                attachId = completionService.take().get();
                if (!TextUtils.isEmpty(attachId)) {
                    sbAttachIds.append(attachId).append(",");
                    count++;
                    final int resultCount = count;
                    if (onUploadMultipleFileListener != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onUploadMultipleFileListener
                                        .onProgressUpdate(resultCount);
                            }
                        });
                    }
                }
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }

//        for (int i = 0; i < filePathList.size(); i++) {
//
//        }
        threadPool.shutdown();
        String attachIds = sbAttachIds.toString();

        if (!TextUtils.isEmpty(attachIds) && attachIds.endsWith(",")) {
            attachIds = attachIds.substring(0, attachIds.length() - 1);
        }

        final String resultIds = attachIds;

        if (onUploadMultipleFileListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onUploadMultipleFileListener.onComplete(resultIds);
                }
            });
        }
        return resultIds;
    }


    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private static boolean checkEndsWithInStringArray(String checkItsEnd,
                                                      String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }

    public interface ProgressListener {
        void onProgressListener(int progress);
    }

    public void setOnProgressListener(ProgressListener listener) {
        progressListener = listener;
    }


}
