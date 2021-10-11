package com.biaozhunyuan.tianyi.wechat.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 作者： bohr
 * 日期： 2020-06-15 17:20
 * 描述：微信消息实体
 */
public class WeChatMsg implements MultiItemEntity {
    private String attachmentId;
    private long createTime;
    private int direction; //1:发送，0：接收
    private int isRoomMsg;
    private String msgContent;
    private String msgSvrId;
    private String msgType;//1:文本和表情；3：图片 ；34:语音； 43：视频；
    private String sender;
    private String staffId;
    private String uuid;
    private String wechatId;
    private String localAttach;
    /**
     * 聊天对象头像
     */
    private String customerIcon;

    /**
     * 系统头像
     */
    private String systemIcon;


    public String getLocalAttach() {
        return localAttach;
    }

    public void setLocalAttach(String localAttach) {
        this.localAttach = localAttach;
    }

    public String getCustomerIcon() {
        return customerIcon;
    }

    public void setCustomerIcon(String customerIcon) {
        this.customerIcon = customerIcon;
    }

    public String getSystemIcon() {
        return systemIcon;
    }

    public void setSystemIcon(String systemIcon) {
        this.systemIcon = systemIcon;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getIsRoomMsg() {
        return isRoomMsg;
    }

    public void setIsRoomMsg(int isRoomMsg) {
        this.isRoomMsg = isRoomMsg;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgSvrId() {
        return msgSvrId;
    }

    public void setMsgSvrId(String msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    @Override
    public int getItemType() {
        return Integer.valueOf(msgType);
    }
}
