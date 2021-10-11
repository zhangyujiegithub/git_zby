package com.biaozhunyuan.tianyi.view;


import android.view.View;
import android.view.ViewTreeObserver;

/**
 * View限制最大高度
 */
public class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    private int maxHeight = 500;
    private View view;

    public OnViewGlobalLayoutListener(View view, int height) {
        this.view = view;
        this.maxHeight = height;
    }

    @Override
    public void onGlobalLayout() {
        if (view.getHeight() > maxHeight)
            view.getLayoutParams().height = maxHeight;
    }
}
