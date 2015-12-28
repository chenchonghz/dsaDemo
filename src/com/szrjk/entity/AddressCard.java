package com.szrjk.entity;

import java.io.Serializable;


/**
 * 用户名片实体类
 */
public class AddressCard implements Serializable{

	//Id
	private String userSeqId;
	//用户头像地址
	private String userFaceUrl;
	//部门
	private String deptName;
	//单位
	private String companyName;
	//职称
	private String professionalTitle;
	//用户名字
	private String userName;
	//用户类型
	private String userType;
	//用户level
	private String userLevel;
	//是否已关注
	private boolean isFocus;
	
	//增加排序字母
	private String sortLetters;
		
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public boolean isFocus() {
		return isFocus;
	}
	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public AddressCard() {
		super();
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
	@Override
	public String toString() {
		return "AddressCard [userSeqId=" + userSeqId + ", userFaceUrl="
				+ userFaceUrl + ", deptName=" + deptName + ", companyName="
				+ companyName + ", professionalTitle=" + professionalTitle
				+ ", userName=" + userName + ", userType=" + userType
				+ ", userLevel=" + userLevel + ", isFocus=" + isFocus
				+ ", sortLetters=" + sortLetters + "]";
	}

	

	

	
	
	
}
