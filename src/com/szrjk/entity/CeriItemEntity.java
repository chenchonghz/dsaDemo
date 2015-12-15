package com.szrjk.entity;

public class CeriItemEntity {

	private String certName;
	private int certId;
	private int certType ;
	private int certSts ;
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public int getCertId() {
		return certId;
	}
	public void setCertId(int certId) {
		this.certId = certId;
	}
	public int getCertType() {
		return certType;
	}
	public void setCertType(int certType) {
		this.certType = certType;
	}
	public int getCertSts() {
		return certSts;
	}
	public void setCertSts(int certSts) {
		this.certSts = certSts;
	}
	@Override
	public String toString() {
		return "CeriItemEntity [certName=" + certName + ", certId=" + certId
				+ ", certType=" + certType + ", certSts=" + certSts + "]";
	}
	
}
