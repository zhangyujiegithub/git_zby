package com.biaozhunyuan.tianyi.chatLibary.chat.model;



import com.biaozhunyuan.tianyi.common.global.GlobalModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangAnMin on 2018/12/4.
 */

public class UnreadMessage extends GlobalModel {
    private String name; //会话名称
    private String creator; //创建人
    private String createTime; //创建时间
    private String isSingle;
    private String lastUpdateTime;
    private String delStatus;
    private String status;
    private String icon;
    private int isDepartment = 0;

    private List<UnreadMessageContent> contents;

    private List<UpdateTime> updateTime;

    public class UpdateTime {
        private String mobileLastUpdateTime;
        private String pcLastUpdateTime;

        public String getPcLastUpdateTime() {
            return pcLastUpdateTime;
        }

        public void setPcLastUpdateTime(String pcLastUpdateTime) {
            this.pcLastUpdateTime = pcLastUpdateTime;
        }

        public String getMobileLastUpdateTime() {
            return mobileLastUpdateTime;
        }

        public void setMobileLastUpdateTime(String mobileLastUpdateTime) {
            this.mobileLastUpdateTime = mobileLastUpdateTime;
        }
    }


    public class UnreadMessageContent extends GlobalModel {
        private String body;
        private String content;
        private String fromId;
        private String name;
        private String groupId;
        private String createTime;
        private String format;
        private String messageId;
        private String targetId;
        private String isRead;
        private String creator;
        private String keyValues;
        private String isSingle;
        private int messageCount;
        private int isDepartment;
        private ChatMessage replyMsg;
        private HashMap<String,String> keyValue;


        public ChatMessage getReplyMsg() {
            return replyMsg;
        }

        public void setReplyMsg(ChatMessage replyMsg) {
            this.replyMsg = replyMsg;
        }

        public String getIsSingle() {
            return isSingle;
        }

        public void setIsSingle(String isSingle) {
            this.isSingle = isSingle;
        }

        public int getIsDepartment() {
            return isDepartment;
        }

        public void setIsDepartment(int isDepartment) {
            this.isDepartment = isDepartment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public HashMap<String, String> getKeyValue() {
            return keyValue;
        }

        public void setKeyValue(HashMap<String, String> keyValue) {
            this.keyValue = keyValue;
        }

        public String getKeyValues() {
            return keyValues;
        }

        public void setKeyValues(String keyValues) {
            this.keyValues = keyValues;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getFromId() {
            return fromId;
        }

        public void setFromId(String fromId) {
            this.fromId = fromId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }


        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getIsRead() {
            return isRead;
        }

        public void setIsRead(String isRead) {
            this.isRead = isRead;
        }
    }


    public int getIsDepartment() {
        return isDepartment;
    }

    public void setIsDepartment(int isDepartment) {
        this.isDepartment = isDepartment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<UpdateTime> getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(List<UpdateTime> updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(String isSingle) {
        this.isSingle = isSingle;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UnreadMessageContent> getContents() {
        return contents;
    }

    public void setContents(List<UnreadMessageContent> contents) {
        this.contents = contents;
    }

}
