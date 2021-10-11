package com.biaozhunyuan.tianyi.utils;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UrlUtils {

    public static String formatEncode(String originUrl) {
        if (TextUtils.isEmpty(originUrl)) {
            return "";
        }
        try {
            URL url = new URL(originUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toURL().toString();
        } catch (MalformedURLException e) {
        } catch (URISyntaxException e) {
        }
        return originUrl;
    }
}
