package com.biaozhunyuan.tianyi.attendance;

/**
 * 考勤信息
 */
public class OaAttendance {
    private String uuid;
    /**
     * 创建人
     */
    private String creatorId;
    /**
     * 创建人部门
     */
    private String creationDepartmentId;
    /**
     * 创建时间
     */
    private String creationTime;
    /**
     * 最后更新时间
     */
    private String lastUpdateTime;
    /**
     * 签到时间
     */
    private String checkInTime;
    /**
     * 签退时间
     */
    private String checkOutTime;
    /**
     * 考勤日期
     */
    private String attendanceDate;
    /**
     * 规定上班时间
     */
    private String startWorkTime;
    /**
     * 规定下班时间
     */
    private String endWorkTime;
    /**
     * 状态-正常, 出差，加班，请假，调休
     */
    private String status;
    /**
     * 是否迟到
     */
    private Boolean isComeLate;
    /**
     * 是否早退
     */
    private Boolean isLeaveEarly;
    /**
     * 备注
     */
    private String remark;
    /**
     * 加班时间，以分钟为单位
     */
    private Integer overtime;
    /**
     * 是否工作日,（法定节假日调休，如果周六日上班，也算是工作日）
     */
    private Boolean isWorkday;
    /**
     * 部门确认状态，未提交，确认中，已确认
     */
    private String validationStatus;
    /**
     * 迟到分钟数
     */
    private Integer comeLateMinute;
    /**
     * 早退分钟数
     */
    private Integer leaveEarlyMinute;
    /**
     * 旷工天数
     */
    private Double absenteeism;
    /**
     * 旷工类型
     */
    private String absenteeismType;
    /**
     * 请假类型
     */
    private String leaveType;
    /**
     * 请假天数
     */
    private Double leaveDays;
    /**
     * 加班小时数
     */
    private Double overtimeHour;
    /**
     * 加班调休天数
     */
    private Double overtimeDaysoff;
    /**
     * 出差天数
     */
    private Double evectionDays;
    /**
     * 外出小时数
     */
    private Double goOutHours;
    /**
     * 年假天数
     */
    private Double annualLeaveDays;

    /**
     * 签到纬度，经度，图片，地址
     */
    private double latitudeSignin;
    private double longitudeSignin;
    private String picSignin;
    private String addressSignin;

    /**
     * 签退纬度，经度，图片，地址
     */

    private double latitudeSignout;
    private double longitudeSignout;
    private String picSignout;
    private String addressSignout;
    /**
     * 迟到原因
     */
    private String comeLateReason;
    /**
     * 早退原因
     */
    private String leaveEarlyReason;

    /**
     * 外出签到纬度，经度，图片，地址（可以多次）
     */

    private double latitudePin;
    private double longitudePin;
    private String picSignPin;
    private String addressPin;
    private String address;
    private String createTime;
    private String staffId;
    private String picPin;
    private String pic;
    private boolean isSignOut; // 是否外出定位

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public double getLatitudePin() {
        return latitudePin;
    }

    public void setLatitudePin(double latitudePin) {
        this.latitudePin = latitudePin;
    }

    public double getLongitudePin() {
        return longitudePin;
    }

    public void setLongitudePin(double longitudePin) {
        this.longitudePin = longitudePin;
    }

    public boolean isSignOut() {
        return isSignOut;
    }

    public void setSignOut(boolean signOut) {
        isSignOut = signOut;
    }

    public String getComeLateReason() {
        return comeLateReason;
    }

    public void setComeLateReason(String comeLateReason) {
        this.comeLateReason = comeLateReason;
    }

    public String getLeaveEarlyReason() {
        return leaveEarlyReason;
    }

    public void setLeaveEarlyReason(String leaveEarlyReason) {
        this.leaveEarlyReason = leaveEarlyReason;
    }

