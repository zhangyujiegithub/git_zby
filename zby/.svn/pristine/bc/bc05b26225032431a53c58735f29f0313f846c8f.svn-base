package com.biaozhunyuan.tianyi.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by new on 2016/12/28.
 */
@SuppressLint("AppCompatCustomView")
public class LastInputEditText extends EditText {

    public LastInputEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastInputEditText(Context context) {
        super(context);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //光标首次获取焦点是在最后面，之后操作就是按照点击的位置移动光标
        if (isEnabled() && hasFocus() && hasFocusable()) {
            setSelection(selEnd);
        } else {
            setSelection(getText().length());
        }
//
//        //保证光标始终在最后面
//        if(selStart==selEnd){ //防止不能多选
//            setSelection(getText().length());
//        }
    }
}