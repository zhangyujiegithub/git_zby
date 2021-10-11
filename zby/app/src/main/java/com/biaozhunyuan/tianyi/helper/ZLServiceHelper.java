package com.biaozhunyuan.tianyi.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/8/10.
 * <p>
 * 帮助类，封装了各种方法
 */

public class ZLServiceHelper {

    List<Attach> attachs = new ArrayList<Attach>();

    /**
     * 获得一组附件
     *
     * @param context      上下文
     * @param attachmentNo 附件序列号 12,13,14这种
     * @return 附件地址
     */
    public List<Attach> getAttachmentAddr(Context context, String attachmentNo, Handler handler) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("attachIds", attachmentNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.附件列表;
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(response);
                List<Attach> attaches1 = JsonUtils.ConvertJsonToList(response, Attach.class);
                if (attaches1 != null) {
                    attachs = attaches1;
                    if (handler != null) {
                        Message msg = handler.obtainMessage();
                        msg.obj = attaches1;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } else {
                    if (handler != null) {
                        handler.sendEmptyMessage(2);
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                if (handler != null) {
                    handler.sendEmptyMessage(2);
                }
            }

            @Override
            public void onResponseCodeErro(String result) {
                if (handler != null) {
                    handler.sendEmptyMessage(2);
                }
            }
        });
        return attachs;
    }


}
