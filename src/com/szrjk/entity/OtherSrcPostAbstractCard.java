package com.szrjk.entity;

public class OtherSrcPostAbstractCard {

	private String backgroundPic;
	private String completeRate;
	private String createDate;
	private String deptIds;
	private String deptNames;
	private String postId;
	private String postTitle;
	private String postType;
	private String userSeqId;
	public String getBackgroundPic() {
		return backgroundPic;
	}
	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}
	public String getCompleteRate() {
		return completeRate;
	}
	public void setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getDeptNames() {
		return deptNames;
	}
	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	@Override
	public String toString() {
		return "OtherSrcPostAbstractCard [backgroundPic=" + backgroundPic
				+ ", completeRate=" + completeRate + ", createDate="
				+ createDate + ", deptIds=" + deptIds + ", deptNames="
				+ deptNames + ", postId=" + postId + ", postTitle=" + postTitle
				+ ", postType=" + postType + ", userSeqId=" + userSeqId + "]";
	}
	
	
}
