package com.biaozhunyuan.tianyi.view;
import android.content.Context;
import android.util.AttributeSet;

import com.chy.srlibrary.slistview.SMListView;

/***
 * 自定义ListView子类，继承ListView
 * @author Administrator
 *
 */
public class SMNOScrollListView extends SMListView {

    public SMNOScrollListView(Context context) {
        super(context);
    }

    public SMNOScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SMNOScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}