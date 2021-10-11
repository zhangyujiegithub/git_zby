package com.biaozhunyuan.tianyi.common.utils;

import com.biaozhunyuan.tianyi.common.helper.PreferceManager;

/**
 * Created by 王安民 on 2017/8/30.
 * cookie帮助类
 */

public class CookieUtils {

    public static final String JSESSIONID = "JSESSIONID";
    public static final String rememberMe = "rememberMe";

    /**
     * 根据cookie的集合返回cookie的请求头
     *
     * @param url
     * @return
     */
    public static String cookieHeader(String url) {
//        List<Cookie> cookies = CookieUtil.getInstance().loadForRequest(HttpUrl.get(URI.create(url)));
//        StringBuilder cookieHeader = new StringBuilder();
//        for (int i = 0, size = cookies.size(); i < size; i++) {
//            if (i > 0) {
//                cookieHeader.append("; ");
//            }
//            Cookie cookie = cookies.get(i);
//            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
//        }

        String cookie = rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(rememberMe) + "; " +
                "JSESSIONID=" + PreferceManager.getInsance().getValueBYkey(JSESSIONID);
        return cookie;
    }
}
