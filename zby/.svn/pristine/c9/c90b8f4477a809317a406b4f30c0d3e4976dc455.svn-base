package com.biaozhunyuan.tianyi.common.attach;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.R;
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
public class BoeryunDownloadManager {

    // 下载状态：正常，暂停，下载中，已下载，排队中
    public static final int DOWNLOAD_STATE_NORMAL = 0x00;
    public static final int DOWNLOAD_STATE_PAUSE = 0x01;
    public static final int DOWNLOAD_STATE_DOWNLOADING = 0x02;
    public static final int DOWNLOAD_STATE_FINISH = 0x03;
    public static final int DOWNLOAD_STATE_WAITING = 0x04;

    /***
     * 下载成功
     */
    public final static int SUCCEDD_DOWNLOAD = 107;

    private static BoeryunDownloadManager mdDownloadHelper;

    /**
     * 下载文件名称集合，避免添加重复
     */
    private HashSet<String> mNameSet = new HashSet<String>();

    private ExecutorService mThreadPool = Executors.newFixedThreadPool(2);

    private Handler mHandler;
    private DownloadListener mDownloadListener;

    private BoeryunDownloadManager() {

    }

    public static BoeryunDownloadManager getInstance() {
        if (mdDownloadHelper == null) {
            mdDownloadHelper = new BoeryunDownloadManager();
        }
        return mdDownloadHelper;
    }

    public void download(DownloadFile downloadFile) {
//        if (mNameSet.contains(downloadFile.attachName)) {
//            return;
//        } else {
//            mNameSet.add(downloadFile.attachName);
        mThreadPool.execute(new DownloadRunnable(downloadFile));
//        }
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    // 更新listview中对应的item
    public void update(DownloadFile downloadFile) {
        Message msg = mHandler.obtainMessage();
        if (downloadFile.totalSize == downloadFile.downloadSize) {
            downloadFile.downloadState = DOWNLOAD_STATE_FINISH;
        }
        msg.obj = downloadFile;
        msg.sendToTarget();
    }


    /**
     * 访问网络,下载图片数据,并存储到本地DCIM目录
     *
     * @param
     */
    private void downloadDataUrl(DownloadFile downloadFile) {

        // 文件存放路径
        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                + downloadFile.attachName;
        Logger.i("filePath" + filePath);

        String uri = downloadFile.url.replace("\\", "/");
        Logger.d("filepath" + uri);

        String cookie = CookieUtils.cookieHeader(uri);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                downloadFile.downloadSize += b;
                update(downloadFile);
            }

            downloadFile.downloadState = DOWNLOAD_STATE_FINISH;
            if (mDownloadListener != null) {
                mDownloadListener.complete();
            }
            update(downloadFile);
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

    public void setOnDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    public interface DownloadListener {
        void complete();
    }

    /***
     * 打开附件文件的方法
     *
     * @param name
     *            后缀名
     */
    public void open(Context mContext, String name) {
        String fileName = FilePathConfig.getCacheDirPath() + File.separator
                + name;
        File currentPath = new File(fileName);
        if (currentPath != null && currentPath.isFile()) {
            Logger.i("pathname" + "-->" + fileName);
            Intent intent = null;
            if (checkEndsWithInStringArray(fileName, mContext.getResources()
                    .getStringArray(R.array.fileEndingImage))) {
                intent = OpenFilesIntent.getImageFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingWebText))) {
                intent = OpenFilesIntent.getHtmlFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPackage))) {
                intent = OpenFilesIntent.getApkFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingAudio))) {
                intent = OpenFilesIntent.getAudioFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingVideo))) {
                intent = OpenFilesIntent.getVideoFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingText))) {
                intent = OpenFilesIntent.getTextFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPdf))) {
                intent = OpenFilesIntent.getPdfFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingWord))) {
//                intent = OpenFilesIntent.getWordFileIntent(currentPath);
                intent = OpenFilesIntent.getWpsFileIntent(currentPath, mContext);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingExcel))) {
                intent = OpenFilesIntent.getExcelFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPPT))) {
                intent = OpenFilesIntent.getPPTFileIntent(currentPath);
            } else {
                intent = OpenFilesIntent.getOtherFileIntent(currentPath);
            }

            if (intent != null) {
                try {
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("Open" + e.getMessage() + "");
                    Toast.makeText(mContext, "系统未检测到打开文件的程序，请选择",
                            Toast.LENGTH_LONG).show();
                    intent = OpenFilesIntent.getOtherFileIntent(currentPath);
                    try {
                        mContext.startActivity(intent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Logger.e("Open2" + e.getMessage() + "");
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "抱歉,这不是一个合法文件！", Toast.LENGTH_LONG).show();
        }
    }

    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }


    /***
     * 下载文件线程
     *
     */
    class DownloadRunnable implements Runnable {
        private DownloadFile mDownloadFile;

        /**
         * 是否正在下载
         */
        private boolean isWorking;

        /**
         * @param
         */
        public DownloadRunnable(DownloadFile downloadFile) {
            this.mDownloadFile = downloadFile;
            // isWorking = true;
        }

        @Override
        public void run() {
            mDownloadFile.downloadState = DOWNLOAD_STATE_DOWNLOADING; // 开始下载状态
            downloadDataUrl(mDownloadFile);
        }
    }
}
