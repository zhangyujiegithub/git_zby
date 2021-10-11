package com.biaozhunyuan.tianyi.common.utils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * emoji表情处理
 *
 * @author k 2016-07-20
 */
public class EmojiUtils {
    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 遍历检测对象中是的字符串内容中是否有emoji表情
     *
     * @return 包含emoji则返回true
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static <T> boolean containsEmojiStr(T t) {
        Class<? extends Object> cl = t.getClass();
        Field[] fields = cl.getFields();
        for (Field field : fields) {
            System.out.println("" + field.getType());
            Class fieldType = field.getType();
            if (fieldType.equals(String.class)) {
                try {
                    Object obj = field.get(t);
                    if (obj != null) {
                        String content = obj.toString();
                        System.out.println("==" + content);
                        if (containsEmoji(content)) {
                            return true;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    /***
     * @param t         检测对象
     * @param fieldName 要检测的指定字段名
     * @param <T>
     * @return
     */
    public static <T> boolean containsEmojiStr(T t, String fieldName) {
        Class<? extends Object> cl = t.getClass();
        try {
            Field field = cl.getField(fieldName);
            Class fieldType = field.getType();
            if (fieldType.equals(String.class)) {
                try {
                    Object obj = field.get(t);
                    if (obj != null) {
                        String content = obj.toString();
                        System.out.println("==" + content);
                        if (containsEmoji(content)) {
                            return true;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * @param list      检测对象
     * @param fieldName 要检测的指定字段名
     * @param <T>
     * @return
     */
    public static <T> boolean containsEmojiStr(List<T> list, String fieldName) {
        for (T t : list) {
            if (containsEmojiStr(t, fieldName)) {
                return true;
            }
        }
        return false;
    }

}
