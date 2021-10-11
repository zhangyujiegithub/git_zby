package com.biaozhunyuan.tianyi.pushService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biaozhunyuan.tianyi.NavActivity;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.buglist.BugInfoActivity;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.ClientRelatedInfoActivity;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.log.LogInfoActivity;
import com.biaozhunyuan.tianyi.notice.NoticeInfoActivity;
import com.biaozhunyuan.tianyi.task.TaskInfoActivityNew;
import com.biaozhunyuan.tianyi.task.TaskListActivityNew;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;

/**
 * Created by wangAnMin on 2019/2/20.
 * 华为点击推送进入对应页面中间页面
 */

public class HuaweiPushActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = HuaweiPushActivity.this;
        if (Global.mUser == null) {
            SharedPreferencesHelper helper = new SharedPreferencesHelper(getApplicationContext());
            Global.mUser = helper.getObjectBuKey("GLOBAL_USER", User.class);
        }
        try {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String uri = intent.toUri(Intent.URI_INTENT_SCHEME);

            String dataId = "";
            String dataType = "";
            String url = "";
            String message = uri.substring(uri.indexOf("message=") + 8, uri.indexOf("#"));
            LogUtils.d("华为推送消息：", message);
            JSONArray array = JSONArray.parseArray(message);
            if (array.size() >= 2) {
                JSONObject ob = (com.alibaba.fastjson.JSONObject) array.get(0);
                JSONObject ob1 = (com.alibaba.fastjson.JSONObject) array.get(1);
                if (array.size() == 3) {
                    JSONObject ob2 = (com.alibaba.fastjson.JSONObject) array.get(2);
                    url = (String) ob2.get("URL");
                }
                dataId = (String) ob.get("dataId");
                dataType = (String) ob1.get("dataType");

                Dynamic dynamic = new Dynamic();
                dynamic.setDataType(dataType);
                dynamic.setDataId(dataId);
                dynamic.setUrl(url);
                intoInfoByPushType(dynamic);
            } else {
                startActivity(new Intent(context, NavActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            startActivity(new Intent(context, NavActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 根据推送数据类型进入详情页面
     */
    private void intoInfoByPushType(Dynamic dynamic) {
        Intent intent = new Intent();
        switch (dynamic.getDataType()) {
            case "流程实例编号":
                intent.setClass(context, FormInfoActivity.class);
                String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                intent.putExtra("exturaUrl", url1);
                break;
            case "日志消息":
                intent.setClass(context, LogInfoActivity.class);
                break;
            case "任务消息":
                intent.setClass(context, TaskInfoActivityNew.class);
                break;
            case "通知消息":
                intent.setClass(context, NoticeInfoActivity.class);
                break;
            case "客户消息":
                intent.setClass(context, ClientRelatedInfoActivity.class);
                break;
            case "项目联系提醒":
            case "客户联系提醒":
                intent.setClass(context, AddRecordActivity.class);
                break;
            case "离线消息":
                intent.setClass(context, NavActivity.class);
//                intent.setClass(context, ChatActivity.class);
//                GroupSession session = new GroupSession();
//                session.setChatId(dynamic.getDataId());
//                intent.putExtra("chatUser", session);
//                intent.putExtra("isPush", true);
                break;
            case "URL":
                intent.setClass(context, WebviewNormalActivity.class);
                intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dynamic.getDataType());
                intent.putExtra(WebviewNormalActivity.EXTRA_URL, dynamic.getUrl());
                break;
            case "空间消息":
                String url = Global.BASE_JAVA_URL + GlobalMethord.空间详情H5 + dynamic.getDataId();
                intent.setClass(context, WebviewNormalActivity.class);
                intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dynamic.getDataType());
                intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
                break;
            case "今日待办任务":
                intent.setClass(context, TaskListActivityNew.class);
                break;
            case "BUG消息":
                intent.setClass(context, BugInfoActivity.class);
                break;
            case "应付提醒" :
                intent.setClass(context, FormInfoActivity.class);
                String url2 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                intent.putExtra("exturaUrl", url2);
                intent.putExtra("formDataId", dynamic.getDataId());
                intent.putExtra("isShowCancelPush", true);
                break;

        }
        intent.putExtra("dynamicInfo", dynamic);
        startActivity(intent);
        finish();
    }
}
