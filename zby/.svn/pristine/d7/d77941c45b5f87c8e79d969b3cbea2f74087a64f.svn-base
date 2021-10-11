package com.biaozhunyuan.tianyi.wps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.attach.WpsModel.Reciver;

import java.io.File;

import okhttp3.Request;


/**
 * Created by wangAnMin on 2018/3/14.
 */

public class WPSBroadCastReciver extends BroadcastReceiver {

    public static String fieldName = "";
    public static String formID = "";
    private Context context;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String attachId = (String) msg.obj;
                    updateAttachment(formID, attachId);
                    break;
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        switch (intent.getAction()) {
            case Reciver.ACTION_BACK://返回键广播
                System.out.println(Reciver.ACTION_BACK);
                break;
            case Reciver.ACTION_CLOSE://关闭文件时候的广播
                System.out.println(Reciver.ACTION_CLOSE);

                break;
            case Reciver.ACTION_HOME://home键广播
                System.out.println(Reciver.ACTION_HOME);

                break;
            case Reciver.ACTION_SAVE://保存广播
                System.out.println(Reciver.ACTION_SAVE);
                Toast.makeText(context, "保存了文档", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(fieldName)) {
                    uploadPhoto(fieldName); //收文单保存文档后,重新上传文件
                }
                break;
            default:
                break;
        }

    }

    /**
     * 上传考勤图片 到服务器
     *
     * @param path
     * @return
     */
    private void uploadPhoto(final String path) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(path);
                String attachId = UploadHelper.uploadFileGetAttachId("attendance", file);
                Message msg = new Message();
                msg.obj = attachId;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }


    /**
     * 更新发文单的 附件
     *
     * @param uuid          发文单的UUID
     * @param attachmentIds 附件的ID
     */
    private void updateAttachment(String uuid, String attachmentIds) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.上传发文附件 + "?uuid=" + uuid + "&attachmentIds=" + attachmentIds;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, JsonUtils.pareseData(response), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

}