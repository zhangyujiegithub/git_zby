package com.biaozhunyuan.tianyi.models;

import java.io.Serializable;

/**
 * 作者： bohr
 * 日期： 2020-07-15 16:58
 * 描述：
 */
public class StaffModel implements Serializable {
    private String corpName;
    private String departmentId;
    private String email;
    private String familyPhone;
    private String mobilePhone;
    private String name;
    private String station;
    private String telephone;
    private String uuid;
    private String switchboard;
    private String workPhone;

    public String getSwitchboard() {
        return switchboard;
    }

    public void setSwitchboard(String switchboard) {
        this.switchboard = switchboard;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }
}
