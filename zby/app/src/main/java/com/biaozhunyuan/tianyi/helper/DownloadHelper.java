package com.biaozhunyuan.tianyi.helper;

import android.widget.ProgressBar;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * 下载文件帮助类
 *
 * @author K
 *
 */
public class DownloadHelper {

    /***
     * 下载成功
     */
    public final static int SUCCEDD_DOWNLOAD = 107;

    private static DownloadHelper mdDownloadHelper;

    /**
     * 下载文件名称集合，避免添加重复
     */
    private HashSet<String> mNameSet = new HashSet<String>();

    private ExecutorService mThreadPool = Executors.newFixedThreadPool(2);

    private DownloadHelper() {

    }

    public static DownloadHelper getInstance() {
        if (mdDownloadHelper == null) {
            mdDownloadHelper = new DownloadHelper();
        }
        return mdDownloadHelper;
    }

    public void download(String url, String fileName, ProgressBar pBar) {
        if (mNameSet.contains(fileName)) {
            return;
        } else {
            mNameSet.add(fileName);
            mThreadPool.execute(new DownloadRunnable(fileName, url, pBar));
        }
    }

    private DownloadListener mDownloadListener;

    public void setOnDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    public interface DownloadListener {
        void complete();
    }

    /***
     * 下载文件线程
     *
     */
    class DownloadRunnable implements Runnable {
        private String fileName;
        private String url;
        private ProgressBar mPbar;

        /**
         * @param url
         */
        public DownloadRunnable(String fileName, String url, ProgressBar pBar) {
            this.fileName = fileName;
            this.url = url;
            this.mPbar = pBar;
        }

        @Override
        public void run() {
            downloadData(url, fileName, mPbar);
        }
    }

    /**
     * 访问网络,下载图片数据,并存储到本地DCIM目录
     *
     * @param url url地址
     */
    private void downloadData(String url, String fileName, ProgressBar pBar) {
        // 文件存放路径
        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                + fileName;
        Logger.i("filePath" + filePath);

        String cookie = CookieUtils.cookieHeader(url);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URL uri = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("connection", "keep-Alive");
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int b = 0;
            byte[] byArr = new byte[1024];
            while ((b = bis.read(byArr)) != -1) {
                bos.write(byArr, 0, b);
                if (pBar != null) {
                    pBar.incrementProgressBy(b);
                }
            }
            if (mDownloadListener != null) {
                mDownloadListener.complete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
