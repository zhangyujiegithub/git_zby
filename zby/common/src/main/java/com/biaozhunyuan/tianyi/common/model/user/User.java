package com.biaozhunyuan.tianyi.common.model.user;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by 王安民 on 2017/8/28.
 * 员工实体类
 */

public class User implements Serializable {

    @DatabaseField(generatedId = true, unique = true)
    public int _id;// 本地数据库的编号
    @DatabaseField
    private String creatorId;//创建人
    @DatabaseField
    private String corpId;//企业id
    @DatabaseField
    private String creatorDepartmentId;//创建部门
    @DatabaseField
    private String createTime;//创建时间
    @DatabaseField
    private String lastUpdateTime;//最后修改时间
    @DatabaseField
    public String name;//姓名
    @DatabaseField
    public String password;//密码
    @DatabaseField
    private String staffNumber;//员工号
    @DatabaseField
    private String areaId;//区域Id
    @DatabaseField
    private String departmentId;//部门
    private String departmentName;//部门
    @DatabaseField
    private String city;//城市
    @DatabaseField
    private String province;//省
    @DatabaseField
    private String post;//岗位
    @DatabaseField
    private String postCategory;//岗位序列
    @DatabaseField
    private String rank;//职级
    @DatabaseField
    private String salaryRank;//薪资职级
    @DatabaseField
    private String directSupervisor;//直属上级
    @DatabaseField
    private String entryDate;//入职时间
    @DatabaseField
    private String leaveDate;//离职时间
    @DatabaseField
    private String status;//人员状态:转正,试用
    @DatabaseField
    private String staffStatus;//on在职,off离职
    @DatabaseField
    private String mobile;//手机号码
    @DatabaseField
    private String enterpriseMailbox;//企业邮箱
    @DatabaseField
    private String personalMailbox;//个人邮箱
    @DatabaseField
    private String nationality;//国籍
    @DatabaseField
    private String passType;//证件类型
    @DatabaseField
    private String passNumber;//证件号码
    @DatabaseField
    private Boolean identityCardCheck;//身份证确认
    @DatabaseField
    private Integer age;//年龄
    @DatabaseField
    private String gender;//性别
    @DatabaseField
    private String nation;//民族
    @DatabaseField
    private String maritalStatus;//婚姻状况-已婚已育，已婚未育
    @DatabaseField
    private String children; //子女状况
    @DatabaseField
    private String birthplace;//籍贯
    @DatabaseField
    private String politicalStatus;//政治面貌
    @DatabaseField
    private String birthday;//出生日期
    @DatabaseField
    private String birthdayMonth;//出生月份
    @DatabaseField
    private String graduateSchool;//毕业院校
    @DatabaseField
    private String specialty;//专业
    @DatabaseField
    private String highestEducation;//最高学历
    @DatabaseField
    private String graduationTime;//毕业时间
    @DatabaseField
    private String beginWorkTime;//参加工作时间
    @DatabaseField
    private String certificate;//纸质证书
    @DatabaseField
    private String trainingExperience;//培训经历
    @DatabaseField
    private String disciplinaryRecords;//奖惩记录
    @DatabaseField
    private String recruitmentChannel;//招聘渠道
    @DatabaseField
    private String referrer;//推荐人
    @DatabaseField
    private Boolean isPearlAwards;//是否伯乐奖
    @DatabaseField
    private String recruiter;//招聘人
    @DatabaseField
    private String householdCategory;//户口性质
    @DatabaseField
    private String householdPlace;//户口所在地
    @DatabaseField
    private String address;//现居住地址
    @DatabaseField
    private String emergencyContactName;//紧急联系人姓名
    @DatabaseField
    private String emergencyContactRelatioin;//紧急联系人关系
    @DatabaseField
    private String emergencyContactPhone;//紧急联系人电话
    @DatabaseField
    private String contractCategory;//合同类型
    @DatabaseField
    private String contractExpiryDate;//合同到期日
    @DatabaseField
    private String probationMonths;//试用月份
    @DatabaseField
    private String positiveDates;//转正日期
    @DatabaseField
    private String firestContractDate;//第一次签订劳动合同日期
    @DatabaseField
    private String firestContractExpiryDate;//第一次签订劳动合同到期
    @DatabaseField
    private String secondContractDate;//第二次签订劳动合同的日期
    @DatabaseField
    private String secondContractExpiryDate;//第二次签订劳动合同到期
    @DatabaseField
    private String thirdContractDate;//第三次签订劳动合同日期
    @DatabaseField
    private String bankCardName;//银行卡开户姓名
    @DatabaseField
    private String bankCardNumber;//银行卡卡号
    @DatabaseField
    private String openingBank;//开户行
    @DatabaseField
    private String socialInsuranceInfo;//社保信息
    @DatabaseField
    private String accumulationFundInfo;//公积金信息
    @DatabaseField
    private Boolean hasStaffInfoForm;//是否有员工信息登记表
    @DatabaseField
    private Boolean hasEmploymentLetter;//是否有聘用函
    @DatabaseField
    private Boolean hasResume;//是否有个人简历
    @DatabaseField
    private Boolean hasCopyIdentityCard;//是否有身份证复印件
    @DatabaseField
    private Boolean hasSchoolCertificate;//是否有学历证明
    @DatabaseField
    private Boolean hasInchPhoto;//是否有1寸照片
    @DatabaseField
    private Boolean hasCopyhousehold;//户口复印件
    @DatabaseField
    private Boolean hasDimissionCertification;//是否有离职证明
    @DatabaseField
    private Boolean hasCopyBankCord;//是否有银行卡复印件
    @DatabaseField
    private Boolean hasCheckupReporting;//是否有体检报告
    @DatabaseField
    private Boolean hasLabourContract;//是否有劳动合同
    @DatabaseField
    private Boolean hasSecrecyAgreement;//是否有保密协议
    @DatabaseField
    private Boolean hasNotification;//是否有告知书
    @DatabaseField
    private Boolean hasBackgroundCheckAuthorization;//是否有背景调查授权书
    @DatabaseField
    private Boolean hasSOPAndSIC;//是否有SOP&SIC
    @DatabaseField
    private String payCutDate;//截薪日期
    @DatabaseField
    private Boolean isDeparture;//是否办理离职手续
    @DatabaseField
    private String fiveInsurancesAndHousingFundStopMonth;//五险一金停交月份
    @DatabaseField
    private String departureDate;//离职手续办理日期
    @DatabaseField
    private String departureCategory;
    @DatabaseField
    private String departureReason;//离职原因
    @DatabaseField
    private Boolean isCanHireAgain;//是否考虑再次录用
    @DatabaseField
    private String remark;//备注
    @DatabaseField
    private Boolean isDelete;//删除
    @DatabaseField
    private Boolean isEnable;//停用
    @DatabaseField
    private String avatar;//头像
    @DatabaseField
    private String branchLead;//分管领导
    @DatabaseField
    private String directSuperior;//直属上级
    @DatabaseField
    private String departmentLead;//部门领导
    @DatabaseField
    private String telephone;//座机
    @DatabaseField
    private String phoneExt;//其他电话
    @DatabaseField
    private String email;//邮箱
    @DatabaseField
    private Boolean isAdmin;//是否管理员
    @DatabaseField
    private String salt;//加密key
    @DatabaseField
    private Integer loginCount;//登录次数
    @DatabaseField
    private String lastLoginTime;//最后一次登录时间
    @DatabaseField
    private Boolean isAuth;//用户是否进行认证
    @DatabaseField
    private String authTime;//认证时间
    @DatabaseField
    private Integer originDataId;//在原库中的编号
    @DatabaseField
    private String deviceModel;//设备型号
    @DatabaseField
    private Integer sort;//排序
    @DatabaseField
    private String expectedPositiveTime;//预计转正时间
    @DatabaseField
    private String evalType;//考核标准
    @DatabaseField
    private String uuid; //id
    @DatabaseField
    public int Department;// 员工所属部门的编号
    public String Id;// 服务器数据库中的编号
    @DatabaseField
    public String enterpriseName;//企业名
    public String contractName;//用户账号

