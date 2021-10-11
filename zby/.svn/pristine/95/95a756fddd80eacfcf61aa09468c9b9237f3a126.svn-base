package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.Command;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.RecentMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.UnreadMessage;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/12/4.
 */

public class MsgCacheManager {

    private static SharedPreferencesHelper preferencesHelper;


    /**
     * 以chartId为key，保存消息
     *
     * @param context 上下文
     * @param msg     消息实体
     */
    public static void saveMessage(Context context, ChatMessage msg) {
        saveMessage(context, msg, true);
    }

    /**
     * 以chartId为key，保存消息
     *
     * @param context    上下文
     * @param msg        消息实体
     * @param isVibrator 是否震动
     */
    public static void saveMessage(Context context, ChatMessage msg, boolean isVibrator) {
        if (Global.mUser == null || (Global.mUser != null && TextUtils.isEmpty(Global.mUser.getUuid()))) {
            SharedPreferencesHelper helper = new SharedPreferencesHelper(context);
            Global.mUser = helper.getObjectBuKey("GLOBAL_USER", User.class);
            LogUtils.e("GLOBAL_USER", Global.mUser.toString());
        }
        preferencesHelper = new SharedPreferencesHelper(context);
        if (!TextUtils.isEmpty(msg.getFromUuid()) && msg.getFromUuid().contains(Global.mUser.getUuid())) { //如果fromid是自己，将对话放置右侧
            if (ChatMessage.FORMAT_VOICE.equals(msg.getFormat())) {
                msg.setChatCategory(ChatMessage.CHAT_RIGHT_AUDIO);
            } else if (ChatMessage.FORMAT_FILE.equals(msg.getFormat())) {
                msg.setChatCategory(ChatMessage.CHAT_RIGHT_FILE);
            } else {
                msg.setChatCategory(ChatMessage.CHAT_RIGHT);
            }
        }
        if (ChatMessage.FORMAT_TIP.equals(msg.getFormat())) {
            msg.setChatCategory(ChatMessage.CHAT_TIP);
        }

        //以对话的chatid为key，保存聊天记录
        Map<String, RecentMessage> listMap = preferencesHelper.getHashMapData(Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
        RecentMessage messages2 = listMap.get(msg.getChatId());  //获取该会话id的记录
        if (messages2 != null && messages2.getRecentMessages() != null) { //如果有，插入数据
            messages2.getRecentMessages().add(msg);
        } else { //如果没有，插入新的数据
            messages2 = new RecentMessage();
            List<ChatMessage> list = new ArrayList<>();
            list.add(msg);
            messages2.setRecentMessages(list);
        }
        listMap.put(msg.getChatId(), messages2);
        preferencesHelper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", listMap); //保存消息到本地

        //如果不是离线消息设置震动
        if (!TextUtils.isEmpty(msg.getFrom())
                && !msg.getFrom().contains(Global.mUser.getUuid())
                && isVibrator
                && !ChatMessage.FORMAT_TIP.equals(msg.getFormat())) {
            //如果不是自己发送的消息，并且不是提示类消息,设置震动
            Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(200);
        }
    }


    public static void getUnreadMessage(Context context) {
        getUnreadMessage(context, null);
    }

    /**
     * 获取离线消息
     */
    public static void getUnreadMessage(final Context context, final Handler handler) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.未读消息 + "?pcOrmobile=mobile";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(final String response) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<UnreadMessage> messages = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), UnreadMessage.class);
                        if (messages != null && messages.size() > 0) {
                            for (UnreadMessage msg : messages) {
                                if (msg.getContents() != null) {
                                    for (int i = 0; i < msg.getContents().size(); i++) {
                                        UnreadMessage.UnreadMessageContent content = msg.getContents().get(i);
                                        ChatMessage message = new ChatMessage();
                                        message.setChatId(content.getGroupId());
                                        message.setKeyValues(content.getKeyValue());
                                        if (message.getKeyValues() != null) {
                                            message.getKeyValues().put("isDepartment", msg.getIsDepartment() + "");
                                        }
                                        if (content.getReplyMsg() != null
                                                && !TextUtils.isEmpty(content.getReplyMsg().getFrom())) {
                                            message.setReplyMsg(content.getReplyMsg());
                                        }
                                        message.setCreateTime(content.getCreateTime());
                                        message.setFrom(content.getFromId() + "@" + Global.mUser.getCorpId() + "/");
                                        message.setBody(content.getBody());
                                        message.setFormat(content.getFormat());
                                        message.setAvatar(msg.getIcon());
                                        message.setCmd(Command.chat);
                                        message.setName(msg.getName());
                                        if ("1".equals(msg.getIsSingle())) {
                                            message.setAvatar(content.getFromId());
                                        }
                                        if (msg.getUpdateTime() != null && msg.getUpdateTime().size() == 1) {
                                            UnreadMessage.UpdateTime updateTime = msg.getUpdateTime().get(0);
                                            if (!TextUtils.isEmpty(updateTime.getPcLastUpdateTime())) {
                                                message.setPcLastUpdateTime(updateTime.getPcLastUpdateTime());
                                            }
                                            if (!TextUtils.isEmpty(updateTime.getMobileLastUpdateTime())) {
                                                message.setMobileLastUpdateTime(updateTime.getMobileLastUpdateTime());
                                            }
                                        }
                                        if (ChatMessage.FORMAT_VOICE.equals(message.getFormat())) {
                                            message.setChatCategory(ChatMessage.CHAT_LEFT_AUDIO);
                                        } else if (ChatMessage.FORMAT_FILE.equals(message.getFormat())) {
                                            message.setChatCategory(ChatMessage.CHAT_LEFT_FILE);
                                        } else {
                                            message.setChatCategory(ChatMessage.CHAT_Left);
                                        }
                                        if (ChatMessage.FORMAT_TIP.equals(message.getFormat())) {
                                            message.setChatCategory(ChatMessage.CHAT_TIP);
                                        }
                                        message.setSendTime(content.getCreateTime());
                                        //保存到消息列表
                                        saveMessage(context, message, false);
                                        if (i == msg.getContents().size() - 1) {//每个对话最后一条消息
                                            setMessageRead(content.getUuid(), msg.getUuid()); //设置消息已读
                                        }
                                        SessionCacheManger.saveSession(context, "", message);
                                    }
                                }
                            }
                        }
                        //发送获取未读消息成功通知
                        EventBus.getDefault().postSticky("505");

                        if (handler != null) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    /**
     * 设置消息为已读
     *
     * @param messageIds 会话最后一条消息的id
     * @param groupId    会话id
     */
    public static void setMessageRead(String messageIds, String groupId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.设置消息已读 + "?messageId=" + messageIds
                + "&groupId=" + groupId + "&pcOrmobile=mobile";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
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
