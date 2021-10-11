package com.biaozhunyuan.tianyi.common.utils;

import java.util.HashMap;

/**
 * 自定义法定节假日 (需要手动维护)
 */

public class HolidayUtils {


    /**
     * 获取周六日 及法定节假日(需手动维护)
     * @return
     */
    public static HashMap<String, String> getHolidayMap() {
        HashMap<String, String> markData = new HashMap<>();
        //元旦节
        markData.put("2019-1-1", "元旦节");
        markData.put("2020-1-1", "元旦节");
        markData.put("2018-1-1", "元旦节");
        //春节
        markData.put("2019-2-4", "除夕");
        markData.put("2019-2-5", "春节");
        markData.put("2019-2-6", "0");
        markData.put("2019-2-7", "0");
        markData.put("2019-2-8", "0");
        markData.put("2019-2-9", "0");
        markData.put("2019-2-10", "0");
        markData.put("2020-1-24", "除夕");
        markData.put("2020-1-25", "春节");
        markData.put("2018-2-15", "除夕");
        markData.put("2018-2-16", "春节");
        //清明节
        markData.put("2019-4-5", "清明节");
        markData.put("2020-4-4", "清明节");
        markData.put("2018-4-5", "清明节");
        markData.put("2019-4-6", "0");
        markData.put("2019-4-7", "0");
        //劳动节
        markData.put("2019-4-28", "1");
        markData.put("2019-5-5", "1");
        markData.put("2018-5-1", "劳动节");
        markData.put("2019-5-1", "劳动节");
        markData.put("2020-5-1", "劳动节");
        markData.put("2019-5-2", "0");
        markData.put("2019-5-3", "0");
        markData.put("2019-5-4", "0");
        //端午节
        markData.put("2019-6-7", "端午节");
        markData.put("2020-6-25", "端午节");
        markData.put("2018-6-18", "端午节");
        markData.put("2019-6-8", "0");
        markData.put("2019-6-9", "0");
        //中秋节
        markData.put("2019-9-13", "中秋节");
        markData.put("2018-8-24", "中秋节");
        markData.put("2019-9-14", "0");
        markData.put("2019-9-15", "0");
        //国庆节
        markData.put("2019-9-29", "1");
        markData.put("2019-10-12", "1");
        markData.put("2018-10-1", "国庆节");
        markData.put("2019-10-1", "国庆节");
        markData.put("2020-10-1", "国庆节");
        markData.put("2021-10-1", "国庆节");
        markData.put("2019-10-2", "0");
        markData.put("2019-10-3", "0");
        markData.put("2019-10-4", "0");
        markData.put("2019-10-5", "0");
        markData.put("2019-10-6", "0");
        markData.put("2019-10-7", "0");

//        String sunday = ViewHelper.getSunday();
//        for (int i = 0; i < 52;i++){
//            markData.put(ViewHelper.getFetureDateStr(sunday,i * 7 - 1), "2"); //周六
//            markData.put(ViewHelper.getFetureDateStr(sunday,i * 7), "2");     //周日
//        }
//        for (int i = 0; i < 52;i++){
//            markData.put(ViewHelper.getFetureDateStr(sunday,- (i * 7 + 1)), "2"); //上周六
//            markData.put(ViewHelper.getFetureDateStr(sunday,- (i * 7)), "2");     //上周日
//        }
        return markData;
    }

}
