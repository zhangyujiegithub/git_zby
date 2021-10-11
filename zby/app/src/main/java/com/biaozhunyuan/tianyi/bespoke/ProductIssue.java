package com.biaozhunyuan.tianyi.bespoke;

import java.math.BigDecimal;

/**
 * Created by 王安民 on 2017/11/23.
 * 期次
 */

public class ProductIssue {
    private String uuid;
    private String issue;//期次
    private String batchNumber;//批次
    private String project;//所投项目
    private BigDecimal currentLine;//本期额度
    private BigDecimal additionalAmount;//附加额度
    private BigDecimal realRaisingAmount;//实际募集金额
    private String status;//状态
    private String initialOperationDate;//起始运作日期
    private String openDate;//开放日
    private String  closeDate;//关闭日
    private String  foundDate;//成立日(确认日)
    private BigDecimal compensationRate;//补偿利率(%)
    private String productId;//产品
    private BigDecimal excessYield;//超额收益(%)


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public BigDecimal getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(BigDecimal currentLine) {
        this.currentLine = currentLine;
    }

    public BigDecimal getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(BigDecimal additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public BigDecimal getRealRaisingAmount() {
        return realRaisingAmount;
    }

    public void setRealRaisingAmount(BigDecimal realRaisingAmount) {
        this.realRaisingAmount = realRaisingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInitialOperationDate() {
        return initialOperationDate;
    }

    public void setInitialOperationDate(String initialOperationDate) {
        this.initialOperationDate = initialOperationDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(String foundDate) {
        this.foundDate = foundDate;
    }

    public BigDecimal getCompensationRate() {
        return compensationRate;
    }

    public void setCompensationRate(BigDecimal compensationRate) {
        this.compensationRate = compensationRate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getExcessYield() {
        return excessYield;
    }

    public void setExcessYield(BigDecimal excessYield) {
        this.excessYield = excessYield;
    }
}
