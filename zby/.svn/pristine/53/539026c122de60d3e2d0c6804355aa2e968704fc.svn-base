package com.biaozhunyuan.tianyi.common.attach;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.helper.Logger;

import java.io.File;

/***
 * android系统通用打开文件意图Intent
 *
 * @author kjx 注释2015-04-24 （原作者不详）
 */
public class OpenFilesIntent {
    /**
     * android获取一个用于打开HTML文件的intent
     **/
    public static Intent getHtmlFileIntent(File file) {
        Uri uri = Uri.parse(file.toString()).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * android获取一个用于打开图片文件的intent
     */
    public static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * android获取一个用于打开PDF文件的intent
     */
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /*** android获取一个用于打开文本文件的intent */
    public static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /**
     * android获取一个用于打开音频文件的intent
     */
    public static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * ndroid获取一个用于打开视频文件的intent
     */
    public static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * android获取一个用于打开CHM文件的intent
     */
    public static Intent getChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * android获取一个用于打开Word文件的intent
     */
    public static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }


    /**
     * word文档 用WPS的方式打开
     *
     * @param file
     * @param mContext
     * @return
     */
    public static Intent getWpsFileIntent(File file, Context mContext) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
//        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true); // 保存时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, mContext.getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
//        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setData(uri);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * android获取一个用于打开Excel文件的intent
     **/
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * android获取一个用于打开PPT文件的intent
     **/
    public static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /***
     * android获取一个用于打开apk文件的intent
     */
    public static Intent getApkFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        return intent;
    }

    /***
     * android获取一个用于打开apk文件的intent
     */
    public static Intent getOtherFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BoeryunApp.getInstance().getApplicationContext(),
                    "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        // intent.setDataAndType(Uri.fromFile(file),
        // "application/vnd.android.package-archive");
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    /***
     * 打开附件文件的方法
     *
     * @param fileName
     *            后缀名
     */
    public static void open(Context mContext, String fileName) {
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
    private static boolean checkEndsWithInStringArray(String checkItsEnd,
                                                      String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }
}
