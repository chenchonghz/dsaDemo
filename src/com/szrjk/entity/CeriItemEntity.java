package com.szrjk.entity;

public class CeriItemEntity {

	private String certName;
	private String opTime;
	private int batchNumber ;
	private int certType ;
	private int verifyStatus ;
	private int userSeqId ;
	private String verifyTime;
	private String certId;
	private String createDate;
	private String certPicUrl;
	private int certAppId;
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public int getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}
	public int getCertType() {
		return certType;
	}
	public void setCertType(int certType) {
		this.certType = certType;
	}
	public int getVerifyStatus() {
		return verifyStatus;
	}
	public void setVerifyStatus(int verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	public int getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(int userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}
	public String getCertId() {
		return certId;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCertPicUrl() {
		return certPicUrl;
	}
	public void setCertPicUrl(String certPicUrl) {
		this.certPicUrl = certPicUrl;
	}
	public int getCertAppId() {
		return certAppId;
	}
	public void setCertAppId(int certAppId) {
		this.certAppId = certAppId;
	}
	@Override
	public String toString() {
		return "CeriItemEntity [certName=" + certName + ", opTime=" + opTime
				+ ", batchNumber=" + batchNumber + ", certType=" + certType
				+ ", verifyStatus=" + verifyStatus + ", userSeqId=" + userSeqId
				+ ", verifyTime=" + verifyTime + ", certId=" + certId
				+ ", createDate=" + createDate + ", certPicUrl=" + certPicUrl
				+ ", certAppId=" + certAppId + "]";
	}
	
	
	
}
