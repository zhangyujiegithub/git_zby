package com.biaozhunyuan.tianyi.cnis.model;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/23 18:55
 */
public class BacklogDoc implements Serializable {


    /**
     * workflowId : 2e47dbcc433b4634a959f8f8a81caef2
     * formDataId : 01c34bf541d344d3bee2ec4e55c37da3
     * workflowTemplate : 9c2558a510c042778e6adg6c36bb9113
     * code : 党委发文
     * formName : 中国标准化研究院党委发文稿纸
     * status : 2
     * creatorId : bb1fae9e4b6c80406a45faaf1384dbaa
     * currentState : 等待沙江,谢晨,张文理审核
     * lastUpdateTime : 2020-11-05 13:54:25
     * prevStepAuditorId : 3536df771bbf3a424e286785d152fb5b
     * nextStepAuditorId : c3688f0d296391d56050d5a937ff51af,bb1fae9e4b6c80406a45faaf1384dbaa
     * nextStep : 党委发文起草
     * creatorDepartmentId : 0ab4c6fa17382c607d6171949ee1a013
     * 标题 : 3
     * 文号 : 中标院党发[2020]10
     */

    private String workflowId;
    private String formDataId;
    private String workflowTemplate;
    private String code;
    private String formName;
    private int status;
    private String creatorId;
    private String currentState;
    private String lastUpdateTime;
    private String prevStepAuditorId;
    private String nextStepAuditorId;
    private String nextStep;
    private String creatorDepartmentId;
    private String 标题;
    private String 文号;
    private String 拟稿人;

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getWorkflowTemplate() {
        return workflowTemplate;
    }

    public void setWorkflowTemplate(String workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String get标题() {
        return 标题;
    }

    public void set标题(String 标题) {
        this.标题 = 标题;
    }

    public String get文号() {
        return 文号;
    }

    public void set文号(String 文号) {
        this.文号 = 文号;
    }

    public String get拟稿人() {
        return 拟稿人;
    }

    public void set拟稿人(String 拟稿人) {
        this.拟稿人 = 拟稿人;
    }
}
