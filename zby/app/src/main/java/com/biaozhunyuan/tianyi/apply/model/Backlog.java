package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/20 16:42
 */
public class Backlog implements Serializable {


    /**
     * uuid : 6a7e84c76958494591767b73797bd3af
     * serialNumber : bmq_00000027
     * workflowTemplate : 7c779d87874944569a91ad721458ef96
     * formDataId : 5334386293044020abd26d9a25134d01
     * name : 部门签报单
     * creatorId : 4aef5b9189a54cc08993204d8df6c222
     * departmentId : f90c2eb73c9c4142af1b1a11f027624e
     * createTime : 2020-11-09 14:49:32
     * currentState : 等待管理员审核
     * nextStepId : 5001
     * complete : false
     * nextStep : 拟稿人
     * lastStepCompleteTime : 2020-11-09 14:55:39
     * nextStepAuditorId : 4aef5b9189a54cc08993204d8df6c222,
     * prevStepAuditorId : 042d1b839ea47cf96c5ad48438c7cdc5
     * readed : true
     * rejectEnd : 0
     * round : 1
     * remark :
     * status : 2
     * submissionTime : 2020-11-09 14:49:47
     * lastUpdateTime : 2020-11-09 14:49:32
     * attachmentStatus : 0
     * filed : false
     * returnNodeStatus : 0
     * hasChildrenWorkflow : false
     * isCountersignWorkflow : false
     * isVoucherCreated : 0
     * myselefIsAlreadyRead : 0
     * receiverIsAlreadyRead : 0
     * isUrge : 0
     * formName : 部门签报单
     * myselefIsAlreadyReads : 未查看
     * creatorName : 管理员
     * isRead : 0
     */

    private String uuid;
    private String serialNumber;
    private String workflowTemplate;
    private String formDataId;
    private String name;
    private String creatorId;
    private String departmentId;
    private String createTime;
    private String currentState;
    private String nextStepId;
    private boolean complete;
    private String nextStep;
    private String lastStepCompleteTime;
    private String nextStepAuditorId;
    private String prevStepAuditorId;
    private boolean readed;
    private int rejectEnd;
    private int round;
    private String remark;
    private int status;
    private String submissionTime;
    private String lastUpdateTime;
    private int attachmentStatus;
    private boolean filed;
    private int returnNodeStatus;
    private boolean hasChildrenWorkflow;
    private boolean isCountersignWorkflow;
    private int isVoucherCreated;
    private String myselefIsAlreadyRead;
    private String receiverIsAlreadyRead;
    private int isUrge;
    private String formName;
    private String processTime;
    private String myselefIsAlreadyReads;
    private String creatorName;
    private int isRead;

    private String 文号;
    private String 标题;
    private String code;

    private String prevStepAuditor;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
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

    public String getNextStepAuditorId() {
        return nextStepAuditorId;
    }

    public void setNextStepAuditorId(String nextStepAuditorId) {
        this.nextStepAuditorId = nextStepAuditorId;
    }

    public String getPrevStepAuditorId() {
        return prevStepAuditorId;
    }

    public void setPrevStepAuditorId(String prevStepAuditorId) {
        this.prevStepAuditorId = prevStepAuditorId;
    }

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public int getRejectEnd() {
        return rejectEnd;
    }

    public void setRejectEnd(int rejectEnd) {
        this.rejectEnd = rejectEnd;
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

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(int attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
    }

    public boolean isFiled() {
        return filed;
    }

    public void setFiled(boolean filed) {
        this.filed = filed;
    }

    public int getReturnNodeStatus() {
        return returnNodeStatus;
    }

    public void setReturnNodeStatus(int returnNodeStatus) {
        this.returnNodeStatus = returnNodeStatus;
    }

    public boolean isHasChildrenWorkflow() {
        return hasChildrenWorkflow;
    }

    public void setHasChildrenWorkflow(boolean hasChildrenWorkflow) {
        this.hasChildrenWorkflow = hasChildrenWorkflow;
    }

    public boolean isIsCountersignWorkflow() {
        return isCountersignWorkflow;
    }

    public void setIsCountersignWorkflow(boolean isCountersignWorkflow) {
        this.isCountersignWorkflow = isCountersignWorkflow;
    }

    public int getIsVoucherCreated() {
        return isVoucherCreated;
    }

    public void setIsVoucherCreated(int isVoucherCreated) {
        this.isVoucherCreated = isVoucherCreated;
    }

    public String getMyselefIsAlreadyRead() {
        return myselefIsAlreadyRead;
    }

    public void setMyselefIsAlreadyRead(String myselefIsAlreadyRead) {
        this.myselefIsAlreadyRead = myselefIsAlreadyRead;
    }

    public String getReceiverIsAlreadyRead() {
        return receiverIsAlreadyRead;
    }

    public void setReceiverIsAlreadyRead(String receiverIsAlreadyRead) {
        this.receiverIsAlreadyRead = receiverIsAlreadyRead;
    }

    public int getIsUrge() {
        return isUrge;
    }

    public void setIsUrge(int isUrge) {
        this.isUrge = isUrge;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getMyselefIsAlreadyReads() {
        return myselefIsAlreadyReads;
    }

    public void setMyselefIsAlreadyReads(String myselefIsAlreadyReads) {
        this.myselefIsAlreadyReads = myselefIsAlreadyReads;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public boolean isCountersignWorkflow() {
        return isCountersignWorkflow;
    }

    public void setCountersignWorkflow(boolean countersignWorkflow) {
        isCountersignWorkflow = countersignWorkflow;
    }

    public String getPrevStepAuditor() {
        return prevStepAuditor;
    }

    public void setPrevStepAuditor(String prevStepAuditor) {
        this.prevStepAuditor = prevStepAuditor;
    }

    public String get文号() {
        return 文号;
    }

    public void set文号(String 文号) {
        this.文号 = 文号;
    }

    public String get标题() {
        return 标题;
    }

    public void set标题(String 标题) {
        this.标题 = 标题;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
