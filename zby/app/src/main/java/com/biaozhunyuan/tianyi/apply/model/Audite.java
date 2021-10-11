package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/9/14.
 * 审批实体类
 */

public class Audite implements Serializable {
    private String workflowId;
    private String opinion;//审批意见
    private int type; //审批类型：1==通过；2==拒绝
    private String auditorIds;
    private String NodeSelectId;
    private String NodeName;
    private String NodeType;


    private String shenPiShunXu;//审批顺序
    private String yiJianType;//意见类型yes或者no



    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuditorIds() {
        return auditorIds;
    }

    public void setAuditorIds(String auditorIds) {
        this.auditorIds = auditorIds;
    }

    public String getNodeSelectId() {
        return NodeSelectId;
    }

    public void setNodeSelectId(String nodeSelectId) {
        NodeSelectId = nodeSelectId;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getNodeType() {
        return NodeType;
    }

    public void setNodeType(String nodeType) {
        NodeType = nodeType;
    }

    public String getShenPiShunXu() {
        return shenPiShunXu;
    }

    public void setShenPiShunXu(String shenPiShunXu) {
        this.shenPiShunXu = shenPiShunXu;
    }

    public String getYiJianType() {
        return yiJianType;
    }

    public void setYiJianType(String yiJianType) {
        this.yiJianType = yiJianType;
    }
}
