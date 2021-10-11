package com.biaozhunyuan.tianyi.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;

/**
 * Created by wangpu on 2018/6/6.
 */

public class BoeryunProgressBar {

    private Dialog progressDialog;
    private TextView msg;

    public BoeryunProgressBar(Context context) {
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
    }


    public void show() {
        progressDialog.show();
    }

    public void dissMiss() {
        progressDialog.dismiss();
    }


    public void setMsg(String m1) {
        msg.setText(m1);
    }
}
