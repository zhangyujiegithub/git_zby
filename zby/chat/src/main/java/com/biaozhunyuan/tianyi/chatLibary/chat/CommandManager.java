package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.CommandModel;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.utils.EmojiUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/11/29.
 * 聊天命令行管理类
 */

public class CommandManager {


    /**
     * 获取命令帮助类的集合
     *
     * @return
     */
    public static List<CommandModel> getCommandList() {
        List<CommandModel> list = new ArrayList<>();

        CommandModel rwCommand = new CommandModel();
        rwCommand.setName("新建任务");
        rwCommand.setShortCut("/rw");
        list.add(rwCommand);

        CommandModel rzCommand = new CommandModel();
        rzCommand.setName("新建日志");
        rzCommand.setShortCut("/rz");
        list.add(rzCommand);

        return list;
    }


    /**
     * 新建一条任务
     *
     * @param executor    执行人uuid
     * @param participant 参与人uuid，可以是多个uuid用逗号拼起来
     * @param content     内容
     * @param context     上下文
     */
    public static void newTask(String executor, String participant, String content, final Context context) {
        Task task = new Task();
        task.setExecutorIds(executor);
        task.setParticipantIds(participant);
        task.setContent(content);
        task.setBeginTime(ViewHelper.getDateToday() + " 00:00:00");
        task.setEndTime(ViewHelper.getDateToday() + " 23:59:59");
        task.setCreatorId(Global.mUser.getUuid());

        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;

        StringRequest.postAsyn(url, task, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "添加任务成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(context, "添加任务失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, "添加任务失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 获取当天的日志并显示到当前页面
     * @param content     日志内容
     * @param context     上下文
     */
    public static void saveLog(final String content, final Context context) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.当天日志 + "?logType=普通日志";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i("当天日志：" + response);
                WorkRecord workRecord = JsonUtils.ConvertJsonObject(response, WorkRecord.class);
                String logContent = "";
                if (workRecord != null) {
                    logContent = workRecord.getContent()+ "," + content;
                    workRecord.setContent(logContent);
                    saveLog(logContent,workRecord,context);
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                String logContent =  content;
                saveLog(logContent,null,context);
            }
        });
    }

    /**
     * 保存日志
     */
    private static void saveLog(String content, WorkRecord mRecord, final Context context) {
        ProgressDialogHelper.dismiss();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(context, "内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.contains("%")) {
            Toast.makeText(context, "非法字符:%!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EmojiUtils.containsEmoji(content)) {
            Toast.makeText(context, "不支持表情符号!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("log", content);
            jo.put("time", ViewHelper.getCurrentFullTime());
            jo.put("logType", "普通日志");
            if (mRecord != null) {
                jo.put("uuid", mRecord.getUuid());
                jo.put("attachmentIds", mRecord.getAttachmentIds());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.日志保存, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i("保存日志：" + response);
                String status = JsonUtils.parseStatus(response);
                if (status.equals("1")) {
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorMessage = JsonUtils.getStringValue(response, JsonUtils.KEY_MESSAGE);
                        Toast.makeText(context, "保存失败:" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String response) {
                Toast.makeText(context, JsonUtils.pareseData(response), Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });

    }
}
