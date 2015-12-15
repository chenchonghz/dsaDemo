package com.szrjk.entity;


/**相册*/
public class PhotoAlbum {

	
	private String month;
	private String num;
	private String picUrls;
	
	
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPicUrls() {
		return picUrls;
	}
	public void setPicUrls(String picUrls) {
		this.picUrls = picUrls;
	}
	@Override
	public String toString() {
		return "PhotoAlbum [month=" + month + ", num=" + num + ", picUrls="
				+ picUrls + "]";
	}
}
