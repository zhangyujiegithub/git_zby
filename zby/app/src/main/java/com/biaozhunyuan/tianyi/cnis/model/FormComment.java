package com.biaozhunyuan.tianyi.cnis.model;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/24 17:46
 */
public class FormComment implements Serializable {


    /**
     * name : 部门审核
     * operatorName : 领域负责人审核
     * uuid : fb031d6069ce426f89315739a15168c7
     * workflowId : ee9135052e68412e92a9556e39e1097f
     * userId : 40f42d671225a4f21fe9fd5aedad8df6
     * opinion : 同意1
     * result : 通过
     * processTime : 2020-11-24 15:06:57
     * processSpendSecond : 510
     * nodeId : 4
     * Round : 1
     * ip : 172.17.70.96
     * phoneType : 电脑
     * userName : 洪岩
     * userDeptName : 高新技术标准化研究所
     * deptUser : 高新技术标准化研究所>洪岩
     */

    private String name;
    private String operatorName;
    private String uuid;
    private String workflowId;
    private String userId;
    private String opinion;
    private String result;
    private String processTime;
    private int processSpendSecond;
    private int nodeId;
    private int Round;
    private String ip;
    private String phoneType;
    private String userName;
    private String userDeptName;
    private String deptUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public int getProcessSpendSecond() {
        return processSpendSecond;
    }

    public void setProcessSpendSecond(int processSpendSecond) {
        this.processSpendSecond = processSpendSecond;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getRound() {
        return Round;
    }

    public void setRound(int Round) {
        this.Round = Round;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDeptName() {
        return userDeptName;
    }

    public void setUserDeptName(String userDeptName) {
        this.userDeptName = userDeptName;
    }

    public String getDeptUser() {
        return deptUser;
    }

    public void setDeptUser(String deptUser) {
        this.deptUser = deptUser;
    }
}
