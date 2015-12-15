package com.szrjk.entity;

import java.util.List;

public class AuthencitionEntity {

	private String userSeqId;//用户ID
	private List<PicItemEntity> picListOut;
	private List<CeriItemEntity> certListOut;
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public List<PicItemEntity> getPicListOut() {
		return picListOut;
	}
	public void setPicListOut(List<PicItemEntity> picListOut) {
		this.picListOut = picListOut;
	}
	public List<CeriItemEntity> getCertListOut() {
		return certListOut;
	}
	public void setCertListOut(List<CeriItemEntity> certListOut) {
		this.certListOut = certListOut;
	}
	@Override
	public String toString() {
		return "AuthencitionEntity [userSeqId=" + userSeqId + ", picListOut="
				+ picListOut + ", certListOut=" + certListOut + "]";
	}
	
	
	
}
