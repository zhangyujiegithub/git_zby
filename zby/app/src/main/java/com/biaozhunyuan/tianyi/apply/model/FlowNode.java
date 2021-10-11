package com.biaozhunyuan.tianyi.apply.model;

/**
 * Created by wangAnMin on 2018/7/31.
 * 流程节点
 */

public class FlowNode {
    private Boolean isStartNode;
    private Boolean isEndNode;
    private String title;
    private String uuid;
    private String agreeNextNode;
    private int disagreeNextProcess;
    private String workflowType;
    private String nextStep;
    private String auditorId;

    private String auditorName;
    private String auditDepartment;

    private String auditeDeptName;
    private String auditPosition;

    private String auditePostName;
    private int auditFunctionPoint;

    private String auditeFunctionName;
    private int auditWay;
    private String workflowCompletionNotification;
    private Integer nodeId;
    private String hideCells;
    private String opinion;
    private String result;
    private String canWriteCells;
    private String requiredCells;
    private String formConfigFile;
    private String coordinate;
    private Boolean noAuditorAutoSkip;
    private boolean approvalCostTime; //是否显示审批耗时

    private int status;
    private Boolean nextStepAuditorIncludeOneselfSkip;

    //        private List<WorkflowNodesGraph> Conditions;
    private String formField;
    private Boolean upSearching;
    private Boolean countersign;
    private Boolean previousStepSpecifiesCurrentAuditor = false;
    private int SubmitAuditEndDepartmentLevel;
    private String submissionTimeRelationField;
    private String 审核人名称;

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

    public String get审核人名称() {
        return 审核人名称;
    }

    public void set审核人名称(String 审核人名称) {
        this.审核人名称 = 审核人名称;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getStartNode() {
        return isStartNode;
    }

    public void setStartNode(Boolean startNode) {
        isStartNode = startNode;
    }

    public Boolean getEndNode() {
        return isEndNode;
    }

    public void setEndNode(Boolean endNode) {
        isEndNode = endNode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgreeNextNode() {
        return agreeNextNode;
    }

    public void setAgreeNextNode(String agreeNextNode) {
        this.agreeNextNode = agreeNextNode;
    }

    public int getDisagreeNextProcess() {
        return disagreeNextProcess;
    }

    public void setDisagreeNextProcess(int disagreeNextProcess) {
        this.disagreeNextProcess = disagreeNextProcess;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getAuditDepartment() {
        return auditDepartment;
    }

    public void setAuditDepartment(String auditDepartment) {
        this.auditDepartment = auditDepartment;
    }

    public String getAuditeDeptName() {
        return auditeDeptName;
    }

    public void setAuditeDeptName(String auditeDeptName) {
        this.auditeDeptName = auditeDeptName;
    }

    public String getAuditPosition() {
        return auditPosition;
    }

    public void setAuditPosition(String auditPosition) {
        this.auditPosition = auditPosition;
    }

    public String getAuditePostName() {
        return auditePostName;
    }

    public void setAuditePostName(String auditePostName) {
        this.auditePostName = auditePostName;
    }

    public int getAuditFunctionPoint() {
        return auditFunctionPoint;
    }

    public void setAuditFunctionPoint(int auditFunctionPoint) {
        this.auditFunctionPoint = auditFunctionPoint;
    }

    public String getAuditeFunctionName() {
        return auditeFunctionName;
    }

    public void setAuditeFunctionName(String auditeFunctionName) {
        this.auditeFunctionName = auditeFunctionName;
    }

    public int getAuditWay() {
        return auditWay;
    }

    public void setAuditWay(int auditWay) {
        this.auditWay = auditWay;
    }

    public String getWorkflowCompletionNotification() {
        return workflowCompletionNotification;
    }

    public void setWorkflowCompletionNotification(String workflowCompletionNotification) {
        this.workflowCompletionNotification = workflowCompletionNotification;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getHideCells() {
        return hideCells;
    }

    public void setHideCells(String hideCells) {
        this.hideCells = hideCells;
    }

    public String getCanWriteCells() {
        return canWriteCells;
    }

    public void setCanWriteCells(String canWriteCells) {
        this.canWriteCells = canWriteCells;
    }

    public String getRequiredCells() {
        return requiredCells;
    }

    public void setRequiredCells(String requiredCells) {
        this.requiredCells = requiredCells;
    }

    public String getFormConfigFile() {
        return formConfigFile;
    }

    public void setFormConfigFile(String formConfigFile) {
        this.formConfigFile = formConfigFile;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Boolean getNoAuditorAutoSkip() {
        return noAuditorAutoSkip;
    }

    public void setNoAuditorAutoSkip(Boolean noAuditorAutoSkip) {
        this.noAuditorAutoSkip = noAuditorAutoSkip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getNextStepAuditorIncludeOneselfSkip() {
        return nextStepAuditorIncludeOneselfSkip;
    }

    public void setNextStepAuditorIncludeOneselfSkip(Boolean nextStepAuditorIncludeOneselfSkip) {
        this.nextStepAuditorIncludeOneselfSkip = nextStepAuditorIncludeOneselfSkip;
    }

    public String getFormField() {
        return formField;
    }

    public void setFormField(String formField) {
        this.formField = formField;
    }

    public Boolean getUpSearching() {
        return upSearching;
    }

    public void setUpSearching(Boolean upSearching) {
        this.upSearching = upSearching;
    }

    public Boolean getCountersign() {
        return countersign;
    }

    public void setCountersign(Boolean countersign) {
        this.countersign = countersign;
    }

    public Boolean getPreviousStepSpecifiesCurrentAuditor() {
        return previousStepSpecifiesCurrentAuditor;
    }

    public void setPreviousStepSpecifiesCurrentAuditor(Boolean previousStepSpecifiesCurrentAuditor) {
        this.previousStepSpecifiesCurrentAuditor = previousStepSpecifiesCurrentAuditor;
    }

    public int getSubmitAuditEndDepartmentLevel() {
        return SubmitAuditEndDepartmentLevel;
    }

    public void setSubmitAuditEndDepartmentLevel(int submitAuditEndDepartmentLevel) {
        SubmitAuditEndDepartmentLevel = submitAuditEndDepartmentLevel;
    }

    public String getSubmissionTimeRelationField() {
        return submissionTimeRelationField;
    }

    public void setSubmissionTimeRelationField(String submissionTimeRelationField) {
        this.submissionTimeRelationField = submissionTimeRelationField;
    }
}
