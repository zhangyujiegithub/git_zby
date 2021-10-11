package com.biaozhunyuan.tianyi.utils;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;

import com.biaozhunyuan.tianyi.common.helper.ViewHelper;

/**
 * Created by 王安民 on 2017/3/9.
 */
public class ShapeUtils {


    public static PaintDrawable getRoundedColorDrawable(int color, float radius, int padding) {
        PaintDrawable paintDrawable = new PaintDrawable(color);
        paintDrawable.setCornerRadius(radius);
        paintDrawable.setPadding(padding, padding, padding, padding);
        return paintDrawable;
    }


    public static PaintDrawable getRoundedDrawableByRadius(Context context,int color, float topRadius, float bottomRadius, int padding) {
        PaintDrawable paintDrawable = new PaintDrawable(color);
        paintDrawable.setCornerRadii(new float[]{ViewHelper.dip2px(context,topRadius),ViewHelper.dip2px(context,topRadius), ViewHelper.dip2px(context,topRadius), ViewHelper.dip2px(context,topRadius)
                , ViewHelper.dip2px(context,bottomRadius), ViewHelper.dip2px(context,bottomRadius), ViewHelper.dip2px(context,bottomRadius), ViewHelper.dip2px(context,bottomRadius)});
        paintDrawable.setPadding(padding, padding, padding, padding);
        return paintDrawable;
    }
}