    public String getUuid() {
        return uuid;
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

    public String getCreationDepartmentId() {
        return creationDepartmentId;
    }

    public void setCreationDepartmentId(String creationDepartmentId) {
        this.creationDepartmentId = creationDepartmentId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStartWorkTime() {
        return startWorkTime;
    }

    public void setStartWorkTime(String startWorkTime) {
        this.startWorkTime = startWorkTime;
    }

    public String getEndWorkTime() {
        return endWorkTime;
    }

    public void setEndWorkTime(String endWorkTime) {
        this.endWorkTime = endWorkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getComeLate() {
        return isComeLate;
    }

    public void setComeLate(Boolean comeLate) {
        isComeLate = comeLate;
    }

    public Boolean getLeaveEarly() {
        return isLeaveEarly;
    }

    public void setLeaveEarly(Boolean leaveEarly) {
        isLeaveEarly = leaveEarly;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOvertime() {
        return overtime;
    }

    public void setOvertime(Integer overtime) {
        this.overtime = overtime;
    }

    public Boolean getWorkday() {
        return isWorkday;
    }

    public void setWorkday(Boolean workday) {
        isWorkday = workday;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public Integer getComeLateMinute() {
        return comeLateMinute;
    }

    public void setComeLateMinute(Integer comeLateMinute) {
        this.comeLateMinute = comeLateMinute;
    }

    public Integer getLeaveEarlyMinute() {
        return leaveEarlyMinute;
    }

    public void setLeaveEarlyMinute(Integer leaveEarlyMinute) {
        this.leaveEarlyMinute = leaveEarlyMinute;
    }

    public Double getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(Double absenteeism) {
        this.absenteeism = absenteeism;
    }

    public String getAbsenteeismType() {
        return absenteeismType;
    }

    public void setAbsenteeismType(String absenteeismType) {
        this.absenteeismType = absenteeismType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Double getOvertimeHour() {
        return overtimeHour;
    }

    public void setOvertimeHour(Double overtimeHour) {
        this.overtimeHour = overtimeHour;
    }

    public Double getOvertimeDaysoff() {
        return overtimeDaysoff;
    }

    public void setOvertimeDaysoff(Double overtimeDaysoff) {
        this.overtimeDaysoff = overtimeDaysoff;
    }

    public Double getEvectionDays() {
        return evectionDays;
    }

    public void setEvectionDays(Double evectionDays) {
        this.evectionDays = evectionDays;
    }

    public Double getGoOutHours() {
        return goOutHours;
    }

    public void setGoOutHours(Double goOutHours) {
        this.goOutHours = goOutHours;
    }

    public Double getAnnualLeaveDays() {
        return annualLeaveDays;
    }

    public void setAnnualLeaveDays(Double annualLeaveDays) {
        this.annualLeaveDays = annualLeaveDays;
    }

    public double getLatitudeSignin() {
        return latitudeSignin;
    }

    public void setLatitudeSignin(double latitudeSignin) {
        this.latitudeSignin = latitudeSignin;
    }

    public double getLongitudeSignin() {
        return longitudeSignin;
    }

    public void setLongitudeSignin(double longitudeSignin) {
        this.longitudeSignin = longitudeSignin;
    }

    public String getPicSignin() {
        return picSignin;
    }

    public void setPicSignin(String picSignin) {
        this.picSignin = picSignin;
    }

    public String getAddressSignin() {
        return addressSignin;
    }

    public void setAddressSignin(String addressSignin) {
        this.addressSignin = addressSignin;
    }

    public double getLatitudeSignout() {
        return latitudeSignout;
    }

    public void setLatitudeSignout(double latitudeSignout) {
        this.latitudeSignout = latitudeSignout;
    }

    public double getLongitudeSignout() {
        return longitudeSignout;
    }

    public void setLongitudeSignout(double longitudeSignout) {
        this.longitudeSignout = longitudeSignout;
    }

    public String getPicSignout() {
        return picSignout;
    }

    public void setPicSignout(String picSignout) {
        this.picSignout = picSignout;
    }

    public String getAddressSignout() {
        return addressSignout;
    }

    public void setAddressSignout(String addressSignout) {
        this.addressSignout = addressSignout;
    }




    public String getPicSignPin() {
        return picSignPin;
    }

    public void setPicSignPin(String picSignPin) {
        this.picSignPin = picSignPin;
    }

    public String getAddressPin() {
        return addressPin;
    }

    public void setAddressPin(String addressPin) {
        this.addressPin = addressPin;
    }

    public String getPicPin() {
        return picPin;
    }

    public void setPicPin(String picPin) {
        this.picPin = picPin;
    }
}
