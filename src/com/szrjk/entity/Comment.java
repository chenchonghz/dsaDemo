package com.szrjk.entity;


public class Comment  {

	private UserCard userCard;
	private CommentInfo commentInfo;
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
//	public CommentContent getContentInfo() {
//		return commentContent;
//	}
//	public void setContentInfo(CommentContent commentContent) {
//		this.commentContent = commentContent;
//	}
//

	public CommentInfo getCommentInfo() {
		return commentInfo;
	}

	public void setCommentInfo(CommentInfo commentInfo) {
		this.commentInfo = commentInfo;
	}
}