    private boolean selected = false;//是否被选中,选择员工时使用,默认值是未被选中
    private boolean clickable = true; //是否可以点击，默认可点击


    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String Passport;

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setPassport(String passport) {
        Passport = passport;
    }

    public String getPassport() {
        return Passport;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setDepartment(int department) {
        Department = department;
    }

    public int getDepartment() {
        return Department;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorDepartmentId() {
        return creatorDepartmentId;
    }

    public void setCreatorDepartmentId(String creatorDepartmentId) {
        this.creatorDepartmentId = creatorDepartmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSalaryRank() {
        return salaryRank;
    }

    public void setSalaryRank(String salaryRank) {
        this.salaryRank = salaryRank;
    }

    public String getDirectSupervisor() {
        return directSupervisor;
    }

    public void setDirectSupervisor(String directSupervisor) {
        this.directSupervisor = directSupervisor;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStaffStatus() {
        return staffStatus;
    }

    public void setStaffStatus(String staffStatus) {
        this.staffStatus = staffStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEnterpriseMailbox() {
        return enterpriseMailbox;
    }

    public void setEnterpriseMailbox(String enterpriseMailbox) {
        this.enterpriseMailbox = enterpriseMailbox;
    }

    public String getPersonalMailbox() {
        return personalMailbox;
    }

    public void setPersonalMailbox(String personalMailbox) {
        this.personalMailbox = personalMailbox;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public String getPassNumber() {
        return passNumber;
    }

    public void setPassNumber(String passNumber) {
        this.passNumber = passNumber;
    }

    public Boolean getIdentityCardCheck() {
        return identityCardCheck;
    }

    public void setIdentityCardCheck(Boolean identityCardCheck) {
        this.identityCardCheck = identityCardCheck;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    public String getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(String graduationTime) {
        this.graduationTime = graduationTime;
    }

    public String getBeginWorkTime() {
        return beginWorkTime;
    }

    public void setBeginWorkTime(String beginWorkTime) {
        this.beginWorkTime = beginWorkTime;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getTrainingExperience() {
        return trainingExperience;
    }

    public void setTrainingExperience(String trainingExperience) {
        this.trainingExperience = trainingExperience;
    }

    public String getDisciplinaryRecords() {
        return disciplinaryRecords;
    }

    public void setDisciplinaryRecords(String disciplinaryRecords) {
        this.disciplinaryRecords = disciplinaryRecords;
    }

    public String getRecruitmentChannel() {
        return recruitmentChannel;
    }

    public void setRecruitmentChannel(String recruitmentChannel) {
        this.recruitmentChannel = recruitmentChannel;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Boolean getPearlAwards() {
        return isPearlAwards;
    }

    public void setPearlAwards(Boolean pearlAwards) {
        isPearlAwards = pearlAwards;
    }

    public String getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(String recruiter) {
        this.recruiter = recruiter;
    }

    public String getHouseholdCategory() {
        return householdCategory;
    }

    public void setHouseholdCategory(String householdCategory) {
        this.householdCategory = householdCategory;
    }

    public String getHouseholdPlace() {
        return householdPlace;
    }

    public void setHouseholdPlace(String householdPlace) {
        this.householdPlace = householdPlace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactRelatioin() {
        return emergencyContactRelatioin;
    }

    public void setEmergencyContactRelatioin(String emergencyContactRelatioin) {
        this.emergencyContactRelatioin = emergencyContactRelatioin;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getContractCategory() {
        return contractCategory;
    }

    public void setContractCategory(String contractCategory) {
        this.contractCategory = contractCategory;
    }

    public String getContractExpiryDate() {
        return contractExpiryDate;
    }

    public void setContractExpiryDate(String contractExpiryDate) {
        this.contractExpiryDate = contractExpiryDate;
    }

    public String getProbationMonths() {
        return probationMonths;
    }

    public void setProbationMonths(String probationMonths) {
        this.probationMonths = probationMonths;
    }

    public String getPositiveDates() {
        return positiveDates;
    }

    public void setPositiveDates(String positiveDates) {
        this.positiveDates = positiveDates;
    }

    public String getFirestContractDate() {
        return firestContractDate;
    }

    public void setFirestContractDate(String firestContractDate) {
        this.firestContractDate = firestContractDate;
    }

    public String getFirestContractExpiryDate() {
        return firestContractExpiryDate;
    }

    public void setFirestContractExpiryDate(String firestContractExpiryDate) {
        this.firestContractExpiryDate = firestContractExpiryDate;
    }

    public String getSecondContractDate() {
        return secondContractDate;
    }

    public void setSecondContractDate(String secondContractDate) {
        this.secondContractDate = secondContractDate;
    }

    public String getSecondContractExpiryDate() {
        return secondContractExpiryDate;
    }

    public void setSecondContractExpiryDate(String secondContractExpiryDate) {
        this.secondContractExpiryDate = secondContractExpiryDate;
    }

    public String getThirdContractDate() {
        return thirdContractDate;
    }

    public void setThirdContractDate(String thirdContractDate) {
        this.thirdContractDate = thirdContractDate;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getSocialInsuranceInfo() {
        return socialInsuranceInfo;
    }

    public void setSocialInsuranceInfo(String socialInsuranceInfo) {
        this.socialInsuranceInfo = socialInsuranceInfo;
    }

    public String getAccumulationFundInfo() {
        return accumulationFundInfo;
    }

    public void setAccumulationFundInfo(String accumulationFundInfo) {
        this.accumulationFundInfo = accumulationFundInfo;
    }

    public Boolean getHasStaffInfoForm() {
        return hasStaffInfoForm;
    }

    public void setHasStaffInfoForm(Boolean hasStaffInfoForm) {
        this.hasStaffInfoForm = hasStaffInfoForm;
    }

    public Boolean getHasEmploymentLetter() {
        return hasEmploymentLetter;
    }

    public void setHasEmploymentLetter(Boolean hasEmploymentLetter) {
        this.hasEmploymentLetter = hasEmploymentLetter;
    }

    public Boolean getHasResume() {
        return hasResume;
    }

    public void setHasResume(Boolean hasResume) {
        this.hasResume = hasResume;
    }

    public Boolean getHasCopyIdentityCard() {
        return hasCopyIdentityCard;
    }

    public void setHasCopyIdentityCard(Boolean hasCopyIdentityCard) {
        this.hasCopyIdentityCard = hasCopyIdentityCard;
    }

    public Boolean getHasSchoolCertificate() {
        return hasSchoolCertificate;
    }

    public void setHasSchoolCertificate(Boolean hasSchoolCertificate) {
        this.hasSchoolCertificate = hasSchoolCertificate;
    }

    public Boolean getHasInchPhoto() {
        return hasInchPhoto;
    }

    public void setHasInchPhoto(Boolean hasInchPhoto) {
        this.hasInchPhoto = hasInchPhoto;
    }

    public Boolean getHasCopyhousehold() {
        return hasCopyhousehold;
    }

    public void setHasCopyhousehold(Boolean hasCopyhousehold) {
        this.hasCopyhousehold = hasCopyhousehold;
    }

    public Boolean getHasDimissionCertification() {
        return hasDimissionCertification;
    }

    public void setHasDimissionCertification(Boolean hasDimissionCertification) {
        this.hasDimissionCertification = hasDimissionCertification;
    }

    public Boolean getHasCopyBankCord() {
        return hasCopyBankCord;
    }

    public void setHasCopyBankCord(Boolean hasCopyBankCord) {
        this.hasCopyBankCord = hasCopyBankCord;
    }

    public Boolean getHasCheckupReporting() {
        return hasCheckupReporting;
    }

    public void setHasCheckupReporting(Boolean hasCheckupReporting) {
        this.hasCheckupReporting = hasCheckupReporting;
    }

    public Boolean getHasLabourContract() {
        return hasLabourContract;
    }

    public void setHasLabourContract(Boolean hasLabourContract) {
        this.hasLabourContract = hasLabourContract;
    }

    public Boolean getHasSecrecyAgreement() {
        return hasSecrecyAgreement;
    }

    public void setHasSecrecyAgreement(Boolean hasSecrecyAgreement) {
        this.hasSecrecyAgreement = hasSecrecyAgreement;
    }

    public Boolean getHasNotification() {
        return hasNotification;
    }

    public void setHasNotification(Boolean hasNotification) {
        this.hasNotification = hasNotification;
    }

    public Boolean getHasBackgroundCheckAuthorization() {
        return hasBackgroundCheckAuthorization;
    }

    public void setHasBackgroundCheckAuthorization(Boolean hasBackgroundCheckAuthorization) {
        this.hasBackgroundCheckAuthorization = hasBackgroundCheckAuthorization;
    }

    public Boolean getHasSOPAndSIC() {
        return hasSOPAndSIC;
    }

    public void setHasSOPAndSIC(Boolean hasSOPAndSIC) {
        this.hasSOPAndSIC = hasSOPAndSIC;
    }

    public String getPayCutDate() {
        return payCutDate;
    }

    public void setPayCutDate(String payCutDate) {
        this.payCutDate = payCutDate;
    }

    public Boolean getDeparture() {
        return isDeparture;
    }

    public void setDeparture(Boolean departure) {
        isDeparture = departure;
    }

    public String getFiveInsurancesAndHousingFundStopMonth() {
        return fiveInsurancesAndHousingFundStopMonth;
    }

    public void setFiveInsurancesAndHousingFundStopMonth(String fiveInsurancesAndHousingFundStopMonth) {
        this.fiveInsurancesAndHousingFundStopMonth = fiveInsurancesAndHousingFundStopMonth;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureCategory() {
        return departureCategory;
    }

    public void setDepartureCategory(String departureCategory) {
        this.departureCategory = departureCategory;
    }

    public String getDepartureReason() {
        return departureReason;
    }

    public void setDepartureReason(String departureReason) {
        this.departureReason = departureReason;
    }

    public Boolean getCanHireAgain() {
        return isCanHireAgain;
    }

    public void setCanHireAgain(Boolean canHireAgain) {
        isCanHireAgain = canHireAgain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBranchLead() {
        return branchLead;
    }

    public void setBranchLead(String branchLead) {
        this.branchLead = branchLead;
    }

    public String getDirectSuperior() {
        return directSuperior;
    }

    public void setDirectSuperior(String directSuperior) {
        this.directSuperior = directSuperior;
    }

    public String getDepartmentLead() {
        return departmentLead;
    }

    public void setDepartmentLead(String departmentLead) {
        this.departmentLead = departmentLead;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public String getAuthTime() {
        return authTime;
    }

    public void setAuthTime(String authTime) {
        this.authTime = authTime;
    }

    public Integer getOriginDataId() {
        return originDataId;
    }

    public void setOriginDataId(Integer originDataId) {
        this.originDataId = originDataId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getExpectedPositiveTime() {
        return expectedPositiveTime;
    }

    public void setExpectedPositiveTime(String expectedPositiveTime) {
        this.expectedPositiveTime = expectedPositiveTime;
    }

    public String getEvalType() {
        return evalType;
    }

    public void setEvalType(String evalType) {
        this.evalType = evalType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private boolean ischeck; //单选框是否选中
    private String st;

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    @Override
    public String toString() {
        return "User{" +
                "createTime='" + createTime + '\'' +
                ", name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", staffStatus='" + staffStatus + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
