package com.szrjk.entity;

/**相册*/
public class PhotoAlbum2 {

	private String postId;
	private int postType;
	private String PicUrl;
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public int getPostType() {
		return postType;
	}
	public void setPostType(int postType) {
		this.postType = postType;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	@Override
	public String toString() {
		return "PhotoAlbum2 [postId=" + postId + ", postType=" + postType
				+ ", PicUrl=" + PicUrl + "]";
	}
	
	
}
