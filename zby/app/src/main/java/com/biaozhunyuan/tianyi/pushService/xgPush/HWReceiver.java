package com.biaozhunyuan.tianyi.pushService.xgPush;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.huawei.hms.support.api.push.PushReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/5/22.
 * 华为推送接收
 */

public class HWReceiver extends PushReceiver {
    @Override
    public void onEvent(Context context, Event arg1, Bundle arg2) {
        super.onEvent(context, arg1, arg2);

        showToast("onEvent" + arg1 + " Bundle " + arg2, context);
//        String dataId = "";
//        String dataType = "";
//        String url = "";
//        if ("NOTIFICATION_OPENED".equals(arg1.name())) { //点击推送
//            Intent intent = new Intent(context, NavActivity.class);
//            JSONArray array = (JSONArray) JSONArray.parse(arg2.getString("pushMsg")); //
//            if (array.size() == 3) {
//                com.alibaba.fastjson.JSONObject ob = (com.alibaba.fastjson.JSONObject) array.get(0);
//                com.alibaba.fastjson.JSONObject ob1 = (com.alibaba.fastjson.JSONObject) array.get(1);
//                com.alibaba.fastjson.JSONObject ob2 = (com.alibaba.fastjson.JSONObject) array.get(2);
//                dataId = (String) ob.get("dataId");
//                dataType = (String) ob1.get("dataType");
//                url = (String) ob2.get("URL");
//            }
//            intent.putExtra("pushDataType", dataType);
//            intent.putExtra("pushDataId", dataId);
//            intent.putExtra("URL", url);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
    }

    @Override
    public boolean onPushMsg(Context context, byte[] arg1, Bundle arg2) {

        showToast("onPushMsg" + new String(arg1) + " Bundle " + arg2, context);
        return super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushMsg(Context context, byte[] arg1, String arg2) {

        showToast("onPushMsg" + new String(arg1) + " arg2 " + arg2, context);
        super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushState(Context context, boolean arg1) {

        showToast("onPushState" + arg1, context);
        super.onPushState(context, arg1);
    }

    @Override
    public void onToken(final Context context, final String arg1, Bundle arg2) {
        super.onToken(context, arg1, arg2);

        LogUtils.i("华为推送注册成功，token：", arg1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                bindToken(arg1);
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onToken(Context context, String arg1) {
        super.onToken(context, arg1);
        showToast(" onToken" + arg1, context);
    }

    public void showToast(final String toast, final Context context) {
        Logger.d(toast);

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }).start();
    }

    private void writeToFile(String conrent) {
        String SDPATH = Environment.getExternalStorageDirectory() + "/huawei.txt";
        try {
            FileWriter fileWriter = new FileWriter(SDPATH, true);

            fileWriter.write(conrent + "\r\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 绑定设备token
     */
    private void bindToken(String token) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.绑定设备;

        JSONObject jo = new JSONObject();
        try {
            jo.put("token", token);
            jo.put("allowPush", "1");
            jo.put("deviceType", "android");
            jo.put("mobileType", "huawei");
            jo.put("otherToken", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

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
