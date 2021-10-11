package com.biaozhunyuan.tianyi.attch;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Administrator on 2017/8/30.
 */

public class ImageLoaderWithCookie extends BaseImageDownloader {
    public ImageLoaderWithCookie(Context context) {
        super(context);
    }

    @Override
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        // Super...
        HttpURLConnection connection = super.createConnection(url, extra);
        connection.setRequestProperty("Cookie", extra.toString());//设置cookie
        return connection;
    }
}
