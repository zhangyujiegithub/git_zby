package com.biaozhunyuan.tianyi.common.helper;

import android.text.TextUtils;
import android.util.Log;

import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.biaozhunyuan.tianyi.common.helper.ViewHelper.formatStrToDateAndTime;


public class DateDeserializer implements JsonDeserializer<Date> {

    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
        Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
        Matcher matcher = pattern.matcher(json.getAsJsonPrimitive()
                .getAsString());
        String result = matcher.replaceAll("$2");
        return new Date(new Long(result));
    }

    /**
     * 判断两个yyyy-mm-dd的字符串时间大小
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 开始日期小于结束日期返回true, 否则返回false
     */
    public static Boolean compareDate(String startDate, String endDate) {
        Boolean result = false;
        String[] ss = startDate.split("-");
        String[] es = endDate.split("-");
        int[] starts = new int[ss.length];
        int[] ends = new int[es.length];
        for (int i = 0; i < ss.length; i++) {
            if (ss.length > 2) {
                ss[i] = ss[i].substring(0, 2);
            }
            starts[i] = Integer.valueOf(ss[i]);
        }

        for (int i = 0; i < es.length; i++) {
            if (es.length > 2) {
                es[i] = es[i].substring(0, 2);
            }
            ends[i] = Integer.valueOf(es[i]);
        }

        // 判断,当开始时间大于结束时间直接返回false
        for (int i = 0; i < ends.length; i++) {
            if (starts[i] > ends[i]) {
                return false;
            }
            result = true;
        }
        return result;
    }

    /**
     * 判断指定时间是否在当前时间之前
     */
    public static Boolean dateIsBeforoNow(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Boolean result = false;
        try {
            Date date = sdf.parse(dateStr);
            Date nowDate = new Date();
            nowDate.setHours(0);
            nowDate.setMinutes(0);
            nowDate.setSeconds(0);
            LogUtils.i("nowDate", nowDate.toString());
            if (date.before(nowDate)) {
                result = true;
            }
        } catch (ParseException e) {
            LogUtils.e("nowDate", e + "");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断是否是昨天
     *
     * @param endDate 判断日期
     */
    private static String getYestoday(String endDate) {
        String result = showDate(endDate);
        String startDate = getTodayDate();
        if (endDate.substring(0, 8).equals(startDate.substring(0, 8))) { // 年月相同
            // 判断,
            String today = startDate.substring(8, 10); // 今天号数
            String day = endDate.substring(8, 10); // 比较号数

            try {
                if (Integer.parseInt(today) - Integer.parseInt(day) == 1) {
                    result = "昨天";
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return result;
    }

    /**
     * 格式化时间
     * <p>
     * 显示今天，昨天 时间显示到分
     *
     * @param dateStr
     * @return
     */
    public static String getFormatTime(String dateStr) {
        String result = dateStr;
        String date = "";
        String time = "";

        if (TextUtils.isEmpty(dateStr)) {
            return date;
        }

        if (dateStr.contains(" ")) {
            String[] arr = dateStr.split(" ");
            date = arr[0];
            time = showTime(arr[1]); // 时间
            if (date.equals(getTodayDate())) {
                result = "今天 " + time;
            } else if (compareDate(date, getTodayDate())) { // 如果是今天以前的日期
                result = getYestoday(date) + " " + time;
            } else {
                result = showDate(date) + " " + time;
            }
        }
        return result;
    }


    /**
     * 格式化时间
     * <p>
     * 显示今天，时间显示为 时分 HH:mm
     *
     * @param dateStr
     * @return
     */
    public static String getFormatShortTime(String dateStr) {
        String time = "";
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        if (dateStr.contains(" ")) {
            String[] arr = dateStr.split(" ");
            time = showTime(arr[1]); // 时间
        }
        return time;
    }

    /**
     * 格式化日期
     * <p>
     * 显示今天，昨天，如3-09等，不要时分秒
     *
     * @param dateStr
     * @return
     */
    public static String getFormatDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        String result = dateStr;
        String date = dateStr;
        if (date.length() > 10) {
            date = date.substring(0, 10);
        }
        if (date.equals(getTodayDate())) {
            result = "今天 ";
        } else if (compareDate(date, getTodayDate())) { // 如果是今天以前的日期
            result = getYestoday(date);
        } else {
            result = showDate(date);
        }
        return result;
    }

    /**
     * 获得当天日期 yyyy-MM-dd
     *
     * @return
     */
    public static String getTodayDate() {
        Date date = new Date(); // 获取当前时间
        String p = "yyyy-MM-dd"; // 自定义时间格式
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        String str = sdf.format(date);// 格式化
        return str;
    }

    /**
     * 时间显示到分
     *
     * @param time
     * @return
     */
    private static String showTime(String time) {
        String result = time;
        if (time.contains(":") && time.length() > 5) {
            result = time.substring(0, 5);
        }
        return result;
    }

    /**
     * 如果是本年则不显示年
     *
     * @param time
     * @return
     */
    private static String showDate(String time) {
        String result = time;
        String yearOfToday = getTodayDate().substring(0, 5);
        if (time.contains(yearOfToday)) {
            result = time.replace(yearOfToday, "");
        }
        return result;
    }


    /**
     * 格式化时间
     * <p>
     * 显示今天，昨天 时间显示到分,如 10月1日 09
     *
     * @param dateStr
     * @return
     */
    public static String getFormatAvatarTime(String dateStr) {
        String result = "";

        String formatStr = "yyyy年M月d日 HH:mm";

        Date sourceDate = formatStrToDateAndTime(dateStr);
        Calendar forCalendar = Calendar.getInstance();
        forCalendar.setTime(sourceDate);
        forCalendar.set(Calendar.HOUR_OF_DAY, 0);
        forCalendar.set(Calendar.MINUTE, 0);
        forCalendar.set(Calendar.SECOND, 0);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
        nowCalendar.set(Calendar.MINUTE, 0);
        nowCalendar.set(Calendar.SECOND, 0);

        if (nowCalendar.get(Calendar.YEAR) == forCalendar.get(Calendar.YEAR)
                && nowCalendar.get(Calendar.MONTH) == forCalendar
                .get(Calendar.MONTH)
                && nowCalendar.get(Calendar.DAY_OF_MONTH) == forCalendar
                .get(Calendar.DAY_OF_MONTH)) {
            formatStr = "今天 HH:mm";
        } else if (nowCalendar.get(Calendar.YEAR) == forCalendar
                .get(Calendar.YEAR)) {
            formatStr = "M月d日 HH:mm";
        }
        result = ViewHelper.formatDateToStr(sourceDate, formatStr);
        return result;
    }


    /**
     * 日期格式化
     *
     * @return
     */
    public static String DateformatTime(Date date) {
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

    /**
     * String型时间戳格式化
     *
     * @return
     */
    public static String LongFormatTime(String time) {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换为日期
        Date date = new Date();
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdfTime.parse(time);
            if (isThisYear(date)) {//今年
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                if (isToday(date)) { //今天
                    return simpleDateFormat.format(date);
                } else {
                    if (isYesToday(time)) {//昨天，显示昨天
                        return "昨天 ";
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                        return sdf.format(date);
                    }
                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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

    private static boolean isThisYear(Date date) {
        Date now = new Date();
        return date.getYear() == now.getYear();
    }



    /**
     * 根据long类型的时间戳，转换为一个String类型的描述性时间
     * 通话记录如果发生在今天：“15：30”
     * 发生在昨天：“昨天8:23”
     * 发生在前天：“前天4:56”
     * 更早：     “2016/04/15”
     *
     * @param timeStample
     * @return
     */
    //timeStample是聊天记录发生的时间
    public static String getTime(long timeStample) {
        //得到现在的时间戳
        long now = System.currentTimeMillis();

        //在java中,int类型的数进行除法运算,只能的整数,正是利用这一点,
        //在下列日期中,只要没过昨天24点,无论相差了1s还是23小时,除法得到的结果都是前一天,
        int day = (int) (now / 1000 / 60 / 60 / 24 - timeStample / 1000 / 60 / 60 / 24);
        System.out.println(day);
        if (day>7) {
//            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy:MM:dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return sdf3.format(timeStample);
        }else {
            switch (day) {
                //如果是0这则说明是今天,显示时间
                case 0:
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    return sdf.format(timeStample);
                //如果是1说明是昨天,显示昨天+时间
                case 1:
                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                    return "昨天" + sdf1.format(timeStample);
                //如果是1说明是前天,显示前天+时间
                case 2:
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                    return "前天" + sdf2.format(timeStample);
                //结果大于2就只显示年月日
                default:
                    SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                    return getWeekOfDate(timeStample)+ sdf3.format(timeStample);
            }
        }
    }
    /***
     * 把datetime转换为long格式
     */
    public long datetimeToLong(String datetime) throws ParseException {
        Date d2;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        d2=sdf.parse(datetime);//将String to Date类型
        System.out.println(d2);
        long t3=d2.getTime();
        System.out.println(t3);
        return t3;
    }
    /***
     * 把datetime格式的转换为特定格式的日期字符串
     *
     */
    public String getdate(String datetime) throws ParseException {
        String s;
        return s=getTime(datetimeToLong(datetime));

    }
    /**
     * 星期几
     *
     * @param time
     * long 系统时间的long类型
     * @return 星期一到星期日
     *
     * */
    public static String getWeekOfDate(long time) {

        Date date = new Date(time);
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
        Log.i("w",date.toString()+w);
//        if (w < 0)
//            w = 0;
        return weekDays[w];
    }



    //是不是昨天
    public static boolean isYesToday(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date(System.currentTimeMillis());
        date2.setHours(0);
        date2.setMinutes(0);
        date2.setSeconds(0);
        int tmp = 86400000;

        long day1 = date.getTime() / tmp;
        long day2 = date2.getTime() / tmp;

        if (day2-day1 == 1) {
            return true;
        } else {
            return false;
        }
    }

}