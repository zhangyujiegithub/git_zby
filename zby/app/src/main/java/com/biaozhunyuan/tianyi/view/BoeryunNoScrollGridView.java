package com.biaozhunyuan.tianyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class BoeryunNoScrollGridView extends GridView {
	public BoeryunNoScrollGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BoeryunNoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BoeryunNoScrollGridView(Context context, AttributeSet attrs,
                                   int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
