package com.szrjk.entity;

public class PicItemEntity {

	private String picUrl;
	private String picid;
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPicid() {
		return picid;
	}
	public void setPicid(String picid) {
		this.picid = picid;
	}
	@Override
	public String toString() {
		return "PicItemEntity [picUrl=" + picUrl + ", picid=" + picid + "]";
	}
	
}
