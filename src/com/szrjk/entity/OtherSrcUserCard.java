package com.szrjk.entity;

public class OtherSrcUserCard {

	private String company;
	private String department;
	private String professionalTitle;
	private String userFaceUrl;
	private String userName;
	private String userSeqId;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getProfessionalTitle() {
		return professionalTitle;
	}
	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}
	public String getUserFaceUrl() {
		return userFaceUrl;
	}
	public void setUserFaceUrl(String userFaceUrl) {
		this.userFaceUrl = userFaceUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	@Override
	public String toString() {
		return "OtherSrcUserCard [company=" + company + ", department="
				+ department + ", professionalTitle=" + professionalTitle
				+ ", userFaceUrl=" + userFaceUrl + ", userName=" + userName
				+ ", userSeqId=" + userSeqId + "]";
	}
	
	
}
