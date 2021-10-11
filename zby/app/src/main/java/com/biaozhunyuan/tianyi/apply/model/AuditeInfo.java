package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/9/12.
 * 审批意见实体类
 */

public class AuditeInfo implements Serializable {

    private String workflowId;//流程编号

    private String userId;//用户编号

    private String userName;//用户姓名

    private String opinion;//意见

    private String processTime;//处理时间

    private String result;//结果

    private String leaderSign;//领导签字

    private String totalTime;//审批总耗时

    private String intervalTime;//审批耗时

    private Integer nodeId;//节点编号

    private int round;

    private String title;//节点标题

    private boolean approvalCostTime;

    public String getTotalTime() {
        return totalTime;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isApprovalCostTime() {
        return approvalCostTime;
    }

    public void setApprovalCostTime(boolean approvalCostTime) {
        this.approvalCostTime = approvalCostTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLeaderSign() {
        return leaderSign;
    }

    public void setLeaderSign(String leaderSign) {
        this.leaderSign = leaderSign;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
