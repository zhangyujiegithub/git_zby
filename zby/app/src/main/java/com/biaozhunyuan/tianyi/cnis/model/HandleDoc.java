package com.biaozhunyuan.tianyi.cnis.model;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/24 9:23
 */
public class HandleDoc implements Serializable {


    /**
     * workflowId : c179e6fc37ce4b46a252098c673b4c8d
     * createTime : 2020-11-20 14:48:12
     * status : 5
     * workflowTemplate : 9c2558a510a042778e6adc6c36bb9111
     * code : 院发文
     * formDataId : 4adf75cf66fa4af09e45f4d2083f35e1
     * creatorId : d6681fb633e2f8619e9a546a94996fa6
     * lastUpdateTime : 2020-11-20 14:48:12
     * currentState : 已完成
     * prevStepAuditorId : 3394e59e4ea64ea0205cd962cd1d7752
     * nextStepAuditorId :
     * processTime : 2020-11-20 15:30:23
     * formName : 中国标准化研究院发文稿纸
     * 标题 : 测试
     * 文号 : 中标院党[2020]12461
     */

    private String workflowId;
    private String createTime;
    private int status;
    private String workflowTemplate;
    private String code;
    private String formDataId;
    private String creatorId;
    private String lastUpdateTime;
    private String currentState;
    private String prevStepAuditorId;
    private String nextStepAuditorId;
    private String processTime;
    private String formName;
    private String 标题;
    private String 文号;
    private String 拟稿人;

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getFormDataId() {
        return formDataId;
    }

    public void setFormDataId(String formDataId) {
        this.formDataId = formDataId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
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

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
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
