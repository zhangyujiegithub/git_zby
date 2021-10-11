package com.biaozhunyuan.tianyi.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    public static final String EMPTY = "";

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "null".equalsIgnoreCase(str);
    }

    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.replaceAll(" ", "").equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 去除标点符号
     */
    public static String deleteSign(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.trim();
            if (str.contains(";")) {
                str = str.replaceAll(";", "");
            }
            if (str.contains("'")) {
                str = str.replaceAll("'", "");
            }
            if (str.contains(",")) {
                str = str.replaceAll(",", "");
            }
        }
        return str;
    }

    /**
     * 移除开头结尾的[]
     */
    public static String removeRex(String json) {
        if (!TextUtils.isEmpty(json) && json.startsWith("[")
                && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }
        return json;
    }

    /**
     * 如果字符串为空，则转为""的形式,避免出现null
     */
    public static String pareseNull(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if ("null".equals(str)){
            return "";
        }
        return str;
    }

    /**
     * 去掉字符串中所有的空格
     */
    public static String removeSpace(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replaceAll(" ", "");
        }
        return str;
    }

    /**
     * 移除开头结尾的指定字符
     *
     * @param json                  目标字符串
     * @param removeStartAndEndChar 要移除开头结尾的指定字符
     * @return
     */
    public static String removeRex(String json, String removeStartAndEndChar) {
        if (!TextUtils.isEmpty(json)) {
            if (json.endsWith(removeStartAndEndChar)) {
                json = json.substring(0, json.length() - 1);
            }

            if (json.startsWith(removeStartAndEndChar)) {
                json = json.substring(1, json.length());
            }
        }
        return json;
    }


    /**
     * 将字符串按照符号分割
     *
     * @param str
     * @return
     */
    public static String[] splitFuhao(String str) {
        return str.split("[+]|[-]|[*]|[/]|[(]|[)]|[:]|[,]|[']|[|]");//分割出符号
    }

    /**
     * 关键字高亮显示
     *
     * @param context 上下文
     * @param text    需要显示的文字
     * @param target  需要高亮的关键字
     * @param color   高亮颜色
     * @param start   头部增加高亮文字个数
     * @param end     尾部增加高亮文字个数
     * @return 处理完后的结果
     */
    public static SpannableString highlight(Context context, String text, String target,
                                            String color, int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    /**
     * 判断一个字符串包含多少其他字符串
     *
     * @param full 字符串
     * @param unit 判断的字符
     * @return
     */
    public static int getStrCountInString(String full, String unit) {
        int result = 0;
        int fullLength = full.length();
        int afterLength = full.replace(unit, "").length();
        result = (int) ((fullLength - afterLength) / unit.length());
        return result;
    }

    /**
     * 使用正则表达式提取中括号中的内容
     * @param msg
     * @return
     */
    public static List<String> extractMessageByRegular(String msg){

        List<String> list=new ArrayList<String>();
        Pattern p = Pattern.compile("\\{([^}]*)\\}");
        Matcher m = p.matcher(msg);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length()-1));
        }
        return list;
    }

}
