package com.biaozhunyuan.tianyi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 常用正则校验工具类
 * 
 * @author kjx create 2015/05/04 15:42
 * 
 */
public class RegexUtils {

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean b = false;
		pattern = Pattern.compile("^((1[3,5,7,8][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"); // 验证手机号
		matcher = pattern.matcher(str);
		b = matcher.matches();
		return b;
	}

	/**
	 * 特殊符号校验，只能是包含数字字母汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isSpecialCharactor(String str) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean b = false;
		pattern = Pattern.compile("^[a-zA-Z0-9\u4e00-\u9fa5]+$"); //
		matcher = pattern.matcher(str);
		b = matcher.matches();
		return b;
	}

	/**
	 * 数字，英文字母校验符号校验
	 * 
	 * @param str
	 * @return 由数字和英文组成 则返回true
	 */
	public static boolean isCharactor(String str) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean b = false;
		pattern = Pattern.compile("^[a-zA-Z0-9]+$"); //
		matcher = pattern.matcher(str);
		b = matcher.matches();
		return b;
	}

	/**
	 * 身份证校验,15位或18位，如果是15位，必需全是数字,如果是18位，最后一位可以是数字或字母Xx，其余必需是数字
	 * 
	 * @param str
	 * @return 则返回true
	 */
	public static boolean isIdCard(String str) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean b = false;
		pattern = Pattern
				.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$"); //
		matcher = pattern.matcher(str);
		b = matcher.matches();
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean ischeck(String str) {
		Pattern p1 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("/^[\\u0391-\\uFFE5\\w]+$/"); // 验证带区号
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/***
	 * 校验指定字符两侧是否有中文 例如：目标字符串"...金额..."中匹配“金额”时，判断“金额”左右两侧是否有汉字
	 * 
	 * @param targetStr
	 *            待匹配的字符串
	 * @param regStr
	 *            指定字符
	 * @return 如果两侧任意一侧包含中文 返回false
	 */
	public static boolean isTrimChinese(String targetStr, String regStr) {
		Pattern p1 = null, p2 = null;
		Matcher m1 = null;
		Matcher m2 = null;
		boolean b1 = false;
		boolean b2 = false;
		String regStrLeft = ".*[\u4e00-\u9fa5]+" + regStr + ".*";
		String regStrRight = ".*" + regStr + "[\u4e00-\u9fa5]+.*";
		p1 = Pattern.compile(regStrLeft);
		p2 = Pattern.compile(regStrRight);
		m1 = p1.matcher(targetStr);
		m2 = p2.matcher(targetStr);
		b1 = m1.matches();
		b2 = m2.matches();
		System.err.println(b1 + "--" + b2);
		if (b1 || b2) {
			return false;
		}
		return true;
	}

	/**
	 * 正则校验
	 * 
	 * @param str
	 *            待校验字符串
	 * @param pattern
	 *            正则校验公式
	 * @return 验证通过返回true
	 */
	public static boolean regex(String str, String pattern) {
		Pattern p1 = Pattern.compile(pattern); // 验证
		Matcher m = p1.matcher(str);
		return m.matches();
	}
}
