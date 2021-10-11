package com.biaozhunyuan.tianyi.product;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 王安民 on 2017/9/23.
 * 产品实体类
 */

public class Product implements Serializable {
    private String uuid;
    private String status;//基金状态 0未知|1产品预热|2募集中|3募集结束|4募集失败|5产品成立|6产品到期|7提前结束
    private String code;//产品代码
    private String name;//产品名称
    private String foundDay;//成立日
    private String buyDay;//开放申购日
    private String redeemDay;//赎回日
    private String expiryDay;//到期日
    private Integer issueType;//发行模式
    private String productType;//产品类型
    private String productTypeName;//产品名称
    private String newProductType;//新增产品类型
    private String renewDeadline;//产品续存期限
    private String dividendDay;//派息日
    private String annualYield;//参考业绩
    private String annualInterval;//参考业绩标准区间(%)
    private String incomeDistributionType;//收益分配方式
    private String credit;//增信措施
    private String investmentOrientation;//投资方向
    private String highlights;//产品亮点
    private String raisingAmounts;// 募集额度
    private BigDecimal fundManagementFee;//基金管理费
    private BigDecimal fundSubscriptionFee;//基金认购费
    private String fundManager;//基金管理人
    private String custodian;//基金托管人
    private String isRecord;//备案与否 0否|1是
    private String proveUrl;//备案证明文件存储地址
    private String level;//允许购买的客户等级
    private String risk;//能接受的客户风险等级
    private String cover;//产品封面
    private String notice;//产品成立公告
    private BigDecimal scoreFactor;//产品积分系数百分比,默认100%
    private boolean isRecommend;//是否推荐 1是|0否
    private boolean isDisplay;//产品是否显示 1显示|0不显示
    private String description;//产品简介
    private String creatorId;//创建人
    private String createTime;//记录增加时间
    private String detailContent;//详细内容
    private String currency;//币种
    private BigDecimal investThreshold;//起投金额
    private String throwDepartment;//投放分公司
    private String collectStart;//募集期的开始
    private String collectEnd;//募集期的结束
    private String investTerm;//投资期限
    private String backCover;//地产项目背景
    private String address;//地产项目地址
    private String showImages;//地产项目详图
    private Integer needReserve;//预约控制
    private Integer contractCount;//合同份数
    private Integer maxCustomerAccount;//最大客户数
    private String attachmentIds;//附件
    private Boolean isLiquidation;//是否清算
    private Boolean isDelete;//是否删除
    private Boolean canAppend;//是否允许追加
    private String productPointIds;//产品亮点标签
    private String remark;//备注
    private String issuer;//基金发行方
    private String fundFeeComponents;//基金费用
    private Double fundFeeRatio;//基金费用比例
    private String fundScale;//基金规模
    private String productManager;//产品经理
    private String productSource;//产品来源
    private String category;//产品类别
    private String durationDescription;//产品期限描述
    private String site;//产品所在地
    private String productUsage;//产品用途
    private String productAdvantages;//产品优势
    private Boolean dueRenew;//到期自动续投
    private String capacityStrategy;//额度控制策略
    private String capacity;//发行规模
    private String lawyer;//法律顾问
    private String riskControlMeasures;//风控措施
    private String repaymentSource;//还款来源
    private String fundRules;//基金形式
    private Boolean countHead;//计头
    private Boolean countTail;//计尾
    private String quartelyReport;//季度报告
    private String structure;//结构分级
    private String openRedemptionRule;//开放赎回规则
    private String accountBank;//开户行信息
    private BigDecimal raisingAmount;//募集额度
    private String collectAccount;//募集账户
    private Integer dayOfYear;//年天数
    private String clearRule;//清算分配规则
    private String clearDay;//清算日期
    private String securityTrader;//券商
    private BigDecimal minDeal;//认购起点
    private String minDealDescription;//认购起点描述
    private String financingParty;//融资公司介绍
    private String onlineStatus;//上线状态
    private Boolean shallClear;//是否清算
    private Boolean isTemp;//是临时产品
    private String dispositionRule;//收益分配规则
    private String dispositionPeriod;//收益分配周期
    private String investAdvisor;//投顾
    private String investStrategy;//投资策略
    private String investType;//投资方式
    private String investField;//投资领域
    private String investRestriction;//投资限制
    private String trusteeBank;//托管银行
    private Integer reserveValidTo;//预约有效期
    private Boolean allowAddInvest;//允许追加投资
    private String debtMaturity;//债权到期日
    private Integer minValueDays;//最小付息天数


