package com.biaozhunyuan.tianyi.apply.model;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/9/7.
 * 流程分类表
 */

public class WorkflowTemplate implements Serializable {
    private String uuid;
    private String name;//此名称实际上对应了数据库表的名字
    private String formName;//表单名称
    private String creatorId;//创建人id
    private String formConfigurationFile;//表单配置文件
    private String category;//类别
    private String code;//代码
    private String workflowFininshedHandle;//流程成功结束时要调用的Handle的类名，比如 ZL.Data.Web.Service.库存管理DomainService
    private String workflowHandle;//表单事件，存放的是类名，例：com.biaozhunyuan.tianyi.service.workflow.Events.Customized.VSheetEventHandler
    private String eventScriptFile;//事件脚本文件
    private boolean noProcess;//不走流程
    private int type;//类型
    private String serialNumberPrefix;//序号前缀
    private boolean isFormClassify = false; // 是否是表单分类默认为false
    private String formClassifyName; //表单分类名称


    public boolean isFormClassify() {
        return isFormClassify;
    }

    public void setFormClassify(boolean formClassify) {
        isFormClassify = formClassify;
    }

    public String getFormClassifyName() {
        return formClassifyName;
    }

    public void setFormClassifyName(String formClassifyName) {
        this.formClassifyName = formClassifyName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormConfigurationFile() {
        return formConfigurationFile;
    }

    public void setFormConfigurationFile(String formConfigurationFile) {
        this.formConfigurationFile = formConfigurationFile;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWorkflowFininshedHandle() {
        return workflowFininshedHandle;
    }

    public void setWorkflowFininshedHandle(String workflowFininshedHandle) {
        this.workflowFininshedHandle = workflowFininshedHandle;
    }

    public String getWorkflowHandle() {
        return workflowHandle;
    }

    public void setWorkflowHandle(String workflowHandle) {
        this.workflowHandle = workflowHandle;
    }

    public String getEventScriptFile() {
        return eventScriptFile;
    }

    public void setEventScriptFile(String eventScriptFile) {
        this.eventScriptFile = eventScriptFile;
    }

    public boolean isNoProcess() {
        return noProcess;
    }

    public void setNoProcess(boolean noProcess) {
        this.noProcess = noProcess;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSerialNumberPrefix() {
        return serialNumberPrefix;
    }

    public void setSerialNumberPrefix(String serialNumberPrefix) {
        this.serialNumberPrefix = serialNumberPrefix;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
