package com.biaozhunyuan.tianyi.common.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;


/**
 * 显示进度条对话框
 */
public class ProgressDialogHelper {
    private static boolean isShow;
    private static Dialog dialog;

    /**
     * 显示进度对话框,请稍后(进度框点击返回键是否消失，默认false)
     */
    public static Dialog show(Context context) {
        return show(context, "加载中...");
    }

    /**
     * 显示进度对话框,请稍后(进度框点击返回键是否消失，默认false)
     */
    public static Dialog show(Context context, boolean isCancelable) {
        return show(context, "加载中...", isCancelable);
    }

    /**
     * 显示进度对话框
     *
     * @param context
     * @param content 提示文字内容，建议不多于8字
     * @return
     */
    public static Dialog show(Context context, String content) {
        return show(context, content, true);
    }

    /**
     * 显示进度对话框
     *
     * @param context
     * @param content      提示文字内容，建议不多于8字
     * @param isCancelable 进度框点击返回键是否消失，默认false
     * @return
     */
    public static Dialog show(Context context, String content,
                              boolean isCancelable) {
        if (isShow && dialog != null) {
            return dialog;
        }
        dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setContentView(R.layout.dialog);
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setCancelable(isCancelable);
        msg.setText(content + "");
        if (TextUtils.isEmpty(content)) {
            msg.setVisibility(View.GONE);
        }
        dialog.show();
        isShow = true;
        dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isShow = false;
            }
        });
        return dialog;
    }

    /**
     * 隐藏进度对话框
     */
    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}