    //新增字段

    private String assetManager;//资产管理人

    private Boolean isYieldFloat;//收益率浮动
    private Boolean isFixedYield;//收益率固定
    private Boolean isDoubleYield;//收益率固定+浮动

    private BigDecimal floatYield;//浮动收益率
    private BigDecimal doubleYield;//固定+浮动

    private String yieldType;//收益率类型
    private String reserved;//收益率类型
    private String operatorHQ;//收益率类型
    private String collected;//收益率类型



    private  String counterparty;//交易对手
    private  BigDecimal lineOfEarlyWarning;//预警线
    private  BigDecimal lineOfStopLoss;//止损线
    private  String styleOfQuit;//退出方式
    private  String newStyleOfQuit;//新增退出方式
    private  BigDecimal accumulativeAmplitude;//累加幅度
    private  Boolean isExtendLimitTimeIfAdd;//追加是否延长期限
    private  String structureChart;//产品结构图
    private  String affiliation;//产品归属
    private  String riskLevelType;//产品的风险评级
    private  BigDecimal commissionPercentage;//提成比例
    private  String openSeason;//开放期

    private  String smallAcountName;//小额账户名称
    private  String smallAcountBank;//小额账户开户行
    private  String smallAcountNumber;//小额账户账号

    private  String bigAcountName;//大额账户名称
    private  String bigAcountBank;//大额账户开户行
    private  String bigAcountNumber;//大额账户账号

    //提成比例

    private BigDecimal saleVolumeRatio;//业绩系数

    private BigDecimal commissionRateAdvisor;//投资顾问提成
    private BigDecimal commissionRateManager;//经理提成
    private BigDecimal commissionRatebranchLead;//分总提成
    private BigDecimal commissionRatedepartmentLead;//总监提成
    private BigDecimal commissionRateAreaLead;//区总提成

    private BigDecimal vicePresident;//副总裁提成
    private BigDecimal president;//总裁提成
    private BigDecimal reserveQuotaRatio;//预约额度比例

    private Boolean isReserve;//是否预约 1是|0否
    private Boolean reservePaused; //预约是否暂停

    //房产信息
    private String houseName;//房产名称
    private String contry;//国家
    private String province;//省
    private String city;//市
    private String area;//区
    private String houseLocation;//楼盘位置
    private String houseDescription;//楼盘介绍
    private String houseNumber;//套数
    private String priceSection;//价格区间
    private String sellingPointsIntroduction;//卖点介绍
    private String houseStatusId;//楼盘状态
    private String houseTypeId;//楼盘类型
    private String developersInformation;//开发商信息
    private String houseAttachmentId;//楼盘附件
    private String kind;//类别
    private int reserveCountControl;//预约人数控制


    public String getRaisingAmounts() {
        return raisingAmounts;
    }

