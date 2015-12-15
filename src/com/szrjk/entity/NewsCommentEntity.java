package com.szrjk.entity;

import java.io.Serializable;

public class NewsCommentEntity implements Serializable{

	private String userSeqId;
	private String userProfessionalTitle;
	private String commentTime;
	private String userDeptName;
	private int praiseCount;
	private String userName;
	private String commentContent;
	private String commentId;
	private String userCompanyName;
	private UserCard smallCard;
	
	
	public UserCard getSmallCard() {
		return smallCard;
	}
	public void setSmallCard(UserCard smallCard) {
		this.smallCard = smallCard;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getUserProfessionalTitle() {
		return userProfessionalTitle;
	}
	public void setUserProfessionalTitle(String userProfessionalTitle) {
		this.userProfessionalTitle = userProfessionalTitle;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getUserDeptName() {
		return userDeptName;
	}
	public void setUserDeptName(String userDeptName) {
		this.userDeptName = userDeptName;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getUserCompanyName() {
		return userCompanyName;
	}
	public void setUserCompanyName(String userCompanyName) {
		this.userCompanyName = userCompanyName;
	}
	@Override
	public String toString() {
		return "NewsCommentEntity [userSeqId=" + userSeqId
				+ ", userProfessionalTitle=" + userProfessionalTitle
				+ ", commentTime=" + commentTime + ", userDeptName="
				+ userDeptName + ", praiseCount=" + praiseCount + ", userName="
				+ userName + ", commentContent=" + commentContent
				+ ", commentId=" + commentId + ", userCompanyName="
				+ userCompanyName + ", smallCard=" + smallCard + "]";
	}
	
	
	
	
	
}
