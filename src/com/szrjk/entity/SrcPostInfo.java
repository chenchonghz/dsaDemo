package com.szrjk.entity;

import java.util.Arrays;

/**
 * 转发帖子帖子信息实体类
 * @author liyi
 *
 */
public class SrcPostInfo {

	private String userSeqId;
	private String backgroundPic;
	private String postType;
	private String deptIds;
	private String postTitle;
	private String createDate;
	private String postId;
	private String deptNames;
	private String completeRate;
	private String content;
	private String picList;
	private String[] picsList;
	private String coterieName;
	private String coterieId;
	
	
	
	public String getCoterieName() {
		return coterieName;
	}
	public void setCoterieName(String coterieName) {
		this.coterieName = coterieName;
	}
	public String getCoterieId() {
		return coterieId;
	}
	public void setCoterieId(String coterieId) {
		this.coterieId = coterieId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getPicList() {
		return picsList;
	}
	public void setPicList(String picList) {
		this.picList = picList;
		picsList = picList.split("\\|");
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getBackgroundPic() {
		return backgroundPic;
	}
	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getDeptNames() {
		return deptNames;
	}
	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}
	public String getCompleteRate() {
		return completeRate;
	}
	public void setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
	}
	@Override
	public String toString() {
		return "SrcPostInfo [userSeqId=" + userSeqId + ", backgroundPic="
				+ backgroundPic + ", postType=" + postType + ", deptIds="
				+ deptIds + ", postTitle=" + postTitle + ", createDate="
				+ createDate + ", postId=" + postId + ", deptNames="
				+ deptNames + ", completeRate=" + completeRate + ", content="
				+ content + ", picList=" + picList + ", picsList="
				+ Arrays.toString(picsList) + ", coterieName=" + coterieName
				+ ", coterieId=" + coterieId + "]";
	}
	
	

	
	
}
