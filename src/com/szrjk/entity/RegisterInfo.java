package com.szrjk.entity;


/**
 * debggm
 * 注册参数类
 */
public class RegisterInfo{


    private String userName;
    private String faceUrl;
    /**
     * 用户类别  0:未知类型(默认) 1:管理员 2:医生   3:学生 4:药企从业人员  5:媒体从业人员  6:团体 7:其他 8:护士 9:药剂师 
     */
    private String userType;
    /**
     * 用户性别 1: 男  2:女
     */
    private String sex;
    private String birthdate;
    private String province;
    private String city;
    private String deptId;
    private String deptName;
    /**
     * 学历0:无 1:小学, 2:初中,  3:中专/高中,  4:中专/高中  5:专科 6:学士 7:硕士研究生 8:博士研究生 9:博士后
     */
    private int educationLev;
    private String entrySchoolDate;
    private String companyId;
    private String companyName;
    /**
     * 职称 0无职称(待定) 1.主任医师，2副主任医师，3.主治医师，4.医师, 5住院医师, 6医士,7 助理医师，8教授，9副教授  10讲师，11助教
     */
    private String professionalTitle;
    private String jobTitle;
    /**媒体类型
     1:期刊,  2:报纸  3:网络**/
    private String mediaType ;
    /**
     * 用户等级V的等级 0:未认证(默认) 1已经认证(加V)
     */
    private String userLev;
    private String phone;

    /**
     *  密码，DES加密的密码
     *  DesUtil.enString(pwd)
     * **/
    private String passwd;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
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

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getEducationLev() {
        return educationLev;
    }

    public void setEducationLev(int index) {
        this.educationLev = index;
    }

    public String getEntrySchoolDate() {
        return entrySchoolDate;
    }

    public void setEntrySchoolDate(String entrySchoolDate) {
        this.entrySchoolDate = entrySchoolDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }

    
    public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUserLev() {
        return userLev;
    }

    public void setUserLev(String userLev) {
        this.userLev = userLev;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

	@Override
	public String toString() {
		return "RegisterInfo [userName=" + userName + ", faceUrl=" + faceUrl
				+ ", userType=" + userType + ", sex=" + sex + ", birthdate="
				+ birthdate + ", province=" + province + ", city=" + city
				+ ", deptId=" + deptId + ", deptName=" + deptName
				+ ", educationLev=" + educationLev + ", entrySchoolDate="
				+ entrySchoolDate + ", companyId=" + companyId
				+ ", companyName=" + companyName + ", professionalTitle="
				+ professionalTitle + ", mediaType=" + mediaType + ", userLev="
				+ userLev + ", phone=" + phone + ", passwd=" + passwd + "]";
	}
    
    
}
