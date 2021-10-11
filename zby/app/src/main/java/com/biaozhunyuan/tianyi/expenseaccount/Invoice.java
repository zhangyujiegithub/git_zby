package com.biaozhunyuan.tianyi.expenseaccount;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangAnMin on 2019/2/14.
 * 发票实体类
 */

public class Invoice implements Serializable {
    private String attachId;//附件id
    private String InvoiceNum;//发票号码
    private String InvoiceCode;//发票代码
    private String InvoiceDate;//开票日期
    private String TotalAmount;//合计金额
    private String TotalTax;//合计税额
    private String AmountInFiguers;//价税合计(小写)
    private String AmountInWords;//价税合计(大写)
    private String CheckCode;//校验码
    private String SellerName;//销售方名称
    private String SellerRegisterNum;//销售方纳税人识别号
    private String PurchaserName;//购方名称
    private String PurchaserRegisterNum;//购方纳税人识别号
    private List<Detail> CommodityName;//货物名称

    class Detail implements Serializable{
        private String word;
        private String row;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getInvoiceNum() {
        return InvoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        InvoiceNum = invoiceNum;
    }

    public String getInvoiceCode() {
        return InvoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        InvoiceCode = invoiceCode;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(String totalTax) {
        TotalTax = totalTax;
    }

    public String getAmountInFiguers() {
        return AmountInFiguers;
    }

    public void setAmountInFiguers(String amountInFiguers) {
        AmountInFiguers = amountInFiguers;
    }

    public String getAmountInWords() {
        return AmountInWords;
    }

    public void setAmountInWords(String amountInWords) {
        AmountInWords = amountInWords;
    }

    public String getCheckCode() {
        return CheckCode;
    }

    public void setCheckCode(String checkCode) {
        CheckCode = checkCode;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getSellerRegisterNum() {
        return SellerRegisterNum;
    }

    public void setSellerRegisterNum(String sellerRegisterNum) {
        SellerRegisterNum = sellerRegisterNum;
    }

    public String getPurchaserName() {
        return PurchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        PurchaserName = purchaserName;
    }

    public String getPurchaserRegisterNum() {
        return PurchaserRegisterNum;
    }

    public void setPurchaserRegisterNum(String purchaserRegisterNum) {
        PurchaserRegisterNum = purchaserRegisterNum;
    }

    public List<Detail> getCommodityName() {
        return CommodityName;
    }

    public void setCommodityName(List<Detail> commodityName) {
        CommodityName = commodityName;
    }
}