    public void setRaisingAmounts(String raisingAmounts) {
        this.raisingAmounts = raisingAmounts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoundDay() {
        return foundDay;
    }

    public void setFoundDay(String foundDay) {
        this.foundDay = foundDay;
    }

    public String getBuyDay() {
        return buyDay;
    }

    public void setBuyDay(String buyDay) {
        this.buyDay = buyDay;
    }

    public String getRedeemDay() {
        return redeemDay;
    }

    public void setRedeemDay(String redeemDay) {
        this.redeemDay = redeemDay;
    }

    public String getExpiryDay() {
        return expiryDay;
    }

    public void setExpiryDay(String expiryDay) {
        this.expiryDay = expiryDay;
    }

    public Integer getIssueType() {
        return issueType;
    }

    public void setIssueType(Integer issueType) {
        this.issueType = issueType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getNewProductType() {
        return newProductType;
    }

    public void setNewProductType(String newProductType) {
        this.newProductType = newProductType;
    }

    public String getRenewDeadline() {
        return renewDeadline;
    }

    public void setRenewDeadline(String renewDeadline) {
        this.renewDeadline = renewDeadline;
    }

    public String getDividendDay() {
        return dividendDay;
    }

    public void setDividendDay(String dividendDay) {
        this.dividendDay = dividendDay;
    }

    public String getAnnualYield() {
        return annualYield;
    }

    public void setAnnualYield(String annualYield) {
        this.annualYield = annualYield;
    }

    public String getAnnualInterval() {
        return annualInterval;
    }

    public void setAnnualInterval(String annualInterval) {
        this.annualInterval = annualInterval;
    }

    public String getIncomeDistributionType() {
        return incomeDistributionType;
    }

    public void setIncomeDistributionType(String incomeDistributionType) {
        this.incomeDistributionType = incomeDistributionType;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getInvestmentOrientation() {
        return investmentOrientation;
    }

    public void setInvestmentOrientation(String investmentOrientation) {
        this.investmentOrientation = investmentOrientation;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public BigDecimal getFundManagementFee() {
        return fundManagementFee;
    }

    public void setFundManagementFee(BigDecimal fundManagementFee) {
        this.fundManagementFee = fundManagementFee;
    }

    public BigDecimal getFundSubscriptionFee() {
        return fundSubscriptionFee;
    }

    public void setFundSubscriptionFee(BigDecimal fundSubscriptionFee) {
        this.fundSubscriptionFee = fundSubscriptionFee;
    }

    public String getFundManager() {
        return fundManager;
    }

    public void setFundManager(String fundManager) {
        this.fundManager = fundManager;
    }

    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }

    public String getProveUrl() {
        return proveUrl;
    }

    public void setProveUrl(String proveUrl) {
        this.proveUrl = proveUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public BigDecimal getScoreFactor() {
        return scoreFactor;
    }

    public void setScoreFactor(BigDecimal scoreFactor) {
        this.scoreFactor = scoreFactor;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDetailContent() {
        return detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getInvestThreshold() {
        return investThreshold;
    }

    public void setInvestThreshold(BigDecimal investThreshold) {
        this.investThreshold = investThreshold;
    }

    public String getThrowDepartment() {
        return throwDepartment;
    }

    public void setThrowDepartment(String throwDepartment) {
        this.throwDepartment = throwDepartment;
    }

    public String getCollectStart() {
        return collectStart;
    }

    public void setCollectStart(String collectStart) {
        this.collectStart = collectStart;
    }

    public String getCollectEnd() {
        return collectEnd;
    }

    public void setCollectEnd(String collectEnd) {
        this.collectEnd = collectEnd;
    }

    public String getInvestTerm() {
        return investTerm;
    }

    public void setInvestTerm(String investTerm) {
        this.investTerm = investTerm;
    }

    public String getBackCover() {
        return backCover;
    }

    public void setBackCover(String backCover) {
        this.backCover = backCover;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShowImages() {
        return showImages;
    }

    public void setShowImages(String showImages) {
        this.showImages = showImages;
    }

    public Integer getNeedReserve() {
        return needReserve;
    }

    public void setNeedReserve(Integer needReserve) {
        this.needReserve = needReserve;
    }

    public Integer getContractCount() {
        return contractCount;
    }

    public void setContractCount(Integer contractCount) {
        this.contractCount = contractCount;
    }

    public Integer getMaxCustomerAccount() {
        return maxCustomerAccount;
    }

    public void setMaxCustomerAccount(Integer maxCustomerAccount) {
        this.maxCustomerAccount = maxCustomerAccount;
    }

    public String getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(String attachmentIds) {
        this.attachmentIds = attachmentIds;
    }

    public Boolean getLiquidation() {
        return isLiquidation;
    }

    public void setLiquidation(Boolean liquidation) {
        isLiquidation = liquidation;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Boolean getCanAppend() {
        return canAppend;
    }

    public void setCanAppend(Boolean canAppend) {
        this.canAppend = canAppend;
    }

    public String getProductPointIds() {
        return productPointIds;
    }

    public void setProductPointIds(String productPointIds) {
        this.productPointIds = productPointIds;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getFundFeeComponents() {
        return fundFeeComponents;
    }

    public void setFundFeeComponents(String fundFeeComponents) {
        this.fundFeeComponents = fundFeeComponents;
    }

    public Double getFundFeeRatio() {
        return fundFeeRatio;
    }

    public void setFundFeeRatio(Double fundFeeRatio) {
        this.fundFeeRatio = fundFeeRatio;
    }

    public String getFundScale() {
        return fundScale;
    }

    public void setFundScale(String fundScale) {
        this.fundScale = fundScale;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }

    public String getProductSource() {
        return productSource;
    }

    public void setProductSource(String productSource) {
        this.productSource = productSource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDurationDescription() {
        return durationDescription;
    }

    public void setDurationDescription(String durationDescription) {
        this.durationDescription = durationDescription;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getProductUsage() {
        return productUsage;
    }

    public void setProductUsage(String productUsage) {
        this.productUsage = productUsage;
    }

    public String getProductAdvantages() {
        return productAdvantages;
    }

    public void setProductAdvantages(String productAdvantages) {
        this.productAdvantages = productAdvantages;
    }

    public Boolean getDueRenew() {
        return dueRenew;
    }

    public void setDueRenew(Boolean dueRenew) {
        this.dueRenew = dueRenew;
    }

    public String getCapacityStrategy() {
        return capacityStrategy;
    }

    public void setCapacityStrategy(String capacityStrategy) {
        this.capacityStrategy = capacityStrategy;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLawyer() {
        return lawyer;
    }

    public void setLawyer(String lawyer) {
        this.lawyer = lawyer;
    }

    public String getRiskControlMeasures() {
        return riskControlMeasures;
    }

    public void setRiskControlMeasures(String riskControlMeasures) {
        this.riskControlMeasures = riskControlMeasures;
    }

    public String getRepaymentSource() {
        return repaymentSource;
    }

    public void setRepaymentSource(String repaymentSource) {
        this.repaymentSource = repaymentSource;
    }

    public String getFundRules() {
        return fundRules;
    }

    public void setFundRules(String fundRules) {
        this.fundRules = fundRules;
    }

    public Boolean getCountHead() {
        return countHead;
    }

    public void setCountHead(Boolean countHead) {
        this.countHead = countHead;
    }

    public Boolean getCountTail() {
        return countTail;
    }

    public void setCountTail(Boolean countTail) {
        this.countTail = countTail;
    }

    public String getQuartelyReport() {
        return quartelyReport;
    }

    public void setQuartelyReport(String quartelyReport) {
        this.quartelyReport = quartelyReport;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getOpenRedemptionRule() {
        return openRedemptionRule;
    }

    public void setOpenRedemptionRule(String openRedemptionRule) {
        this.openRedemptionRule = openRedemptionRule;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public BigDecimal getRaisingAmount() {
        return raisingAmount;
    }

    public void setRaisingAmount(BigDecimal raisingAmount) {
        this.raisingAmount = raisingAmount;
    }

    public String getCollectAccount() {
        return collectAccount;
    }

    public void setCollectAccount(String collectAccount) {
        this.collectAccount = collectAccount;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Integer dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public String getClearRule() {
        return clearRule;
    }

    public void setClearRule(String clearRule) {
        this.clearRule = clearRule;
    }

    public String getClearDay() {
        return clearDay;
    }

    public void setClearDay(String clearDay) {
        this.clearDay = clearDay;
    }

    public String getSecurityTrader() {
        return securityTrader;
    }

    public void setSecurityTrader(String securityTrader) {
        this.securityTrader = securityTrader;
    }

    public BigDecimal getMinDeal() {
        return minDeal;
    }

    public void setMinDeal(BigDecimal minDeal) {
        this.minDeal = minDeal;
    }

    public String getMinDealDescription() {
        return minDealDescription;
    }

    public void setMinDealDescription(String minDealDescription) {
        this.minDealDescription = minDealDescription;
    }

    public String getFinancingParty() {
        return financingParty;
    }

    public void setFinancingParty(String financingParty) {
        this.financingParty = financingParty;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Boolean getShallClear() {
        return shallClear;
    }

    public void setShallClear(Boolean shallClear) {
        this.shallClear = shallClear;
    }

    public Boolean getTemp() {
        return isTemp;
    }

    public void setTemp(Boolean temp) {
        isTemp = temp;
    }

    public String getDispositionRule() {
        return dispositionRule;
    }

    public void setDispositionRule(String dispositionRule) {
        this.dispositionRule = dispositionRule;
    }

    public String getDispositionPeriod() {
        return dispositionPeriod;
    }

    public void setDispositionPeriod(String dispositionPeriod) {
        this.dispositionPeriod = dispositionPeriod;
    }

    public String getInvestAdvisor() {
        return investAdvisor;
    }

    public void setInvestAdvisor(String investAdvisor) {
        this.investAdvisor = investAdvisor;
    }

    public String getInvestStrategy() {
        return investStrategy;
    }

    public void setInvestStrategy(String investStrategy) {
        this.investStrategy = investStrategy;
    }

    public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }

    public String getInvestField() {
        return investField;
    }

    public void setInvestField(String investField) {
        this.investField = investField;
    }

    public String getInvestRestriction() {
        return investRestriction;
    }

    public void setInvestRestriction(String investRestriction) {
        this.investRestriction = investRestriction;
    }

    public String getTrusteeBank() {
        return trusteeBank;
    }

    public void setTrusteeBank(String trusteeBank) {
        this.trusteeBank = trusteeBank;
    }

    public Integer getReserveValidTo() {
        return reserveValidTo;
    }

    public void setReserveValidTo(Integer reserveValidTo) {
        this.reserveValidTo = reserveValidTo;
    }

    public Boolean getAllowAddInvest() {
        return allowAddInvest;
    }

    public void setAllowAddInvest(Boolean allowAddInvest) {
        this.allowAddInvest = allowAddInvest;
    }

    public String getDebtMaturity() {
        return debtMaturity;
    }

    public void setDebtMaturity(String debtMaturity) {
        this.debtMaturity = debtMaturity;
    }

    public Integer getMinValueDays() {
        return minValueDays;
    }

    public void setMinValueDays(Integer minValueDays) {
        this.minValueDays = minValueDays;
    }

    public String getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(String assetManager) {
        this.assetManager = assetManager;
    }

    public Boolean getYieldFloat() {
        return isYieldFloat;
    }

    public void setYieldFloat(Boolean yieldFloat) {
        isYieldFloat = yieldFloat;
    }

    public Boolean getFixedYield() {
        return isFixedYield;
    }

    public void setFixedYield(Boolean fixedYield) {
        isFixedYield = fixedYield;
    }

    public Boolean getDoubleYield() {
        return isDoubleYield;
    }

    public void setDoubleYield(BigDecimal doubleYield) {
        this.doubleYield = doubleYield;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public BigDecimal getLineOfEarlyWarning() {
        return lineOfEarlyWarning;
    }

    public void setLineOfEarlyWarning(BigDecimal lineOfEarlyWarning) {
        this.lineOfEarlyWarning = lineOfEarlyWarning;
    }

    public BigDecimal getLineOfStopLoss() {
        return lineOfStopLoss;
    }

    public void setLineOfStopLoss(BigDecimal lineOfStopLoss) {
        this.lineOfStopLoss = lineOfStopLoss;
    }

    public String getStyleOfQuit() {
        return styleOfQuit;
    }

    public void setStyleOfQuit(String styleOfQuit) {
        this.styleOfQuit = styleOfQuit;
    }

    public String getNewStyleOfQuit() {
        return newStyleOfQuit;
    }

    public void setNewStyleOfQuit(String newStyleOfQuit) {
        this.newStyleOfQuit = newStyleOfQuit;
    }

    public BigDecimal getAccumulativeAmplitude() {
        return accumulativeAmplitude;
    }

    public void setAccumulativeAmplitude(BigDecimal accumulativeAmplitude) {
        this.accumulativeAmplitude = accumulativeAmplitude;
    }

    public Boolean getExtendLimitTimeIfAdd() {
        return isExtendLimitTimeIfAdd;
    }

    public void setExtendLimitTimeIfAdd(Boolean extendLimitTimeIfAdd) {
        isExtendLimitTimeIfAdd = extendLimitTimeIfAdd;
    }

    public String getStructureChart() {
        return structureChart;
    }

    public void setStructureChart(String structureChart) {
        this.structureChart = structureChart;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getRiskLevelType() {
        return riskLevelType;
    }

    public void setRiskLevelType(String riskLevelType) {
        this.riskLevelType = riskLevelType;
    }

    public BigDecimal getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(BigDecimal commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public String getOpenSeason() {
        return openSeason;
    }

    public void setOpenSeason(String openSeason) {
        this.openSeason = openSeason;
    }

    public String getSmallAcountName() {
        return smallAcountName;
    }

    public void setSmallAcountName(String smallAcountName) {
        this.smallAcountName = smallAcountName;
    }

    public String getSmallAcountBank() {
        return smallAcountBank;
    }

    public void setSmallAcountBank(String smallAcountBank) {
        this.smallAcountBank = smallAcountBank;
    }

    public String getSmallAcountNumber() {
        return smallAcountNumber;
    }

    public void setSmallAcountNumber(String smallAcountNumber) {
        this.smallAcountNumber = smallAcountNumber;
    }

    public String getBigAcountName() {
        return bigAcountName;
    }

    public void setBigAcountName(String bigAcountName) {
        this.bigAcountName = bigAcountName;
    }

    public String getBigAcountBank() {
        return bigAcountBank;
    }

    public void setBigAcountBank(String bigAcountBank) {
        this.bigAcountBank = bigAcountBank;
    }

    public String getBigAcountNumber() {
        return bigAcountNumber;
    }

    public void setBigAcountNumber(String bigAcountNumber) {
        this.bigAcountNumber = bigAcountNumber;
    }

    public BigDecimal getSaleVolumeRatio() {
        return saleVolumeRatio;
    }

    public void setSaleVolumeRatio(BigDecimal saleVolumeRatio) {
        this.saleVolumeRatio = saleVolumeRatio;
    }

    public BigDecimal getCommissionRateAdvisor() {
        return commissionRateAdvisor;
    }

    public void setCommissionRateAdvisor(BigDecimal commissionRateAdvisor) {
        this.commissionRateAdvisor = commissionRateAdvisor;
    }

    public BigDecimal getCommissionRateManager() {
        return commissionRateManager;
    }

    public void setCommissionRateManager(BigDecimal commissionRateManager) {
        this.commissionRateManager = commissionRateManager;
    }

    public BigDecimal getCommissionRatebranchLead() {
        return commissionRatebranchLead;
    }

    public void setCommissionRatebranchLead(BigDecimal commissionRatebranchLead) {
        this.commissionRatebranchLead = commissionRatebranchLead;
    }

    public BigDecimal getCommissionRatedepartmentLead() {
        return commissionRatedepartmentLead;
    }

    public void setCommissionRatedepartmentLead(BigDecimal commissionRatedepartmentLead) {
        this.commissionRatedepartmentLead = commissionRatedepartmentLead;
    }

    public BigDecimal getCommissionRateAreaLead() {
        return commissionRateAreaLead;
    }

    public void setCommissionRateAreaLead(BigDecimal commissionRateAreaLead) {
        this.commissionRateAreaLead = commissionRateAreaLead;
    }

    public BigDecimal getVicePresident() {
        return vicePresident;
    }

    public void setVicePresident(BigDecimal vicePresident) {
        this.vicePresident = vicePresident;
    }

    public BigDecimal getPresident() {
        return president;
    }

    public void setPresident(BigDecimal president) {
        this.president = president;
    }

    public BigDecimal getReserveQuotaRatio() {
        return reserveQuotaRatio;
    }

    public void setReserveQuotaRatio(BigDecimal reserveQuotaRatio) {
        this.reserveQuotaRatio = reserveQuotaRatio;
    }

    public Boolean getReserve() {
        return isReserve;
    }

    public void setReserve(Boolean reserve) {
        isReserve = reserve;
    }

    public Boolean getReservePaused() {
        return reservePaused;
    }

    public void setReservePaused(Boolean reservePaused) {
        this.reservePaused = reservePaused;
    }

    public String getOperatorHQ() {
        return operatorHQ;
    }

    public void setOperatorHQ(String operatorHQ) {
        this.operatorHQ = operatorHQ;
    }

    public void setDoubleYield(Boolean doubleYield) {
        isDoubleYield = doubleYield;
    }

    public BigDecimal getFloatYield() {
        return floatYield;
    }

    public void setFloatYield(BigDecimal floatYield) {
        this.floatYield = floatYield;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getCollected() {
        return collected;
    }

    public void setCollected(String collected) {
        this.collected = collected;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getYieldType() {
        return yieldType;
    }

    public void setYieldType(String yieldType) {
        this.yieldType = yieldType;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(String houseLocation) {
        this.houseLocation = houseLocation;
    }

    public String getHouseDescription() {
        return houseDescription;
    }

    public void setHouseDescription(String houseDescription) {
        this.houseDescription = houseDescription;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPriceSection() {
        return priceSection;
    }

    public void setPriceSection(String priceSection) {
        this.priceSection = priceSection;
    }

    public String getSellingPointsIntroduction() {
        return sellingPointsIntroduction;
    }

    public void setSellingPointsIntroduction(String sellingPointsIntroduction) {
        this.sellingPointsIntroduction = sellingPointsIntroduction;
    }

    public String getHouseStatusId() {
        return houseStatusId;
    }

    public void setHouseStatusId(String houseStatusId) {
        this.houseStatusId = houseStatusId;
    }

    public String getHouseTypeId() {
        return houseTypeId;
    }

    public void setHouseTypeId(String houseTypeId) {
        this.houseTypeId = houseTypeId;
    }

    public String getDevelopersInformation() {
        return developersInformation;
    }

    public void setDevelopersInformation(String developersInformation) {
        this.developersInformation = developersInformation;
    }

    public String getHouseAttachmentId() {
        return houseAttachmentId;
    }

    public void setHouseAttachmentId(String houseAttachmentId) {
        this.houseAttachmentId = houseAttachmentId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getReserveCountControl() {
        return reserveCountControl;
    }

    public void setReserveCountControl(int reserveCountControl) {
        this.reserveCountControl = reserveCountControl;
    }
}
