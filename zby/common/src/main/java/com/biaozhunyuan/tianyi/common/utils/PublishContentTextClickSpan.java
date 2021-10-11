package com.biaozhunyuan.tianyi.common.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;

/**
 * 生成一个需要整体删除的Span 设置点击事件
 */

public class PublishContentTextClickSpan extends ClickableSpan {

    private Context mContext;
    private String showText;
    private Object tag;
    private EditText et;
    private ClickSpanListener clickSpanListener;

    public PublishContentTextClickSpan(Context context, String showText, EditText et) {
        this.mContext = context;
        this.showText = showText;
        this.et = et;
    }

    public String getShowText() {
        return showText;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public void onClick(View widget) {
        et.setSelection(et.getText().toString().length());
        if (clickSpanListener != null) {
            clickSpanListener.clickSpan();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //去除连接下划线
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

    public interface ClickSpanListener {
        void clickSpan();
    }

    public void setOnClickSpanListener(ClickSpanListener listener) {
        clickSpanListener = listener;
    }
}
