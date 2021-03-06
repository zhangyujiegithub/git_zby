package com.loonggg.weekcalendar.entity;

public class CalendarData {

    public int year;
    public int month;
    public int day;
    public int week = -1;
    public boolean isNextMonthDay = false;//是否是下个月中的日期
    public boolean isLastMonthDay = false;//是否是上个月中的日期

    public CalendarData() {
    }

    public CalendarData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public boolean isSameDay(CalendarData data) {
        if(data == null){
            return false;
        } else {
            return (data.day == day && data.month == month && data.year == year);
        }
    }

    @Override
    public String toString() {
        return "CalendarData{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", week=" + week +
                ", isNextMonthDay=" + isNextMonthDay +
                ", isLastMonthDay=" + isLastMonthDay +
                '}';
    }

    /***
     * 获取年月日 yyyy-MM-dd格式的日期
     * @return
     */
    public String getDateStr() {
        return year + "-" + String.format("%02d", month) +"-" + String.format("%02d", day);
    }
}
