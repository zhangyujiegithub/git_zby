package com.biaozhunyuan.tianyi.contract;

import java.math.BigDecimal;

/**
 * Created by 王安民 on 2017/10/1.
 * 合同实体类
 */

public class Contract {
    private String uuid;
    private String creatorId;//创建人
    private String creationTime;//创建时间
    private String flowId;//工作流
    private String attachment;//附件

    //客户信息
    private String serial;//合同编号
    private String contractType;//合同类型
    private String originalContractId;//合同类型
    private Integer type;//合同类型


    private String customerId;//客户
    private String customerName;//客户
    private String mobile;//联系电话
    private String phone;//座机

    private String customerType;//认购人类型
    private String certificateType;//证件类别

    private String certificateNo;//证件号码
    private String email;//邮箱

    private String address;//通讯地址


    //基金产品信息
    private BigDecimal amountLower;//认购金额小写
    private BigDecimal originalContractAmountLower;//投资金额
    private BigDecimal currentTransferAmountLower;//认购金额小写
    private String amountUpper;//认购金额大写

    private BigDecimal purchaseFeeLower;//认购费用小写
    private String purchaseFeeUpper;//认购费用大写


    private String productId;//理财产品
    private String productName;//理财产品

    private BigDecimal expectedAnnualYield;//预期年化收益率
    private String expectedAnnualYieldType;//预期年化收益率分类
    private String reservationId;//理财产品预约

    private Integer duration;//投资期限
    private Integer transferSymbol;//转让次数
    private String status;//状态
    private String advisorId;//理财师
    private String advisorName;//理财师名称

    private String clientManagerId;//客户经理
    private String department;//所属部门
    private String branchId;//所属分公司

    private String buyDate;//购买日期
    private String accountingDate;//到账日期
    private String redemptionDate;//赎回日期


    private Boolean isPayed;//是否兑付
    private Boolean isImported;//是否导入
    private String importedBatchNumber;//导入批次


    private String investStartDate;//投资起算日
    private String investDueDate;//投资到期日
    private String investValueDate;//投资起息日

    private String contractRecycleDate;//合同收回日期
    private String achivementDepartment;//业绩归属部门
    private String achivementBranch;//业绩归属分公司

    private Boolean isExpired;//是否到期
    private String currency;//币种
    private BigDecimal exchangeReate;//汇率

    private BigDecimal achievement;//业绩
    private String distributionRatio;//分单比例


    //账户信息
    private String receptAccountName;//收款账户名
    private String receptAccountBank;//收款账户开户行
    private String receptAccount;//收款账号

    private String payAccountName;//付款账户名
    private String payAccountBank;//付款账户开户行
    private String payAccount;//付款账号


    //附件
    private String idCopyFront;//身份证正反面复印件扫描件
    private String idCopyBack;//银行卡正反面复印件扫描件
    private String contractSignPage;//合同签署页扫描件

    private String payNoteCopy;//付款凭证扫描件
    private String clubAppFormFile;//俱乐部入会申请表
    private String riskEvalCardFile;//投资风险测评卡

    private String qualifiedInvestorCert;//合格投资者资产证明
    private String redemptionAppForm;//赎回申请单
    private String flowStatus;//审批状态

    private boolean isdeprecated;//是否已作废

    public BigDecimal getCurrentTransferAmountLower() {
        return currentTransferAmountLower;
    }

    public void setCurrentTransferAmountLower(BigDecimal currentTransferAmountLower) {
        this.currentTransferAmountLower = currentTransferAmountLower;
    }

    public Integer getTransferSymbol() {
        return transferSymbol;
    }

    public void setTransferSymbol(Integer transferSymbol) {
        this.transferSymbol = transferSymbol;
    }

