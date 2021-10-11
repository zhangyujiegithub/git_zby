package com.biaozhunyuan.tianyi.wechat.model;

/**
 * 作者： bohr
 * 日期： 2020-06-15 14:23
 * 描述：微信群聊实体
 */
public class WeChatRoom {
    private String lastUpdateTime;
    private String memberCount;
    private String memberIdList;
    private long modifyTime;
    private String name;
    private String roomId;
    private String roomOwnerId;
    private String staffId;
    private String uuid;
    private int msgTotalCount; //与联系人聊天的数量

    public int getMsgTotalCount() {
        return msgTotalCount;
    }

    public void setMsgTotalCount(int msgTotalCount) {
        this.msgTotalCount = msgTotalCount;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    public String getMemberIdList() {
        return memberIdList;
    }

    public void setMemberIdList(String memberIdList) {
        this.memberIdList = memberIdList;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomOwnerId() {
        return roomOwnerId;
    }

    public void setRoomOwnerId(String roomOwnerId) {
        this.roomOwnerId = roomOwnerId;
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
}
