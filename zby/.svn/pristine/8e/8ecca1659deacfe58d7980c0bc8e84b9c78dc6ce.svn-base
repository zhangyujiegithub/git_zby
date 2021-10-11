package com.biaozhunyuan.tianyi.common.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;
import com.biaozhunyuan.tianyi.common.model.user.User;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 屏幕显示帮助类
 *
 * @author bohr
 */
public class ViewHelper {

    private static final String FORMAT_STR_DATE_AND_TIME = "yyyy-MM-dd kk:mm:ss";
    private static final String FORMAT_STR_DATE_AND_TIME_NO_SECONDS = "yyyy-MM-dd kk:mm";
    private static final String FORMATDATE_AND_TIME = "yyyy-MM-dd HH:mm";
    private static final String FORMATDATE_AND_HOUR = "MM-dd HH:mm";
    private static final String FORMAT_STR_DATE = "yyyy-MM-dd";

    private static final String FORMAT_STR_TIME = "kk:mm:ss";
    private static Random random;

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return widthPixels
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度 heightPixels
     *
     * @param context
     * @return heightPixels
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top; // 状态栏高度
        Log.i("statusBarHeight=", "statusBarHeight=" + statusBarHeight);
        return statusBarHeight;
    }

    /**
     * 把px 转化为dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;// 密度
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    /**
     * 把dip转化为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dip2px(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;// 密度
        float px = dp * density + 0.5f;
        return px;
    }

    /**
     * 把dip转化为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2pxInt(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;// 密度
        float px = dp * density + 0.5f;
        return (int) px;
    }

    /**
     * 把sp转化为px
     *
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * 把px转化为sp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, int px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity + 0.5);
    }

    /**
     * 获取当前时间的字符串格式yyyy-MM-dd kk:mm:ss 24小时制
     *
     * @return
     */
    public static String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间的字符串格式yyyy-MM-dd kk:mm24小时制
     *
     * @return
     */
    public static String getDateStringNoSeconds() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME_NO_SECONDS);
        return sdf.format(new Date());
    }

    /**
     * 日期的字符串格式yyyy-MM-dd hh-mm
     *
     * @return
     */
    public static String formatStrDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE);
        return sdf.format(date);
    }

    /**
     * 获取今天日期的字符串格式yyyy-MM-dd
     *
     * @return
     */
    public static String getDateToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取今天日期
     *
     * @return
     */
    public static String getDateToday(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 日期的字符串格式yyyy-MM-dd hh-mm
     *
     * @return
     */
    public static String formatStrDateHourAndMinute(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE_AND_TIME);
        return sdf.format(date);
    }

    /**
     * 获取当前的时间字符串格式kk:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_TIME);
        return sdf.format(new Date());
    }

    /**
     * 获取当前的时间字符串格式yyyy-MM-dd kk:mm
     *
     * @return
     */
    public static String getCurrentStringTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATDATE_AND_TIME);
        return sdf.format(new Date());
    }

    /**
     * 获取当前的时间字符串格式yyyy-MM-dd kk:mm:ss
     *
     * @return
     */
    public static String getCurrentFullTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME);
        return sdf.format(new Date());
    }

    /**
     * 获取今天日期的字符串格式 yyyy年MM月dd
     *
     * @return
     */
    public static String getDateTodayStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date());
    }

    /**
     * 格式化日期为 字符串格式yyyy-MM-dd kk:mm:ss
     *
     * @return
     */
    public static String formatDateToStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME);
        if (date != null) {
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 格式化日期为 字符串格式yyyy-MM-dd
     *
     * @return
     */
    public static String formatDateToStr1(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE);
        if (date != null) {
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 根据日期获得星期
     *
     * @param sDate yyyy-MM-dd
     * @return 当前日期是星期几
     * @author ＱＣ班长
     */
    public static String getFullDateWeekTime(String sDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(sDate);
            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            return format.format(date);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 将字符串yyyy-MM-dd转为日期
     *
     * @return 转换失败返回当前日期
     */
    @SuppressLint("SimpleDateFormat")
    public static Date formatStrToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE);
        Date result = new Date();
        if (TextUtils.isEmpty(dateStr)) {
            return result;
        }
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将字符串yyyy-MM-dd转为日期
     *
     * @return 转换失败返回当前日期
     */
    @SuppressLint("SimpleDateFormat")
    public static Date formatStrToDateAndTime(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME);
        Date result = new Date();
        if (TextUtils.isEmpty(dateStr)) {
            return result;
        }
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 转为日期
     *
     * @return 转换失败返回当前日期
     */
    @SuppressLint("SimpleDateFormat")
    public static Date formatStrToDateAndTime(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = new Date();
        if (TextUtils.isEmpty(dateStr)) {
            return result;
        }
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /***
     * 格式化日期为 字符串格式
     *
     * @param date   时间
     * @param format 日期格式化公式，如果非法则采用默认yyyy-MM-dd kk:mm:ss
     * @return
     */
    public static String formatDateToStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = "";
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            dateStr = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME)
                    .format(date);
        }
        return dateStr;
    }

    /***
     * 格式化日期为 字符串格式
     *
     * @param date   时间
     * @param format 日期格式化公式，如果非法则采用默认yyyy-MM-dd kk:mm:ss
     * @return
     */
    public static String formatStrToStr(String date, String format) {
        return formatDateToStr(formatStrToDateAndTime(date), format);
    }

    /***
     * 格式化日期为 字符串格式
     *
     * @param strTime   时间
     * @param formatType 日期格式化公式，如果非法则采用默认yyyy-MM-dd kk:mm:ss
     * @return
     */
    public static Date stringToDate(String strTime, String formatType) {
        SimpleDateFormat formatter;
        Date date = null;
        try {
            formatter = new SimpleDateFormat(formatType);
            date = formatter.parse(strTime);
        } catch (Exception e) {
            try {
                formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.parse(strTime);
            } catch (Exception e1) {
                try {
                    formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    date = formatter.parse(strTime);
                } catch (Exception e2) {

                }
            }
        }
        return date;
    }

    /**
     * 获取今天年月的字符串格式 yyyy-MM
     *
     * @return
     */
    public static String getDateMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(new Date());
    }


    /**
     * 获取明天这个时候的时间
     */
    public static String getTomorrowTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        Date time = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(time);
        return format;
    }

    /**
     * 获得本周的日期列表yyyy-MM-dd
     *
     * @return
     */
    public static List<String> getDateThisWeeks() {
        List<String> list = new ArrayList<String>();
        List<String> returnList = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        int weekday = date.getDay();// 获得星期中的第几天
        for (; weekday > 0; weekday--) {
            list.add(sdf.format(date));
            date = getYestody(date);
        }
        // 从小到达依次排序，存入returnList
        for (int i = list.size() - 1; i >= 0; i--) {
            returnList.add(list.get(i));
        }

        Date now = new Date();
        // 获得星期中的第几天
        weekday = now.getDay();
        // 多加了一天 2014-09-25 00:00:00
        int count = 8 - weekday;
        for (int i = 0; i < count; i++) {
            now = getTomorrow(now);
            returnList.add(sdf.format(now));
        }
        return returnList;
    }

    /**
     * 根据当前日期获得这一周的日期列表
     *
     * @return
     */
    public static List<String> getWeeks(final Date nowDate) {
        List<String> list = new ArrayList<String>();
        List<String> returnList = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = nowDate;
        // Date引用对象， 在字符串存时间值
        String dateValue = sdf.format(nowDate);
        int weekday = date.getDay();// 获得星期中的第几天
        int weekday2 = date.getDay();
        for (; weekday > 0; weekday--) {
            list.add(sdf.format(date));
            date = getYestody(date);
        }
        // 从小到达依次排序，存入returnList
        for (int i = list.size() - 1; i >= 0; i--) {
            returnList.add(list.get(i));
        }

        // 多加了一天 2014-09-25 00:00:00
        int count = 8 - weekday2;
        Date date2 = null;
        try {
            date2 = sdf.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < count; i++) {
            date2 = getTomorrow(date2);
            returnList.add(sdf.format(date2));
        }
        return returnList;
    }

    /**
     * 获取昨天日期的字符串格式yyyy-MM-dd
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getDateYestoday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date = getYestody(date);
        return sdf.format(date);
    }

    /**
     * 获取明天日期的字符串格式yyyy-MM-dd
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getDateTomorrow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date = getTomorrow(date);
        return sdf.format(date);
    }

    /**
     * 获得当前日期的前一天
     *
     * @param date
     * @return
     */
    public static Date getYestody(Date date) {
        int day = date.getDate();
        if (day > 1) {
            date.setDate(day - 1);
        } else { // 如果是本月第一天
            int month = date.getMonth();
            if (month > 1) {
                date.setMonth(month - 1);
                date.setDate(getDateNum(date.getYear(), date.getMonth()));
            } else if (month == 1) { // 1月1号,前一天为上一年的12月31
                date.setYear(date.getYear() - 1);
                date.setMonth(12);
                date.setDate(31);
            }
        }
        return date;
    }

    /**
     * 获得当前日期的后一天
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDate();
        // 获得本月最后一天
        int lastDay = getDateNum(year, month);
        if (day < lastDay) {
            date.setDate(day + 1);
        } else if (day == lastDay) { // 如果是本月最后一天
            if (month < 12) {
                date.setMonth(month + 1);
                date.setDate(1); // 下月1号
            } else if (month == 12) { // 12月31
                date.setYear(date.getYear() + 1);
                date.setMonth(1);
                date.setDate(1);
            }
        }
        Logger.d("weekdate" + ViewHelper.getDateString(date));
        return date;
    }

    /**
     * 获得当前日期上一周的日期
     *
     * @param date
     * @return
     */
    public static Date getBeforWeekDate(Date date) {
        for (int i = 0; i < 7; i++) {
            date = getYestody(date);
        }
        return date;
    }

    /**
     * 获得当前日期下一周的日期
     *
     * @param date
     * @return
     */
    public static Date getAfterWeekDate(Date date) {
        for (int i = 0; i < 7; i++) {
            date = getTomorrow(date);
        }
        return date;
    }

    /**
     * 计算某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    private static int getDateNum(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year + 1900);
        time.set(Calendar.MONTH, month);
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间的字符串格式yyyy-MM-dd
     *
     * @return
     */
    public static String getDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * 获取格式化时间的字符串格式MM月dd日 HH:mm
     *
     * @return
     */
    public static String getDateStringFormat(String time, String format) {
        Date date = formatStrToDateAndTime(time, "yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * 获取格式化时间的字符串格式MM月dd日 HH:mm
     *
     * @return
     */
    public static String resetDateStringFormat(String time, String format) {
        Date date = formatStrToDateAndTime(time, format);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * 获取格式化时间的字符串格式MM月dd日 HH:mm
     *
     * @return
     */
    public static String getDateStringFormat(String time) {
        Date date = formatStrToDateAndTime(time, "yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * 获取格式化时间的字符串格式MM月dd日 HH:mm
     *
     * @return
     */
    public static String getStringFormat(String time, String format) {
        Date date = formatStrToDateAndTime(time, "yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * 获取格式化时间的字符串格式MM月dd日 HH:mm
     *
     * @return
     */
    public static String formatString(String time) {
        String format = "";
        if (isToday(time)) {
            return "今天";
        }
        Date date = formatStrToDateAndTime(time, "yyyy-MM-dd kk:mm:ss");
        if (date == null) {
            return "";
        }
        if (isYestYesterday(date)) {
            return "昨天";
        }
        if (isThisYear(date)) {
            format = "yyyy-MM-dd";
        } else {
            format = "MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 判断时间是否为昨天
     *
     * @param date
     * @return
     */
    private static boolean isYestYesterday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() + 1 == now.getDate());
    }

    /**
     * 判断时间是否为今年
     *
     * @param date
     * @return
     */
    public static boolean isThisYear(Date date) {
        Date now = new Date();
        return date.getYear() == now.getYear();
    }

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获得两个日期间距多少天
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(beginDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
        toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
        toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
        toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

        long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / 24 / 3600 / 1000;
//        dayDistance = Math.abs(dayDistance);

        return dayDistance;
    }

    /**
     * 获取格式化时间的字符串格式 HH:mm
     *
     * @return
     */
    public static String getHourMinFormat(String time) {
        Date date = formatStrToDateAndTime(time, "yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /***
     * 格式化日期字符串格式 为指定format格式
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static String convertStrToFormatDateStr(String dateStr, String format) {
        try {
            Date date = formatStrToDateAndTime(dateStr, FORMAT_STR_DATE_AND_TIME);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }


    /***
     * 格式化日期字符串格式
     *
     * @param dateStr
     * @return yyyy-MM-dd kk:mm:ss
     */
    public static String convertStrToFormatDateStr(String dateStr) {
        return convertStrToFormatDateStr(dateStr, FORMAT_STR_DATE_AND_TIME);
    }

    /**
     * 获取版本号
     *
     * @param context 应用程序的上下文
     * @return 应用程序的版本号
     */
    public static String getVersionName(Context context) {
        String version = null;
        // 获得包管理器
        PackageManager pm = context.getPackageManager();
        try {
            // 封装了关于该应用程序的所有的功能清单中的数据
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName; // 版本号 1.01，给客户看
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取版本号
     *
     * @param context 应用程序的上下文
     * @return 应用程序的版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        // 获得包管理器
        PackageManager pm = context.getPackageManager();
        try {
            // 封装了关于该应用程序的所有的功能清单中的数据
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode; // 版本号 1.01，给程序看
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取设备的IMEI,唯一的设备标志
     *
     * @param context 应用程序的上下文
     * @return 获取设备的IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取经过信鸽加密处理的Token
     *
     * @param context 应用程序的上下文
     * @return 获取设备的IMEI
     */
    public static String getDeviceToken(Context context) {
        // 获取设备唯一通行证
//        final String token = XGPushConfig.getToken(context);
//        return token;
        return "";
    }

    /**
     * 获取本周一第一天的日期
     */
    public static String getFirstDateStrOfThisWeek() {
        return formatDateToStr(getFirstDateOfThisWeek(), FORMAT_STR_DATE);
    }

    /**
     * 获取本周一最后一天的日期
     */
    public static String getLastDateStrOfThisWeek() {
        return formatDateToStr(getLastDateOfThisWeek(), FORMAT_STR_DATE);
    }

    /**
     * 获取本周一第一天的日期
     */
    public static Date getFirstDateOfThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 获取当前时间 是本周中的第几天 星期一为1
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, -(dayofWeek - 1));
        System.out.println("周一:" + calendar.get(Calendar.DATE));
        return calendar.getTime();
    }

    /**
     * 获取本周一最后一天的日期
     */
    public static Date getLastDateOfThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFirstDateOfThisWeek());
        // // 获取当前时间 是本周中的第几天 星期一为1
        // int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        // System.out.println(dayofWeek + "---" + (dayofWeek + 6));
        // calendar.add(Calendar.DATE, -(dayofWeek - 1));
        // System.out.println("周一:" + calendar.get(Calendar.DATE));
        calendar.add(Calendar.DATE, 6);
        System.out.println("周日:" + calendar.get(Calendar.DATE));

        return calendar.getTime();
    }

    /**
     * 获取本周一的日期;格式: 2015-11-02 ;形式：字符串
     */
    public static String getMonday() {
        SimpleDateFormat 格式 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar 日历 = Calendar.getInstance(Locale.CHINA);
        日历.setTimeInMillis(System.currentTimeMillis());
        日历.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return 格式.format(日历.getTime());
    }

    /**
     * 获取本周五的日期;格式: 2016-09-16; 形式：字符串
     */
    public static String getFriday() {
        SimpleDateFormat 格式 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar 日历 = Calendar.getInstance(Locale.CHINA);
        日历.setTimeInMillis(System.currentTimeMillis());
        日历.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return 格式.format(日历.getTime());
    }

    /**
     * 获取本周日的日期;格式: 2016-09-16; 形式：字符串
     */
    public static String getSunday() {
        SimpleDateFormat 格式 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar 日历 = Calendar.getInstance(Locale.CHINA);
        日历.setTimeInMillis(System.currentTimeMillis());
        日历.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        日历.add(Calendar.DAY_OF_YEAR, 7);
        return 格式.format(日历.getTime());
    }

    public static Date getLastDateOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // calendar.set(Calendar.DAY_OF_MONTH, 1); // 本月第一天
        // System.out.println("月初:" + calendar.get(Calendar.DATE));
        // 本月最后一天
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getMaximum(Calendar.DAY_OF_MONTH));
        // System.out.println("y:" + calendar.get(Calendar.DATE));
        return calendar.getTime();
    }

    public static Date getFirstDateOfThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 本月第一天
        System.out.println("月初:" + calendar.get(Calendar.DATE));
        return calendar.getTime();
    }

    /**
     * Long类型时间->转换成日期->转成要求格式的String类型
     */
    public static String fromLongToDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR_DATE_AND_TIME);
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获得当前时间所在月份的上个月的最后一天所在日期
     *
     * @param t 当前日期
     * @return
     */
    public static String getLastMonthDay(Date t) {
        Calendar cal = Calendar.getInstance();
        Date date = t;
        cal.setTime(date);
        int year = 0;
        int month = cal.get(Calendar.MONTH); // 上个月月份
        //设置年月
        if (month == 0) {
            year = cal.get(Calendar.YEAR) - 1;
            month = 12;
        } else {
            year = cal.get(Calendar.YEAR);
        }
        //设置天数
        String temp = year + "-" + month;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date d = null;
        try {
            d = format.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String endDay = year + "-" + month + "-" + day;
        return endDay;
    }


    /**
     * 得到某月的天数
     *
     * @param source String source = "2007年12月";
     * @return
     */
    public static int getMonthDay(String source) {
        int count = 30;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        try {
            Date date = format.parse(source);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取指定日期 第 past 天的日期
     *
     * @param past 正数表示该日期后n天，负数表示该日期的前n天
     */
    public static String getFetureDateStr(String dateStr, int past) {
        // 时间表示格式可以改变，yyyyMMdd需要写例如20160523这种形式的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
        Date date = sdf.parse(dateStr, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, past);
        Date date1 = calendar.getTime();
        return sdf.format(date1);
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static Date getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        return today;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDateStr(int past) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        return sdf1.format(calendar.getTime());
    }

    /**
     * 获取过去 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static Date getFormerlyDate(int past) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -past);
        return calendar1.getTime();
    }

    /**
     * 获取过去 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFormerlyDateStr(int past) {
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar1.add(Calendar.DATE, -past);
        String format = sdf1.format(calendar1.getTime());
        return format;
    }


    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeCompareSize(String startTime, String endTime) {
        int i = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(startTime);//开始时间
            Date date2 = dateFormat.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //正常情况下的逻辑操作.
                i = 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 判断2个时间大小
     * yyyy-MM-dd HH:mm 格式（自己可以修改成想要的时间格式）
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getTimeCompareSize(String startTime, String endTime, SimpleDateFormat Format) {
        int i = 0;
        try {
            Date date1 = Format.parse(startTime);//开始时间
            Date date2 = Format.parse(endTime);//结束时间
            // 1 结束时间小于开始时间 2 开始时间与结束时间相同 3 结束时间大于开始时间
            if (date2.getTime() < date1.getTime()) {
                i = 1;
            } else if (date2.getTime() == date1.getTime()) {
                i = 2;
            } else if (date2.getTime() > date1.getTime()) {
                //正常情况下的逻辑操作.
                i = 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 计算两点之间距离
     *
     * @paramstart
     * @paramend
     * @return米
     */
    public static double getDistance(LatLng start, LatLng end) {
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        //地球半径
        double R = 6371;
        //两点间距离km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

    /**
     * 日期格式转换
     * formatStart:转换前的格式
     * formatEnd:转换后的格式
     */
    public static String turnDate(String date, String formatStart, String formatEnd) {
        SimpleDateFormat simpleDateFormat = null;
        SimpleDateFormat simpleDateFormat1 = null;
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(formatStart);
            simpleDateFormat1 = new SimpleDateFormat(formatEnd);
        }
        try {
            Date parse = simpleDateFormat.parse(date);
            String format = simpleDateFormat1.format(parse);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * String保留N位小数。
     *
     * @param a
     * @return
     */
    public static String stringToDouble(String a, String pattern) {
        double b = Double.valueOf(a);
        DecimalFormat df = new DecimalFormat("0." + pattern);
        String temp = df.format(b);
        return temp;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(Date sdate) {
        boolean b = false;
        Date today = new Date();
        if (sdate != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(sdate);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * latest转换user
     *
     * @param latest
     * @return
     */

    public static User turnUser(Latest latest) {
        User user = new User();
        user.setUuid(latest.getUuid());
        user.setName(latest.getName());
        user.setSelected(latest.isSelected());
        user.setAvatar(latest.getAvatar());
        user.setMobile(latest.getMobile());
        user.setTelephone(latest.getTelephone());
        user.setEnterpriseMailbox(latest.getEnterpriseMailbox());
        user.setPost(latest.getPost());
        return user;
    }

    /**
     * user转换latest
     *
     * @param user
     * @return
     */

    public static Latest turnLatest(User user) {
        Latest latest = new Latest();
        latest.setUuid(user.getUuid());
        latest.setName(user.getName());
        latest.setSelected(user.isSelected());
        latest.setAvatar(user.getAvatar());
        latest.setMobile(user.getMobile());
        latest.setTelephone(user.getTelephone());
        latest.setEnterpriseMailbox(user.getEnterpriseMailbox());
        latest.setPost(user.getPost());
        return latest;
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        //默认保留两位会有错误，这里设置保留小数点后4位
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * ListView嵌套ListView,子ListView展示不全
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /**
     * 给edittext设置提示文字大小
     *
     * @param editText
     * @param hintText
     * @param size
     */
    public static void setEditTextHintSize(EditText editText, String hintText, int size) {
        SpannableString ss = new SpannableString(hintText);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss));
    }

    public static boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * 取姓名的最后1个字
     */
    public static String getUserNamebyStr(String name) {
        String lastStr = "";
        if (!TextUtils.isEmpty(name)) {
            if (name.length() <= 1) {
                lastStr = name;
            } else {
                lastStr = name.substring(name.length() - 1, name.length());
            }
        }
        return lastStr;
    }

    /**
     * 从一个范围内取随机数
     */
    public static int getRandom(int min, int max) {
        if (random == null) {
            random = new Random();
        }
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 给recyclerview设置左右边距
     *
     * @param context
     * @param recyclerView
     * @param left
     * @param right
     */
    public static void setRecycleViewLRPadding(Context context, RecyclerView recyclerView, int left, int right) {
        recyclerView.setPadding(ViewHelper.dip2pxInt(context, left), 0, ViewHelper.dip2pxInt(context, right), 0);
    }

    /**
     * 功能点list排序
     *
     * @param list
     */
    public static void sortList(List<FunctionBoard> list) {
        //重新排序
        Collections.sort(list, new Comparator<FunctionBoard>() {
            @Override
            public int compare(FunctionBoard o1, FunctionBoard o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
    }


    /**
     * 根据日期来获取年份的数字
     *
     * @param date 日期
     */
    public static int getYearByDate(String date) {
        if (!TextUtils.isEmpty(date) && date.length() > 3) {
            try {
                return Integer.valueOf(date.substring(0, 4));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 根据日期来获取月份的数字
     *
     * @param date 日期
     */
    public static int getMonthByDate(String date) {
        if (!TextUtils.isEmpty(date) && date.length() > 6) {
            try {
                return Integer.valueOf(date.substring(5, 7));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 根据日期来获取日的数字
     *
     * @param date 日期
     */
    public static int getDayByDate(String date) {
        if (!TextUtils.isEmpty(date) && date.length() > 9) {
            try {
                return Integer.valueOf(date.substring(8, 10));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }


    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static ArrayList<String> getTotalDaysList(int intervals) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        ArrayList<String> fetureDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            pastDaysList.add(getPastDate(i));
            fetureDaysList.add(getFetureDateTime(i));
        }
        Collections.reverse(pastDaysList);
        pastDaysList.addAll(fetureDaysList);
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDateTime(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
}
