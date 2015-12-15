package com.szrjk.entity;

public class PostBody {

	private String createDate;
	private String postContent;
	private String postPicUrlList;
	public PostBody() {
		super();
	}
	public PostBody(String createDate,String postContent, String postPicUrlList) {
		super();
		this.createDate=createDate;
		this.postContent = postContent;
		this.postPicUrlList = postPicUrlList;
	}
	
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public String getpostPicUrlList() {
		return postPicUrlList;
	}
	public void setpostPicUrlList(String postPicUrlList) {
		this.postPicUrlList = postPicUrlList;
	}
	@Override
	public String toString() {
		return "PostBody [createDate=" + createDate + ", postContent="
				+ postContent + ", postPicUrlList=" + postPicUrlList + "]";
	}
	
	
	
}
