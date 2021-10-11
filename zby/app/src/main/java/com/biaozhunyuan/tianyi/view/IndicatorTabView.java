package com.biaozhunyuan.tianyi.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by K on 2016/11/11.
 */
public class IndicatorTabView extends LinearLayout {

    private Context mContext;
    private ViewPager mViewPager;

    private int currentPos = -1;

    private List<String> mTitles;

    private Paint mPaint;
    private int mWidth; // 滑动下标的宽度
    private int mChildCount = 4; // Tab个数
    private onPageSelectListener pageLisener;

    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0XFF8c8c8d;

    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xffa1cf03;

    public IndicatorTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.Indicator);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.hanyaRed));
        // 设置画笔抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight(); // 默认Tab高度
        int width = getMeasuredWidth();// 计算控件的宽度
        mWidth = width / mChildCount;

        // 重新计算控件宽高
        setMeasuredDimension(width, height);
    }

    /***
     * @param pos
     * @param offset
     */
    public void scroll(int pos, float offset) {
//		mMarginLeft = (int) ((pos + offset) * mWidth);
//		Log.i("scroll", mMarginLeft + "-" + mMarginTop + "-"
//				+ (mMarginLeft + mWidth) + "-" + (mMarginTop + mHeight));
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
    public void setTabItemTitles(String[] datas) {
        setTabItemTitles(Arrays.asList(datas));
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
                        if (pageLisener != null) {
                            pageLisener.onPageSelect(pos);
                        }
                        if (mViewPager != null) {
                            mViewPager.setCurrentItem(pos);
                        }
                        currentPos = pos;
                    }
                });
                // 添加view
                addView(tv);
            }
            onPageChanged(0);

        }

    }

    public int getCurrentPos() {
        if (currentPos != -1) {
            return currentPos;
        }
        return 0;
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
                0, LayoutParams.MATCH_PARENT, 1);
        if (mChildCount < 3) {
            int marginLeft = (int) (getScreenWidth() / mChildCount * 0.2);
            lp.leftMargin = marginLeft;
            lp.rightMargin = marginLeft;
        }
        tv.setPadding(10, 0, 10, 0);
        lp.width = getScreenWidth() / mChildCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
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
            TextView textView = (TextView) view;
            textView.setTextColor(getResources().getColor(R.color.hanyaRed));

//			textView.setTextColor(COLOR_TEXT_NORMAL);
            textView.setBackgroundResource(R.drawable.bg_log_circle);
        }
    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTextColor(COLOR_TEXT_NORMAL);
                textView.setBackground(null);
            }
        }
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


    public interface onPageSelectListener {
        void onPageSelect(int pos);
    }

    public void setOnPageSelectListener(onPageSelectListener listener) {
        this.pageLisener = listener;
    }
}
