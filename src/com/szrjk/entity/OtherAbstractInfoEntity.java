package com.szrjk.entity;

public class OtherAbstractInfoEntity {

	private String content;
	private String createDate;
	private String pPostId;
	private String pUserName;
	private String pUserSeqId;
	private String postId;
	private String postLevel;
	private String postType;
	private boolean isMineLike;
	
	public boolean isMineLike() {
		return isMineLike;
	}
	public void setMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}
	private OtherSrcPostAbstractCard srcPostAbstractCard;
	
	private String srcPostId;
	private OtherSrcUserCard srcUserCard;
	private String userSeqId;
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getpPostId() {
		return pPostId;
	}
	public void setpPostId(String pPostId) {
		this.pPostId = pPostId;
	}
	public String getpUserName() {
		return pUserName;
	}
	public void setpUserName(String pUserName) {
		this.pUserName = pUserName;
	}
	public String getpUserSeqId() {
		return pUserSeqId;
	}
	public void setpUserSeqId(String pUserSeqId) {
		this.pUserSeqId = pUserSeqId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public OtherSrcPostAbstractCard getSrcPostAbstractCard() {
		return srcPostAbstractCard;
	}
	public void setSrcPostAbstractCard(OtherSrcPostAbstractCard srcPostAbstractCard) {
		this.srcPostAbstractCard = srcPostAbstractCard;
	}
	public String getSrcPostId() {
		return srcPostId;
	}
	public void setSrcPostId(String srcPostId) {
		this.srcPostId = srcPostId;
	}
	public OtherSrcUserCard getSrcUserCard() {
		return srcUserCard;
	}
	public void setSrcUserCard(OtherSrcUserCard srcUserCard) {
		this.srcUserCard = srcUserCard;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	@Override
	public String toString() {
		return "OtherAbstractInfoEntity [content=" + content + ", createDate="
				+ createDate + ", pPostId=" + pPostId + ", pUserName="
				+ pUserName + ", pUserSeqId=" + pUserSeqId + ", postId="
				+ postId + ", postLevel=" + postLevel + ", postType="
				+ postType + ", isMineLike=" + isMineLike
				+ ", srcPostAbstractCard=" + srcPostAbstractCard
				+ ", srcPostId=" + srcPostId + ", srcUserCard=" + srcUserCard
				+ ", userSeqId=" + userSeqId + "]";
	}
	
	
}
