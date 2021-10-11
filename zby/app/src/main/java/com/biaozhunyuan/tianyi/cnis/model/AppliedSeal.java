package com.biaozhunyuan.tianyi.cnis.model;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/24 11:24
 */
public class AppliedSeal implements Serializable {


    /**
     * uuid : cd4ffe8365c549e3910f2b2bc4e7ea1b
     * serialNumber : YFW_00000098
     * creatorId : 042d1b839ea47cf96c5ad48438c7cdc5
     * workFlowId : 09bc5c26f3a54fe498f52e3a8ab54958
     * createTime : 2020-11-16 11:12:36
     * attachmentIds :
     * applicationDate : 2020-11-16 00:00:00
     * 申请部门 : d5775b914f6c4be6e2708ea3eb5dd86e
     * 院领导批示 :
     * 标题 : 5
     * 印章种类 : 1
     * 部门负责人 :
     * 经办人 : 李文武/2020-11-16
     * 办公室审核 :
     * 会签部门意见 :
     * 文号 : 〔〕
     * isDelete : 0
     * type : 9c2558a510a042778e6adc6c36b15987
     * 手签章 :
     * workflowTemplate : 9c2558a510a042778e6adc6c36b15987
     * status : 1
     * currentState : 已保存
     * lastUpdateTime : 2020-11-16 11:12:36
     * prevStepAuditorId :
     * nextStepAuditorId :
     * nextStep : 未提交
     * creatorDepartmentId : d5775b914f6c4be6e2708ea3eb5dd86e
     */

    private String uuid;
    private String serialNumber;
    private String creatorId;
    private String creatorName;
    private String workFlowId;
    private String createTime;
    private String attachmentIds;
    private String applicationDate;
    private String 申请部门;
    private String 院领导批示;
    private String 标题;
    private String 项目名称;
    private String 货物名称;
    private String 印章种类;
    private String 部门负责人;
    private String 经办人;
    private String 办公室审核;
    private String 会签部门意见;
    private String 文号;
    private int isDelete;
    private String type;
    private String 手签章;
    private String workflowTemplate;
    private int status;
    private String currentState;
    private String lastUpdateTime;
    private String prevStepAuditorId;
    private String prevStepAuditorName;
    private String nextStepAuditorId;
    private String nextStep;
    private String creatorDepartmentId;
    private String docType;
    private String code;
    private String formName;
    private String processTime;
    private String formDataId;
    private String prevHandler;


    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getPrevStepAuditorName() {
        return prevStepAuditorName;
    }

    public void setPrevStepAuditorName(String prevStepAuditorName) {
        this.prevStepAuditorName = prevStepAuditorName;
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

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String get申请部门() {
        return 申请部门;
    }

    public void set申请部门(String 申请部门) {
        this.申请部门 = 申请部门;
    }

    public String get院领导批示() {
        return 院领导批示;
    }

    public void set院领导批示(String 院领导批示) {
        this.院领导批示 = 院领导批示;
    }

    public String get标题() {
        return 标题;
    }

    public void set标题(String 标题) {
        this.标题 = 标题;
    }
    public String get项目名称() {
        return 项目名称;
    }

    public void set项目名称(String 项目名称) {
        this.项目名称 = 项目名称;
    }

    public String get货物名称() {
        return 货物名称;
    }

    public void set货物名称(String 货物名称) {
        this.货物名称 = 货物名称;
    }

    public String get印章种类() {
        return 印章种类;
    }

    public void set印章种类(String 印章种类) {
        this.印章种类 = 印章种类;
    }

    public String get部门负责人() {
        return 部门负责人;
    }

    public void set部门负责人(String 部门负责人) {
        this.部门负责人 = 部门负责人;
    }

    public String get经办人() {
        return 经办人;
    }

    public void set经办人(String 经办人) {
        this.经办人 = 经办人;
    }

    public String get办公室审核() {
        return 办公室审核;
    }

    public void set办公室审核(String 办公室审核) {
        this.办公室审核 = 办公室审核;
    }

    public String get会签部门意见() {
        return 会签部门意见;
    }

    public void set会签部门意见(String 会签部门意见) {
        this.会签部门意见 = 会签部门意见;
    }

    public String get文号() {
        return 文号;
    }

    public void set文号(String 文号) {
        this.文号 = 文号;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String get手签章() {
        return 手签章;
    }

    public void set手签章(String 手签章) {
        this.手签章 = 手签章;
    }

    public String getWorkflowTemplate() {
        return workflowTemplate;
    }

    public void setWorkflowTemplate(String workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getPrevStepAuditorId() {
        return prevStepAuditorId;
    }

    public void setPrevStepAuditorId(String prevStepAuditorId) {
        this.prevStepAuditorId = prevStepAuditorId;
    }

    public String getNextStepAuditorId() {
        return nextStepAuditorId;
    }

    public void setNextStepAuditorId(String nextStepAuditorId) {
        this.nextStepAuditorId = nextStepAuditorId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getCreatorDepartmentId() {
        return creatorDepartmentId;
    }

    public void setCreatorDepartmentId(String creatorDepartmentId) {
        this.creatorDepartmentId = creatorDepartmentId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getPrevHandler() {
        return prevHandler;
    }

    public void setPrevHandler(String prevHandler) {
        this.prevHandler = prevHandler;
    }
}
