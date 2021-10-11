package com.biaozhunyuan.tianyi.newuihome;

import java.io.Serializable;

/**
 * @author GaoB
 * @description:
 * @date : 2020/11/19 11:00
 */
public class Notice implements Serializable {


    /**
     * uuid : f053ce479d4248d89d7c123e08e59d94
     * type : 1
     * title : 横向项目-项目验收通知
     * content : <p>项目1 </p><p>请针对上述1个项目发起项目验收工作。</p>
     * status : 2
     * creatorId : d6681fb633e2f8619e9a546a94996fa6
     * departmentId : 0ab4c6fa17382c607d6171949ee1a013
     * createTime : 2020-11-19 10:43:31
     * pushTime : 2020-11-19 10:43:00
     */



    private String uuid;
    private String type;
    private String title;
    private String content;
    private String status;
    private String creatorId;
    private String departmentId;
    private String createTime;
    private String pushTime;

    private String statusName;
    private String typeName;
    private String 标题;
    private String readStatus;

    private String serialNumber;
    private String workFlowId;
    private String attachmentIds;
    private String 发布时间;
    private String 发布人;
    private String 发布部门;
    private String 栏目;
    private String 内容;
    private String 附件;
    private String workflowTemplateId;



    public String get标题() {
        return 标题;
    }

    public void set标题(String 标题) {
        this.标题 = 标题;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public String get发布时间() {
        return 发布时间;
    }

    public void set发布时间(String 发布时间) {
        this.发布时间 = 发布时间;
    }

    public String get发布人() {
        return 发布人;
    }

    public void set发布人(String 发布人) {
        this.发布人 = 发布人;
    }

    public String get发布部门() {
        return 发布部门;
    }

    public void set发布部门(String 发布部门) {
        this.发布部门 = 发布部门;
    }

    public String get栏目() {
        return 栏目;
    }

    public void set栏目(String 栏目) {
        this.栏目 = 栏目;
    }

    public String get内容() {
        return 内容;
    }

    public void set内容(String 内容) {
        this.内容 = 内容;
    }

    public String get附件() {
        return 附件;
    }

    public void set附件(String 附件) {
        this.附件 = 附件;
    }

    public String getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(String workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }
    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
