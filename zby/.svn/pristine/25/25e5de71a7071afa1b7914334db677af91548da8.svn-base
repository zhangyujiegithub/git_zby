package com.biaozhunyuan.tianyi.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;


public class MyProgressBar extends LinearLayout {
    private Context context;
    private TextView textView;
    private ImageView iv_loading;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading, this, true);
        iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
        iv_loading.setBackgroundResource(R.drawable.frame_loading);
        AnimationDrawable anim = (AnimationDrawable) iv_loading.getBackground();
        anim.start();
    }

    public void setInfo(CharSequence tilte) {
        textView.setText(tilte);
    }

}
