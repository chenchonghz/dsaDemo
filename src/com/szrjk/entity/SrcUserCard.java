package com.szrjk.entity;

/**
 * 转发帖子用户信息实体类
 * @author liyi
 *
 */
public class SrcUserCard {

	private String userSeqId;
	private String userFaceUrl;
	private String deptName;
	private String companyName;
	private String professionalTitle;
	private String userName;
	private String userType;
	private String userLevel;
	
	
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getUserFaceUrl() {
		return userFaceUrl;
	}
	public void setUserFaceUrl(String userFaceUrl) {
		this.userFaceUrl = userFaceUrl;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	@Override
	public String toString() {
		return "SrcUserCard [userSeqId=" + userSeqId + ", userFaceUrl="
				+ userFaceUrl + ", deptName=" + deptName + ", companyName="
				+ companyName + ", professionalTitle=" + professionalTitle
				+ ", userName=" + userName + ", userType=" + userType
				+ ", userLevel=" + userLevel + "]";
	}
	
}