    public boolean isIsdeprecated() {
        return isdeprecated;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOriginalContractId() {
        return originalContractId;
    }

    public void setOriginalContractId(String originalContractId) {
        this.originalContractId = originalContractId;
    }

    public BigDecimal getOriginalContractAmountLower() {
        return originalContractAmountLower;
    }

    public void setOriginalContractAmountLower(BigDecimal originalContractAmountLower) {
        this.originalContractAmountLower = originalContractAmountLower;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAmountLower() {
        return amountLower;
    }

    public void setAmountLower(BigDecimal amountLower) {
        this.amountLower = amountLower;
    }

    public String getAmountUpper() {
        return amountUpper;
    }

    public void setAmountUpper(String amountUpper) {
        this.amountUpper = amountUpper;
    }

    public BigDecimal getPurchaseFeeLower() {
        return purchaseFeeLower;
    }

    public void setPurchaseFeeLower(BigDecimal purchaseFeeLower) {
        this.purchaseFeeLower = purchaseFeeLower;
    }

    public String getPurchaseFeeUpper() {
        return purchaseFeeUpper;
    }

    public void setPurchaseFeeUpper(String purchaseFeeUpper) {
        this.purchaseFeeUpper = purchaseFeeUpper;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getExpectedAnnualYield() {
        return expectedAnnualYield;
    }

    public void setExpectedAnnualYield(BigDecimal expectedAnnualYield) {
        this.expectedAnnualYield = expectedAnnualYield;
    }

    public String getExpectedAnnualYieldType() {
        return expectedAnnualYieldType;
    }

    public void setExpectedAnnualYieldType(String expectedAnnualYieldType) {
        this.expectedAnnualYieldType = expectedAnnualYieldType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getClientManagerId() {
        return clientManagerId;
    }

    public void setClientManagerId(String clientManagerId) {
        this.clientManagerId = clientManagerId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getRedemptionDate() {
        return redemptionDate;
    }

    public void setRedemptionDate(String redemptionDate) {
        this.redemptionDate = redemptionDate;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public Boolean getImported() {
        return isImported;
    }

    public void setImported(Boolean imported) {
        isImported = imported;
    }

    public String getImportedBatchNumber() {
        return importedBatchNumber;
    }

    public void setImportedBatchNumber(String importedBatchNumber) {
        this.importedBatchNumber = importedBatchNumber;
    }

    public String getInvestStartDate() {
        return investStartDate;
    }

    public void setInvestStartDate(String investStartDate) {
        this.investStartDate = investStartDate;
    }

    public String getInvestDueDate() {
        return investDueDate;
    }

    public void setInvestDueDate(String investDueDate) {
        this.investDueDate = investDueDate;
    }

    public String getInvestValueDate() {
        return investValueDate;
    }

    public void setInvestValueDate(String investValueDate) {
        this.investValueDate = investValueDate;
    }

    public String getContractRecycleDate() {
        return contractRecycleDate;
    }

    public void setContractRecycleDate(String contractRecycleDate) {
        this.contractRecycleDate = contractRecycleDate;
    }

    public String getAchivementDepartment() {
        return achivementDepartment;
    }

    public void setAchivementDepartment(String achivementDepartment) {
        this.achivementDepartment = achivementDepartment;
    }

    public String getAchivementBranch() {
        return achivementBranch;
    }

    public void setAchivementBranch(String achivementBranch) {
        this.achivementBranch = achivementBranch;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeReate() {
        return exchangeReate;
    }

    public void setExchangeReate(BigDecimal exchangeReate) {
        this.exchangeReate = exchangeReate;
    }

    public BigDecimal getAchievement() {
        return achievement;
    }

    public void setAchievement(BigDecimal achievement) {
        this.achievement = achievement;
    }

    public String getDistributionRatio() {
        return distributionRatio;
    }

    public void setDistributionRatio(String distributionRatio) {
        this.distributionRatio = distributionRatio;
    }

    public String getReceptAccountName() {
        return receptAccountName;
    }

    public void setReceptAccountName(String receptAccountName) {
        this.receptAccountName = receptAccountName;
    }

    public String getReceptAccountBank() {
        return receptAccountBank;
    }

    public void setReceptAccountBank(String receptAccountBank) {
        this.receptAccountBank = receptAccountBank;
    }

    public String getReceptAccount() {
        return receptAccount;
    }

    public void setReceptAccount(String receptAccount) {
        this.receptAccount = receptAccount;
    }

    public String getPayAccountName() {
        return payAccountName;
    }

    public void setPayAccountName(String payAccountName) {
        this.payAccountName = payAccountName;
    }

    public String getPayAccountBank() {
        return payAccountBank;
    }

    public void setPayAccountBank(String payAccountBank) {
        this.payAccountBank = payAccountBank;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getIdCopyFront() {
        return idCopyFront;
    }

    public void setIdCopyFront(String idCopyFront) {
        this.idCopyFront = idCopyFront;
    }

    public String getIdCopyBack() {
        return idCopyBack;
    }

    public void setIdCopyBack(String idCopyBack) {
        this.idCopyBack = idCopyBack;
    }

    public String getContractSignPage() {
        return contractSignPage;
    }

    public void setContractSignPage(String contractSignPage) {
        this.contractSignPage = contractSignPage;
    }

    public String getPayNoteCopy() {
        return payNoteCopy;
    }

    public void setPayNoteCopy(String payNoteCopy) {
        this.payNoteCopy = payNoteCopy;
    }

    public String getClubAppFormFile() {
        return clubAppFormFile;
    }

    public void setClubAppFormFile(String clubAppFormFile) {
        this.clubAppFormFile = clubAppFormFile;
    }

    public String getRiskEvalCardFile() {
        return riskEvalCardFile;
    }

    public void setRiskEvalCardFile(String riskEvalCardFile) {
        this.riskEvalCardFile = riskEvalCardFile;
    }

    public String getQualifiedInvestorCert() {
        return qualifiedInvestorCert;
    }

    public void setQualifiedInvestorCert(String qualifiedInvestorCert) {
        this.qualifiedInvestorCert = qualifiedInvestorCert;
    }

    public String getRedemptionAppForm() {
        return redemptionAppForm;
    }

    public void setRedemptionAppForm(String redemptionAppForm) {
        this.redemptionAppForm = redemptionAppForm;
    }

    public boolean isdeprecated() {
        return isdeprecated;
    }

    public void setIsdeprecated(boolean isdeprecated) {
        this.isdeprecated = isdeprecated;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
