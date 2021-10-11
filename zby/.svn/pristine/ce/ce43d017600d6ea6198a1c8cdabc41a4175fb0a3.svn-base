package com.biaozhunyuan.tianyi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by K on 2016/1/26.
 */
public class Indicator extends LinearLayout {

    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0XFF8c8c8d;
    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFF0099ff;
    private ViewPager mViewPager;
    private int mColor;
    private List<String> mTitles;
    private Paint mPaint;
    private int mMarginTop; // 距离顶部距离
    private int mMarginLeft;// 左侧距离
    private int mWidth; // 滑动下标的宽度
    private int mHeight = 5;// 滑动下标的高度,默认10px
    private int mChildCount = 4; // Tab个数
    private int currentPosition = 0;//indicator当前所在的位置
    private OnSelectListener mOnSelectListener;

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Indicator);
        mColor = typedArray.getColor(R.styleable.Indicator_color_indicator,
                getResources().getColor(R.color.hanyaRed));
        mHeight = (int) typedArray.getDimension(R.styleable.Indicator_height_indicator_tab, 5);

        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(mColor);
        // 设置画笔抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMarginTop = getMeasuredHeight(); // 默认Tab高度
        int width = getMeasuredWidth();// 计算控件的宽度
        int height = mMarginTop + mHeight;
        mWidth = width / mChildCount;

        // 重新计算控件宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect(mMarginLeft, mMarginTop, mMarginLeft + mWidth,
                mMarginTop + mHeight);
        canvas.drawRect(rect, mPaint);
        super.onDraw(canvas);
    }

    /***
     * @param pos
     * @param offset
     */
    public void scroll(int pos, float offset) {
        mMarginLeft = (int) ((pos + offset) * mWidth);
        Log.i("scroll", mMarginLeft + "-" + mMarginTop + "-"
                + (mMarginLeft + mWidth) + "-" + (mMarginTop + mHeight));
        invalidate();// 强制要求重绘
    }

    /**
     * 翻页
     */
    public void onPageChanged(int pos) {
        resetTextViewColor();
        highLightTextView(pos);
    }

    /***
     * 设置相关联的ViewPager
     *
     * @param mViewPager
     */
    public void setRelateViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                onPageChanged(pos);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTitles = datas;
            this.mChildCount = datas.size();

            for (int i = 0; i < mTitles.size(); i++) {
                final int pos = i;
                TextView tv = generateTextView(mTitles.get(i));
                // 点击tab滑动viewpage
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPageChanged(pos);
                        currentPosition = pos;
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(pos);
                        } else {
                            //如果没有关联viewpager,点击重绘
                            scroll(pos, 0);
                        }

                        if (mOnSelectListener != null) {
                            mOnSelectListener.onSelect(pos);
                        }
                    }
                });
                // 添加view
                addView(tv);
            }

            onPageChanged(getCurrentPosition());
            invalidate();

            // 设置item的click事件
            // setItemClickEvent();
        }
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(String[] datas) {
        setTabItemTitles(Arrays.asList(datas));
    }

    /**
     * 设置tab的标题内容 可选
     *
     * @param datas     泛型类型的集合数据源
     * @param fieldName 泛型中作为显示名称的字段名称
     */
    public <T> void show(List<T> datas, String fieldName) {
        if (datas == null) {
            return;
        }
        List<String> list = new ArrayList<String>();
        for (T item : datas) {
            Class cs = item.getClass();
            try {
                Field field = cs.getField(fieldName);
                String value = (String) field.get(item);
                Logger.i("field" + "--" + value);
                list.add(value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        setTabItemTitles(list);
    }

    public void updateTabTitleAtPos(int pos, String title) {
        TextView tv = (TextView) getChildAt(pos);
        if (tv != null) {
            tv.setText(StrUtils.pareseNull(title));
        }
    }

    /**
     * 根据标题生成我们的TextView
     *
     * @param text
     * @return
     */
    private TextView generateTextView(String text) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mChildCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setTypeface(Typeface.DEFAULT_BOLD);  //加粗
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 高亮文本
     *
     * @param position
     */
    protected void highLightTextView(int position) {
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(mColor);
        }
    }

    /**
     * 更新顶部标题文字
     */
    public void upldateTitles(String[] datas) {
        upldateTitles(Arrays.asList(datas));
    }

    /**
     * 更新顶部标题文字
     */
    public void upldateTitles(List<String> datas) {
        if (datas != null && datas.size() == getChildCount()) {
            for (int i = 0; i < datas.size(); i++) {
                View view = getChildAt(i);
                if (view instanceof TextView) {
                    ((TextView) view).setText(datas.get(i));
                }
            }
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 获取indicator当前所在的位置
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(int pos);
    }
}
