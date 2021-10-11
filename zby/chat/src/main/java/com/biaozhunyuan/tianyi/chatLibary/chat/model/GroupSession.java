package com.biaozhunyuan.tianyi.chatLibary.chat.model;



import com.biaozhunyuan.tianyi.common.global.GlobalModel;

import java.io.Serializable;

/**
 * Created by wangAnMin on 2018/12/5.
 */

public class GroupSession extends GlobalModel implements Serializable {
    private String name; //会话名称
    private Integer isSingle = 0; //是否是单人聊天
    private int atType = 0; //@员工类型，0=没有人@你， 1=有人@你， 2=有人@所有人
    private String atMessageId; //@的那条消息的id
    private String chatId;//会话id
    private String from;//会话id
    private String chatType;//会话类型
    private String creator;//创建人
    private String createTime;//创建时间
    private String members;//如果是单聊，传入群成员
    private String lastMessage; //最后一条消息内容
    private String lastMessageId; //最后一条消息内容
    private String lastMessageFormat; //最后一条消息内容类型
    private String lastMessageSendTime; //最后一条消息时间
    private int unreadCount;//未读数量
    private String avatar;//群组头像
    private String icon;//群组头像
    private String pcLastUpdateTime;
    private String mobileLastUpdateTime;
    private String lastUpdateTime;
    private Integer sendStatus;
    private boolean isTop = false; //是否置顶
    private boolean isQuite = false; //是否已退出群组
    private boolean isSetNoInterrupt = false; //是否免打扰
    private int isDepartment = 0; //是否是部门聊天,默认不是
    private String topTime; //置顶时间


    public boolean isQuite() {
        return isQuite;
    }

    public void setQuite(boolean quite) {
        isQuite = quite;
    }

    public boolean isSetNoInterrupt() {
        return isSetNoInterrupt;
    }

    public void setSetNoInterrupt(boolean setNoInterrupt) {
        isSetNoInterrupt = setNoInterrupt;
    }

    public int isDepartMent() {
        return isDepartment;
    }

    public void setDepartMent(int departMent) {
        isDepartment = departMent;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAtMessageId() {
        return atMessageId;
    }

    public void setAtMessageId(String atMessageId) {
        this.atMessageId = atMessageId;
    }

    public int getAtType() {
        return atType;
    }

    public void setAtType(int atType) {
        this.atType = atType;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getLastMessageSendTime() {
        return lastMessageSendTime;
    }

    public void setLastMessageSendTime(String lastMessageSendTime) {
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getLastMessageFormat() {
        return lastMessageFormat;
    }

    public void setLastMessageFormat(String lastMessageFormat) {
        this.lastMessageFormat = lastMessageFormat;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer isSingle() {
        return isSingle;
    }

    public void setSingle(Integer single) {
        isSingle = single;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
