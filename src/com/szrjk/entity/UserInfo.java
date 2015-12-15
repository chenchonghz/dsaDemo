package com.szrjk.entity;

import java.io.Serializable;

/***
 *         "backgroundUrl": "http://dd-feed.oss-cn-shenzhen.aliyuncs.com/wall/b2e72544322a8cae.png",
 "deptName": "外分泌",
 "entrySchoolDate": "",
 "phone": "12345678963",
 "isComplete": "false",
 "sex": "2",
 "cityCode": "2103",
 "companyName": "中国核工业北京四〇一医院",
 "mediaType": "",
 "userType": "2",
 "userSeqId": 100812,
 "userFaceUrl": "http://dman-face.oss-cn-shenzhen.aliyuncs.com/face/touxiang.jpg",
 "educationLevel": "无",
 "userLevel": "0",
 "birthdate": "",
 "professionalTitle": "",
 "province": "210",
 "userName": "门卫"
 */
public class UserInfo implements Serializable
{
	// 用户专业资料状态 1、有;2 、没有;
	private String profInfoSts;
	//
	private String pwdPrivate;
	// 资料是否完成
	private boolean isComplete;
	// 电话号码
	private String phone;
	// 性别
	private String sex;
	//用户类型
	private String userType;
	//用户生日
	private String birthday;
	//省份
	private String province;
	//城市
	private String cityCode;
	private String companyId;
	private String companyName;
	private String deptId;
	private String deptName;
	private String educationLevel;
	private String entrySchoolDate;
	private String mediaType;
	private String professionalTitle;
	private String faceUrl;
	private String backgroundUrl;
	//认证级别
	private String userLevel;
	//密码
	private String password;
	// 用户ID
	private String userSeqId;
	private String pwdSts;
	//用户头像
	private String userFaceUrl;
	private String authAccount;
	// 用户名称
	private String userName;
	public String getProfInfoSts() {
		return profInfoSts;
	}
	public void setProfInfoSts(String profInfoSts) {
		this.profInfoSts = profInfoSts;
	}
	public String getPwdPrivate() {
		return pwdPrivate;
	}
	public void setPwdPrivate(String pwdPrivate) {
		this.pwdPrivate = pwdPrivate;
	}
	public boolean isComplete() {
		return isComplete;
	}
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
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
	public String getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(String educationLevel) {
		this.educationLevel = educationLevel;
	}
	public String getEntrySchoolDate() {
		return entrySchoolDate;
	}
	public void setEntrySchoolDate(String entrySchoolDate) {
		this.entrySchoolDate = entrySchoolDate;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getProfessionalTitle() {
		return professionalTitle;
	}
	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}
	public String getFaceUrl() {
		return faceUrl;
	}
	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getPwdSts() {
		return pwdSts;
	}
	public void setPwdSts(String pwdSts) {
		this.pwdSts = pwdSts;
	}
	public String getUserFaceUrl() {
		return userFaceUrl;
	}
	public void setUserFaceUrl(String userFaceUrl) {
		this.userFaceUrl = userFaceUrl;
	}
	public String getAuthAccount() {
		return authAccount;
	}
	public void setAuthAccount(String authAccount) {
		this.authAccount = authAccount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	@Override
	public String toString() {
		return "UserInfo [profInfoSts=" + profInfoSts + ", pwdPrivate="
				+ pwdPrivate + ", isComplete=" + isComplete + ", phone="
				+ phone + ", sex=" + sex + ", userType=" + userType
				+ ", birthday=" + birthday + ", province=" + province
				+ ", cityCode=" + cityCode + ", companyId=" + companyId
				+ ", companyName=" + companyName + ", deptId=" + deptId
				+ ", deptName=" + deptName + ", educationLevel="
				+ educationLevel + ", entrySchoolDate=" + entrySchoolDate
				+ ", mediaType=" + mediaType + ", professionalTitle="
				+ professionalTitle + ", faceUrl=" + faceUrl
				+ ", backgroundUrl=" + backgroundUrl + ", userLevel="
				+ userLevel + ", password=" + password + ", userSeqId="
				+ userSeqId + ", pwdSts=" + pwdSts + ", userFaceUrl="
				+ userFaceUrl + ", authAccount=" + authAccount + ", userName="
				+ userName + "]";
	}
	

	
}
