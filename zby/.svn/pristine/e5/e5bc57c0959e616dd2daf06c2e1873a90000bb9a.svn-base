package com.biaozhunyuan.tianyi.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtil {
    /**
     * 将json时间类型 转换成String，格式为 yyyy-MM-dd HH:mm
     */
    public static String ConvertLongDateToString(String date_s) {
        String str = "";
        try {
            Date date = null;
            String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
            Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
            Matcher matcher = pattern.matcher(date_s);
            String result = matcher.replaceAll("$2");
            date = new Date(new Long(result));

            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm");
            str = format.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    /**
     * 转换成 yyyy-MM-dd 格式
     */
    public static String ConvertDateToString(String date_s) {
        String str = "";
        try {
            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd");

            Date date = format.parse(date_s);

            str = format.format(date);
        } catch (Exception ex) {
        }
        return str;
    }

    /**
     * 转换成 yyyy-MM-dd 格式
     */
    public static Date ConvertStringDateToDate(String date_s) {
        Date date = null;
        try {
            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd");
            date = format.parse(date_s);
        } catch (Exception ex) {
            date = new Date();
        }
        return date;
    }

    /**
     * 转换成 yyyy-MM-dd HH:mm 格式
     */
    public static String ConvertLongDateToString(Date date) {
        String str = "";
        try {
            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm");
            str = format.format(date);
        } catch (Exception ex) {
        }
        return str;
    }

    /**
     * 将 yyyy-MM-dd HH:mm 格式 的字符日期 转为Date
     */
    public static Date ConvertStringToLongDate(String date) {
        Date dateReturn = null;
        try {
            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            dateReturn = format.parse(date);
        } catch (Exception ex) {
        }
        return dateReturn;
    }

    /**
     * 转换成yyyy-MM-dd 格式
     */
    public static String ConvertDateToString(Date date) {
        String str = "";
        try {
            java.text.SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd");
            str = format.format(date);
        } catch (Exception ex) {
        }
        return str;
    }

    //根据日期取得星期几
    public static String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp 毫秒
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis();
        long time = (curTime - timeStamp) / (long) 1000;
        if (time < 10 && time >= 0) {
            return "刚刚";
        } else if (time < 60 && time >= 10) {
            return time + "秒前";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 一个时间是否在一段时间内
     *
     * @param nowDate   要计算的时间
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static boolean yearMonthBetween(String nowDate, String startDate, String endDate) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date now = format.parse(nowDate);
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);

        long nowTime = now.getTime();
        long startTime = start.getTime();
        long endTime = end.getTime();

        return nowTime >= startTime && nowTime <= endTime;
    }


    /**
     * 计算两个时间的间隔天数
     *
     * @param stime
     * @param etime
     * @return
     */

    public static List<String> getBetweenDayList(String stime, String etime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = null;
        Date eDate = null;
        try {
            sdate = df.parse(stime);
            eDate = df.parse(etime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long betweendays = (long) ((eDate.getTime() - sdate.getTime()) / (1000 * 60 * 60 * 24) + 0.5);//天数间隔
        Calendar c = Calendar.getInstance();
        List<String> list = new ArrayList<String>();
        while (sdate.getTime() <= eDate.getTime()) {
            list.add(df.format(sdate));
            System.out.println(df.format(sdate));
            c.setTime(sdate);
            c.add(Calendar.DATE, 1); // 日期加1天
            sdate = c.getTime();
        }
        return list;
    }


    /**
     * 计算两个时间的间隔天数
     *
     * @param stime
     * @param etime
     * @return
     */
    public static int getBetweenDays(String stime, String etime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = null;
        Date eDate = null;
        try {
            sdate = df.parse(stime);
            eDate = df.parse(etime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) (long) ((eDate.getTime() - sdate.getTime()) / (1000 * 60 * 60 * 24) + 0.5);//天数间隔
    }


    /**
     * 比较两个时间大小
     *
     * @param DATE1 时间一
     * @param DATE2 时间二
     * @return 如果时间一在时间二之前，返回-1；
     * 如果时间一在时间二之后，返回1;
     * 如果时间一与时间二想等，返回0
     */
    public static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * 比较两个时间大小
     *
     * @param DATE1 时间一
     * @param DATE2 时间二
     * @return 如果时间一在时间二之前，返回-1；
     * 如果时间一在时间二之后，返回1;
     * 如果时间一与时间二想等，返回0
     */
    public static int compareDateByDay(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 日期格式化
     *
     * @return
     */
    public static String dateformatTime(Date date, boolean isShowTime) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        if (isThisYear(date)) {//今年
            if (isToday(date)) { //今天
                return "今天";
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return "昨天";
                } else if (isTomorrowDay(date)) {//本周,显示周几
                    return "明天";
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }

    /**
     * 日期格式化
     *
     * @return
     */
    public static String dateformatTime(Date date) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        long time = date.getTime();
        if (isThisYear(date)) {//今年
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            if (isToday(date)) { //今天
                int minute = minutesAgo(time);
                if (minute < 60) {//1小时之内
                    if (minute <= 1) {//一分钟之内，显示刚刚
                        return "刚刚";
                    } else {
                        return minute + "分钟前";
                    }
                } else {
                    return simpleDateFormat.format(date);
                }
            } else {
                if (isYestYesterday(date)) {//昨天，显示昨天
                    return "昨天 " + simpleDateFormat.format(date);
                } else if (isThisWeek(date)) {//本周,显示周几
                    String weekday = null;
                    if (date.getDay() == 1) {
                        weekday = "周一";
                    }
                    if (date.getDay() == 2) {
                        weekday = "周二";
                    }
                    if (date.getDay() == 3) {
                        weekday = "周三";
                    }
                    if (date.getDay() == 4) {
                        weekday = "周四";
                    }
                    if (date.getDay() == 5) {
                        weekday = "周五";
                    }
                    if (date.getDay() == 6) {
                        weekday = "周六";
                    }
                    if (date.getDay() == 0) {
                        weekday = "周日";
                    }
                    return weekday + " " + simpleDateFormat.format(date);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(date);
                }
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(date);
        }
    }

    private static int minutesAgo(long time) {
        return (int) ((System.currentTimeMillis() - time) / (60000));
    }

    private static boolean isToday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() == now.getDate());
    }

    private static boolean isYestYesterday(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() + 1 == now.getDate());
    }

    private static boolean isThisWeek(Date date) {
        Date now = new Date();
        if ((date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth())) {
            if (now.getDay() - date.getDay() < now.getDay() && now.getDate() - date.getDate() > 0 && now.getDate() - date.getDate() < 7) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTomorrowDay(Date date) {
        Date now = new Date();
        return (date.getYear() == now.getYear()) && (date.getMonth() == now.getMonth()) && (date.getDate() - 1 == now.getDate());
    }

    private static boolean isThisYear(Date date) {
        Date now = new Date();
        return date.getYear() == now.getYear();
    }
}
