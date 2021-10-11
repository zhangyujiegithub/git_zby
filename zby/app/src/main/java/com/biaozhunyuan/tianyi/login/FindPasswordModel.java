package com.biaozhunyuan.tianyi.login;

import java.io.Serializable;

/**
 * <p/>
 * 找回密码
 */
public class FindPasswordModel implements Serializable {

    private String enterpriseName;
    private String contractName;
    private String mobile;
    private String password;
    private String verifyCode;
    private String ValidateCode;
    private String ValidateTime;


    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidateCode() {
        return ValidateCode;
    }

    public void setValidateCode(String validateCode) {
        ValidateCode = validateCode;
    }

    public String getValidateTime() {
        return ValidateTime;
    }

    public void setValidateTime(String validateTime) {
        ValidateTime = validateTime;
    }


}
