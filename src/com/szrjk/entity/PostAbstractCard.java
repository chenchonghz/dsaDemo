package com.szrjk.entity;

public class PostAbstractCard {

	private String content;
	private String userSeqId;
	private String postType;
	private String createDate;
	private String picList;
	private String postId;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPicList() {
		return picList;
	}
	public void setPicList(String picList) {
		this.picList = picList;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	@Override
	public String toString() {
		return "PostAbstractCard [content=" + content + ", userSeqId="
				+ userSeqId + ", postType=" + postType + ", createDate="
				+ createDate + ", picList=" + picList + ", postId=" + postId
				+ "]";
	}
	
	
}
