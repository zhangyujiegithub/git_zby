package com.biaozhunyuan.tianyi.common.utils;

/***
 * 金额大小写转换工具类
 *
 * @author K
 *
 */
public class MoneyUtils {

    private static final String UNIT = "万仟佰拾亿仟佰拾万仟佰拾元角分";
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
    private static final double MAX_VALUE = 9999999999999.99D;


    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。没有分割显示
     *
     * @param value
     * @param isTwoSmall 是否保留两位小数
     * @return Sting
     */
    public static String formatFloatNumberNoSplit(double value, boolean isTwoSmall) {
        if (value != 0.00) {
            String pattern = isTwoSmall ? "0.##" : "###";
            java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
            return df.format(value);
        } else {
            if (isTwoSmall)
                return "0.00";
            else
                return "0";
        }

    }

    /***
     * 转为大写金额
     *
     * @param v
     * @return
     */
    public static String change(double v) {
        if (v < 0 || v > MAX_VALUE) {
            return "参数非法!";
        }
        long l = Math.round(v * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万'
                        || UNIT.charAt(j) == '元') {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

}