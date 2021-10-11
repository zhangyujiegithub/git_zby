package com.biaozhunyuan.tianyi.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 王安民 on 2017/10/13.
 * 解码工具类
 */

public class DecodeUtils {



    /**
     * unicode转中文
     *
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch + "");

        }

        return str;

    }

}
