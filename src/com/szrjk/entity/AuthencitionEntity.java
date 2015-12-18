package com.szrjk.entity;

import java.util.List;

public class AuthencitionEntity {

	private List<PicItemEntity> picListOut;
	private List<CeriItemEntity> certListOut;
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
		return "AuthencitionEntity [picListOut=" + picListOut
				+ ", certListOut=" + certListOut + "]";
	}
	
	
	
	
}
