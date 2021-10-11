package com.biaozhunyuan.tianyi.wechat.model;

/**
 * 作者： bohr
 * 日期： 2020-06-15 14:19
 * 描述：微信联系人实体
 */
public class WeChatContact {
    private String customerIcon;
    private int customerType;
    private int msgTotalCount; //与联系人聊天的数量
    private String importTime;
    private String lastUpdateTime;
    private String nickName;
    private String remark;
    private String staffId;
    private String uuid;
    private String wechatId;
    private String wechatNumber;

    public int getMsgTotalCount() {
        return msgTotalCount;
    }

    public void setMsgTotalCount(int msgTotalCount) {
        this.msgTotalCount = msgTotalCount;
    }

    public String getCustomerIcon() {
        return customerIcon;
    }

    public void setCustomerIcon(String customerIcon) {
        this.customerIcon = customerIcon;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }
}
