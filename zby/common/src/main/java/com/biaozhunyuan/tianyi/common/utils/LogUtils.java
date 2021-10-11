package com.biaozhunyuan.tianyi.common.utils;

import android.util.Log;

/***
 * 自定义打印日志，当发布时隐藏除了错误信息之外的所有日志
 *
 * @author K 2015/06/26 09:39
 */
public class LogUtils {
    // 打印级别
    public final static boolean DEBUG_MODE = true;

    /***
     * 打印信息
     *
     * @param tag
     * @param msg
     *            信息内容
     */
    public static void i(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.i(tag, buildMessage(msg));
        }
    }

    /***
     * 打印信息
     *
     * @param tag
     * @param msg
     *            信息内容
     */
    public static void d(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.d(tag, buildMessage(msg));
        }
    }

    /***
     * 打印信息
     *
     * @param tag
     * @param msg
     *            信息内容
     */
    public static void v(String tag, String msg) {
        if (DEBUG_MODE) {
            Log.v(tag, buildMessage(msg));
        }
    }

    /***
     * 打印信息
     *
     * @param tag
     * @param msg
     *            信息内容
     */
    public static void e(String tag, String msg) {
        Log.e(tag, buildMessage(msg));
    }

    protected static String buildMessage(String msg) {

        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        String stackTrace = new StringBuilder().append(caller.getFileName()).append(caller.getMethodName()).append(caller.getLineNumber()).append(msg).toString();
//        return stackTrace;
        return caller.toString() + msg;

    }
}
