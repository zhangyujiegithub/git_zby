package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/12/5.
 */

public class ChartIntentUtils {

    public static SocketService socketService;


    /**
     * 跳转到聊天详细页面
     *
     * @param userIds 聊天员工id
     */
    public static void startChatInfo(final Context context, String userIds) {
        final String groupName;
        final DictionaryHelper helper = new DictionaryHelper(context);
        final Intent intent = new Intent(context, ChatActivity.class);

        if (userIds.contains(",")) {  //多人聊天
            if (userIds.contains(Global.mUser.getUuid())) { //如果选中的群聊成员中包含自己，从其中去掉自己的id
                userIds = userIds.replace(Global.mUser.getUuid() + ",", "");
                userIds = userIds.replace("," + Global.mUser.getUuid(), "");
            }
            groupName = helper.getUserNamesById(Global.mUser.getUuid() + "," + userIds);
        } else {
            groupName = helper.getUserNameById(userIds);
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.新增会话 + "?groupName=" + groupName + "&staffIds=" + userIds;
        final String userUuids = userIds;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    GroupSession session = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), GroupSession.class);
                    if (session != null) {
                        session.setChatId(session.getUuid());
                        if (!userUuids.contains(",")) { //单人聊天设置头像
                            session.setAvatar(userUuids);
                            session.setMembers(userUuids + "," + Global.mUser.getUuid());  //设置群成员
                            session.setName(helper.getUserNameById(userUuids));
                            session.setSingle(1);
                        } else {
                            session.setAvatar(session.getIcon());
                        }
                        intent.putExtra("chatUser", session);
                        intent.putExtra("userIds", userUuids);
                        context.startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
