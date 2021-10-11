package com.biaozhunyuan.tianyi.apply.model;

/**
 * Created by 王安民 on 2017/9/8.
 * 审批流程状态
 */

public enum EnumApplyStatus {
    未提交 (1,"未提交"),
    审批中 (2,"审批中"),
    已退回 (3,"已退回"),
    已否决 (4,"已否决"),
    已通过 (5,"已通过"),
    已终止 (6,"已终止"),
    已作废 (7,"已作废"),
    附件错误(8, "附件错误");


    private int status;
    private String statusName;

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return statusName;
    }

    EnumApplyStatus(int status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
