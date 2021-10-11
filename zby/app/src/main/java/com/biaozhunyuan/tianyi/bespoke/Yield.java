package com.biaozhunyuan.tianyi.bespoke;

import java.math.BigDecimal;

/**
 * Created by 王安民 on 2017/10/1.
 * 收益率
 */

public class Yield {
    private String uuid;
    private String productId;// 理财产品
    private Integer duration;// 投资期限
    private BigDecimal min;// 最小投资额
    private BigDecimal max;// 最大投资额
    private BigDecimal yield;// 收益率
    private boolean isDisable;// 停用
    private BigDecimal fee;//费用
    private int feeType;// 费用类型
    private String remark;// 备注
    private boolean isYieldFloat;// 浮动
    private String category;// 分类
    private String issue;// 期次
    private String batchNumber;// 批次
    private Integer floatingDuration;// 浮动投资期限


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

    public Integer getFloatingDuration() {
        return floatingDuration;
    }

    public void setFloatingDuration(Integer floatingDuration) {
        this.floatingDuration = floatingDuration;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getYield() {
        return yield;
    }

    public void setYield(BigDecimal yield) {
        this.yield = yield;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isYieldFloat() {
        return isYieldFloat;
    }

    public void setYieldFloat(boolean yieldFloat) {
        isYieldFloat = yieldFloat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
