package com.biaozhunyuan.tianyi.chatLibary.chat.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangAnMin on 2018/4/26.
 */

public class RecentMessage implements Serializable{
    private List<ChatMessage> recentMessages;

    public List<ChatMessage> getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(List<ChatMessage> recentMessages) {
        this.recentMessages = recentMessages;
    }
}
