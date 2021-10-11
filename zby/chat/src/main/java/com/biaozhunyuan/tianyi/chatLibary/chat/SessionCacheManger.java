package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.text.TextUtils;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupMembers;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/12/4.
 */

public class SessionCacheManger {

    private static SharedPreferencesHelper preferencesHelper;


    /**
     * 保存会话，如果已经有该会话，会话列表保存最新一条数据，如果没有，插入一条新的会话
     *
     * @param context
     * @param chatId
     * @param msg
     */
    public static void saveSession(Context context, String chatId, ChatMessage msg) {

        GroupSession model = new GroupSession();
        model.setChatId(msg.getChatId());
        model.setAvatar(msg.getAvatar());
        model.setLastMessage(msg.getBody());
        model.setChatType(msg.getChatType());
        model.setFrom(msg.getFrom());
        model.setSendStatus(msg.getSendStatus());
        model.setPcLastUpdateTime(msg.getPcLastUpdateTime());
        model.setLastUpdateTime(msg.getGroupUpdateTime());
        model.setLastMessageSendTime(msg.getSendTime());
        model.setLastMessageFormat(msg.getFormat());

        if (msg.getKeyValues() != null) {
            model.setMembers(msg.getKeyValues().get("members"));
            if (!TextUtils.isEmpty(msg.getKeyValues().get("isDepartment"))) {
                model.setDepartMent(Integer.valueOf(msg.getKeyValues().get("isDepartment"))); //是否是部门聊天
            }
            if (!TextUtils.isEmpty(msg.getKeyValues().get("name"))) {
                model.setName(msg.getKeyValues().get("name"));
            }
        }
        if (msg.getFromUuid() != null && msg.getFromUuid().contains(Global.mUser.getUuid())) { //如果fromid是自己，将对话放置右侧
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
        if (ChatMessage.FORMAT_TIP.equals(msg.getFormat())) {
            //修改群名称
            if (!TextUtils.isEmpty(msg.getBody())
                    && msg.getBody().contains("修改")) {
                String body = msg.getBody();
                String groupName = body.substring(body.indexOf("\"") + 1, body.lastIndexOf("\""));
                model.setName(groupName);
            }
        }

        preferencesHelper = new SharedPreferencesHelper(context);
        List<GroupSession> sessions = preferencesHelper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);//会话列表
        boolean isAlreadyExits = false; //是否已经有该聊天
        for (int i = 0; i < sessions.size(); i++) {//判断会话列表是否已经有该聊天
            GroupSession session = sessions.get(i);
            if (session.getChatId().equals(model.getChatId())) { //有该会话
                isAlreadyExits = true;
                model.setTop(session.isTop());
                model.setSingle(session.isSingle());
                model.setDepartMent(session.isDepartMent());
                if (1 == (session.isSingle())) {
                    model.setName(session.getName());
                } else {
                    if (msg.getKeyValues() != null) {
                        if (!TextUtils.isEmpty(msg.getKeyValues().get("name"))) {
                            model.setName(msg.getKeyValues().get("name"));
                        }
                    }
                }
                model.setSetNoInterrupt(session.isSetNoInterrupt());
                if (ChatMessage.CHAT_Left == msg.getChatCategory()
                        && msg.getKeyValues() != null && "1".equals(msg.getKeyValues().get("isSingle"))) {
                    model.setAvatar(msg.getFromUuid());
                    model.setName(session.getName());
                }
                if (session.isDepartMent() == 1) {
                    model.setDepartMent(1);
                }
                //如果头像为空，并且之前头像不为空，沿用之前的头像
                if (TextUtils.isEmpty(model.getAvatar())) {
                    if (!TextUtils.isEmpty(session.getAvatar())) {
                        model.setAvatar(session.getAvatar());
                    }
                }
                //如果名称为空，并且之前名称不为空，沿用之前的名称
                if (TextUtils.isEmpty(model.getName())) {
                    model.setName(session.getName());
                }
                if (!TextUtils.isEmpty(msg.getKeyValues().get("@"))) {
                    if (session.getAtType() == 0) {
                        //有人@所有人
                        if ("all".equals(msg.getKeyValues().get("@"))) {
                            model.setAtType(2);
                            model.setAtMessageId(msg.getId());

                            //有人@你
                        } else if (msg.getKeyValues().get("@").contains(Global.mUser.getUuid())) {
                            model.setAtType(1);
                            model.setAtMessageId(msg.getId());
                        }
                    }
                } else {
                    model.setAtType(session.getAtType());
                    model.setAtMessageId(session.getAtMessageId());
                }
                sessions.remove(session); //移除之前的数据
                if (!chatId.equals(model.getChatId()) && msg.getChatCategory() != ChatMessage.CHAT_RIGHT) {
                    if (!TextUtils.isEmpty(model.getPcLastUpdateTime()) && !TextUtils.isEmpty(msg.getCreateTime())) {
                        /**
                         * 如果消息的pc最后更新时间在创建时间之前，则表示pc端未读
                         * 反之，则表示pc端已读
                         */
                        if (DateTimeUtil.compareDate(model.getPcLastUpdateTime(), msg.getCreateTime()) == -1) {
                            model.setUnreadCount(session.getUnreadCount() + 1); //设置消息未读数
                        }
                    } else {
                        model.setUnreadCount(session.getUnreadCount() + 1); //设置消息未读数
                    }
                }
                sessions.add(model); //添加最新的一条消息
                break;
            }
        }
        if (!isAlreadyExits) { //如果没有，插入一条新的会话
            if (!TextUtils.isEmpty(msg.getAvatar())) {
                model.setAvatar(msg.getAvatar());
            }

            if (ChatMessage.CHAT_Left == msg.getChatCategory() ||
                    ChatMessage.CHAT_LEFT_AUDIO == msg.getChatCategory() ||
                    ChatMessage.CHAT_LEFT_FILE == msg.getChatCategory()) {
                model.setUnreadCount(1); //设置消息未读数

                if (msg.getKeyValues() != null && "1".equals(msg.getKeyValues().get("isSingle"))) {
                    DictionaryHelper helper = new DictionaryHelper(context);
                    model.setName(helper.getUserNameById(msg.getFromUuid()));
                    model.setAvatar(msg.getFromUuid());
                    model.setSingle(1);
                }

                if (!TextUtils.isEmpty(msg.getKeyValues().get("@"))) {
                    //有人@所有人
                    if ("all".equals(msg.getKeyValues().get("@"))) {
                        model.setAtType(2);
                        model.setAtMessageId(msg.getId());

                        //有人@你
                    } else if (msg.getKeyValues().get("@").contains(Global.mUser.getUuid())) {
                        model.setAtType(1);
                        model.setAtMessageId(msg.getId());
                    }
                }
            } else {
                if (msg.getKeyValues() != null) {
                    String members = msg.getKeyValues().get("members");
                    if (!TextUtils.isEmpty(members)) {  //如果是单聊，members是自己的id和对方的id拼接起来
                        members = members.replace(",", "");
                        members = members.replace(Global.mUser.getUuid(), "");
                        model.setAvatar(members);
                    }
                    if ("1".equals(msg.getKeyValues().get("isSingle"))) {
                        model.setSingle(1);
                    }
                }
            }
            sessions.add(model);
        }
        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);  //保存会话列表到本地
    }


    /**
     * 根据会话id获取该会话群组成员,并且保存会话
     *
     * @param message
     */
    public static void getSessionStaffsAndSaveSession(final Context context, final String chatId, final ChatMessage message) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组成员 + "?groupId=" + message.getChatId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<GroupMembers> groupMembers = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupMembers.class);

                String name = "";//群组名称
                String staffIds = "";//群成员
                if (groupMembers != null) {
                    if (groupMembers.size() > 2) { //群组中不止两个人，证明是多聊
                        for (GroupMembers members : groupMembers) {
                            name += members.getName() + ",";
                            staffIds += members.getUuid() + ",";
                        }
                        if (name.length() > 0) {
                            name = name.substring(0, name.length() - 1);
                            staffIds = staffIds.substring(0, staffIds.length() - 1);
                        }
                    } else if (groupMembers.size() == 2) { //两个人，证明是单聊
                        for (GroupMembers members : groupMembers) {
                            if (Global.mUser.getUuid().equals(members.getUuid())) {
                                groupMembers.remove(members);  //去掉自己，剩下的就是对方
                                break;
                            }
                        }
                        name = groupMembers.get(0).getName();
                        staffIds = groupMembers.get(0).getUuid();
                        message.setAvatar(groupMembers.get(0).getUuid());
                    }
                    message.setName(name);
                    message.setStaffIds(staffIds);
                    message.setFrom(message.getFrom());

//                    saveSession(context, chatId, message);
                }
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
