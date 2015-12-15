package com.szrjk.entity;

public class ForwardInfo {

	private String content;
	private String pUserSeqId;
	private String userSeqId;
	private String postType;
	private String postLevel;
	private String srcPostId;
	private String pPostId;
	private String pPontent;
	private UserCard srcUserCard;
	private String createDate;
	private String pUserName;
	private String postId;
	private PostAbstractCard srcPostAbstractCard;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getpUserSeqId() {
		return pUserSeqId;
	}
	public void setpUserSeqId(String pUserSeqId) {
		this.pUserSeqId = pUserSeqId;
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
	public String getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	public String getSrcPostId() {
		return srcPostId;
	}
	public void setSrcPostId(String srcPostId) {
		this.srcPostId = srcPostId;
	}
	public String getpPostId() {
		return pPostId;
	}
	public void setpPostId(String pPostId) {
		this.pPostId = pPostId;
	}
	public String getpPontent() {
		return pPontent;
	}
	public void setpPontent(String pPontent) {
		this.pPontent = pPontent;
	}
	public UserCard getSrcUserCard() {
		return srcUserCard;
	}
	public void setSrcUserCard(UserCard srcUserCard) {
		this.srcUserCard = srcUserCard;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getpUserName() {
		return pUserName;
	}
	public void setpUserName(String pUserName) {
		this.pUserName = pUserName;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public PostAbstractCard getSrcPostAbstractCard() {
		return srcPostAbstractCard;
	}
	public void setSrcPostAbstractCard(PostAbstractCard srcPostAbstractCard) {
		this.srcPostAbstractCard = srcPostAbstractCard;
	}
	@Override
	public String toString() {
		return "ForwardContent [content=" + content + ", pUserSeqId="
				+ pUserSeqId + ", userSeqId=" + userSeqId + ", postType="
				+ postType + ", postLevel=" + postLevel + ", srcPostId="
				+ srcPostId + ", pPostId=" + pPostId + ", pPontent=" + pPontent
				+ ", srcUserCard=" + srcUserCard + ", createDate=" + createDate
				+ ", pUserName=" + pUserName + ", postId=" + postId
				+ ", srcPostAbstractCard=" + srcPostAbstractCard + "]";
	}
	
	
} 
