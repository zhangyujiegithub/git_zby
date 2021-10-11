package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/9/8.
 */

public class WorkflowInstance implements Serializable {

    public String title;
    public String uuid;

    private String companyId;//公司id

    private String serialNumber;//流水号

    private String workflowTemplate;//流程分类编号


    private String formDataId;//表单数据编号
    private String icon;//表单数据编号
    private String summary;
    private String lookStatus; //表单查看状态

    /// <summary>
    /// 需要和数据库表名一致
    /// </summary>

    private String name;
    private String formName; //表单名称
//    @NoDbColumn
//    private String 项目编号;


    private String creatorId;//创建人

    private String agentId;//代办人

    private String nextStepAuditorId;//下一个步骤审核人


    private String createTime;//创建时间


    private String submissionTime;//提交时间
    /// <summary>
    /// 未提交时为“"未提交"”
    /// </summary>

    private String currentState;    // 当前状态
    /// <summary>
    /// 当前所处节点
    /// </summary>

    private String nextStepId;//下一个步骤编号


    private String nextStep;//下个步骤


    private String lastStepCompleteTime;//上个步骤完成时间


    private boolean complete;//完成


    private String rejectEnd;//否决结束


    private boolean readed;
    private String 已读时间;
    private String 附件;
    private long 评论数;
    private int 业务员;

    private int 客户;

    private int round;


    private String remark;//摘要


    private int status;


    private int SupportCount;


    private int DiamondCount;


    private int MySupportCount;


    private int MyDiamondCount;


    public String getLookStatus() {
        return lookStatus;
    }

    public void setLookStatus(String lookStatus) {
        this.lookStatus = lookStatus;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getWorkflowTemplate() {
        return workflowTemplate;
    }

    public void setWorkflowTemplate(String workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getNextStepAuditorId() {
        return nextStepAuditorId;
    }

    public void setNextStepAuditorId(String nextStepAuditorId) {
        this.nextStepAuditorId = nextStepAuditorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(String nextStepId) {
        this.nextStepId = nextStepId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getLastStepCompleteTime() {
        return lastStepCompleteTime;
    }

    public void setLastStepCompleteTime(String lastStepCompleteTime) {
        this.lastStepCompleteTime = lastStepCompleteTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String isRejectEnd() {
        return rejectEnd;
    }

    public void setRejectEnd(String rejectEnd) {
        this.rejectEnd = rejectEnd;
    }

    public boolean getReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public String get已读时间() {
        return 已读时间;
    }

    public void set已读时间(String 已读时间) {
        this.已读时间 = 已读时间;
    }

    public String get附件() {
        return 附件;
    }

    public void set附件(String 附件) {
        this.附件 = 附件;
    }

    public long get评论数() {
        return 评论数;
    }

    public void set评论数(long 评论数) {
        this.评论数 = 评论数;
    }

    public int get业务员() {
        return 业务员;
    }

    public void set业务员(int 业务员) {
        this.业务员 = 业务员;
    }

    public int get客户() {
        return 客户;
    }

    public void set客户(int 客户) {
        this.客户 = 客户;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSupportCount() {
        return SupportCount;
    }

    public void setSupportCount(int supportCount) {
        SupportCount = supportCount;
    }

    public int getDiamondCount() {
        return DiamondCount;
    }

    public void setDiamondCount(int diamondCount) {
        DiamondCount = diamondCount;
    }

    public int getMySupportCount() {
        return MySupportCount;
    }

    public void setMySupportCount(int mySupportCount) {
        MySupportCount = mySupportCount;
    }

    public int getMyDiamondCount() {
        return MyDiamondCount;
    }

    public void setMyDiamondCount(int myDiamondCount) {
        MyDiamondCount = myDiamondCount;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRejectEnd() {
        return rejectEnd;
    }
}
