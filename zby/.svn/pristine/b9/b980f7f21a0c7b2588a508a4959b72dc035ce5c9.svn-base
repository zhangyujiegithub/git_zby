package com.biaozhunyuan.tianyi.common.utils;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * 防止重复点击
 */
public class ClickUtil {
    private final View view;
    private long time = 3000;

    public ClickUtil(View view) {
        this.view = view;
    }

    public static ClickUtil clicks(@NonNull View view) {
        checkNotNull(view, "view == null");
        return new ClickUtil(view);
    }

    public ClickUtil throttleFirst(long time) {
        this.time = time;
        return this;
    }

    private long endTime = 0;

    private synchronized boolean isThrottle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - endTime >= time) {
            endTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    public void setOnClickListener(@NonNull final View.OnClickListener listener) {
        checkNotNull(listener, "listener == null");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isThrottle()) {
                    listener.onClick(v);
                }
            }
        });
    }

    private static void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }
}
