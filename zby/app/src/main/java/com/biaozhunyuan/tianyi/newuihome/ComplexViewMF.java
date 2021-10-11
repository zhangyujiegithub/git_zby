package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.gongwen.marqueen.MarqueeFactory;

/**
 * 资讯跑马灯
 *
 */

public class ComplexViewMF extends MarqueeFactory<RelativeLayout, HotNews> {
    private LayoutInflater inflater;

    public ComplexViewMF(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RelativeLayout generateMarqueeItemView(HotNews data) {
        RelativeLayout mView = (RelativeLayout) inflater.inflate(R.layout.item_notice_marqueeview, null);
        ((TextView) mView.findViewById(R.id.tv_title)).setText(data.getTitle());
        return mView;
    }
}