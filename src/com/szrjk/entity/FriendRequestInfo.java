package com.szrjk.entity;

public class FriendRequestInfo {
	private String userSeqId;
	private String userFaceUrl;
	private String department;
	private String company;
	private String professionalTitle;
	private String userLevel;
	private String userName;
	private String userType;
	public FriendRequestInfo() {
		super();
	}
	public FriendRequestInfo(String userSeqId, String userFaceUrl,
			String department, String company, String professionalTitle,
			String userLevel, String userName, String userType) {
		super();
		this.userSeqId = userSeqId;
		this.userFaceUrl = userFaceUrl;
		this.department = department;
		this.company = company;
		this.professionalTitle = professionalTitle;
		this.userLevel = userLevel;
		this.userName = userName;
		this.userType = userType;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProfessionalTitle() {
		return professionalTitle;
	}
	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
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
		return "FriendRequestInfo [userSeqId=" + userSeqId + ", userFaceUrl="
				+ userFaceUrl + ", department=" + department + ", company="
				+ company + ", professionalTitle=" + professionalTitle
				+ ", userLevel=" + userLevel + ", userName=" + userName
				+ ", userType=" + userType + "]";
	}
}
