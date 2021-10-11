package com.biaozhunyuan.tianyi.chatLibary.chat.model;

/**
 * 消息发送状态枚举类
 */
public enum MessageSendStatusEnum {

    // 枚举成员变量，默认是静态

    发送成功(1, "发送成功"), 发送中(2, "发送中"), 发送失败(3, "发送失败");

    // 私有成员变量，保存名称
    private int status;
    private String statusName;

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }

    // 带参构造函数
    MessageSendStatusEnum(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
