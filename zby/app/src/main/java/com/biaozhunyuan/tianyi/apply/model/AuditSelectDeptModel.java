package com.biaozhunyuan.tianyi.apply.model;

import cn.qqtheme.framework.entity.WheelItem;

/**
 * 作者： bohr
 * 日期： 2020-07-06 19:30
 * 描述：
 */
public class AuditSelectDeptModel implements WheelItem {
    private String uuid;
    private int nodeId;
    private String name;
    private String operatorName;
    private String workflowTemplateId;
    private String nextId;
    private int type;
    private int isSameTime;
    private String staffSelectType;
    private String staffSelectValue;
    private String opinionType;
    private String buttons;
    private String defaultNode;
    private String defaultValue;
    private String exitJson;
    private String groupIds;

    public int getIsSameTime() {
        return isSameTime;
    }

    public void setIsSameTime(int isSameTime) {
        this.isSameTime = isSameTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

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

    public String getWorkflowTemplateId() {
        return workflowTemplateId;
    }

    public void setWorkflowTemplateId(String workflowTemplateId) {
        this.workflowTemplateId = workflowTemplateId;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStaffSelectType() {
        return staffSelectType;
    }

    public void setStaffSelectType(String staffSelectType) {
        this.staffSelectType = staffSelectType;
    }

    public String getStaffSelectValue() {
        return staffSelectValue;
    }

    public void setStaffSelectValue(String staffSelectValue) {
        this.staffSelectValue = staffSelectValue;
    }

    public String getOpinionType() {
        return opinionType;
    }

    public void setOpinionType(String opinionType) {
        this.opinionType = opinionType;
    }

    public String getButtons() {
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
    }

    public String getDefaultNode() {
        return defaultNode;
    }

    public void setDefaultNode(String defaultNode) {
        this.defaultNode = defaultNode;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExitJson() {
        return exitJson;
    }

    public void setExitJson(String exitJson) {
        this.exitJson = exitJson;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    @Override
    public String toString() {
        return operatorName;
    }
}
