package com.szrjk.entity;

import java.io.Serializable;

public class MyPostComments implements Serializable,Comparable<MyPostComments>{
	
	private UserCard userCard;
	private PostInfo abstractInfo;
	private UserCard userCard_SecondLayer;
	private UserCard userCard_FirstLayer;
	private CommentInfo commentInfo_SecondLayer;
	private CommentInfo commentInfo_FirstLayer;
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	public PostInfo getAbstractInfo() {
		return abstractInfo;
	}
	public void setAbstractInfo(PostInfo abstractInfo) {
		this.abstractInfo = abstractInfo;
	}
	public UserCard getUserCard_SecondLayer() {
		return userCard_SecondLayer;
	}
	public void setUserCard_SecondLayer(UserCard userCard_SecondLayer) {
		this.userCard_SecondLayer = userCard_SecondLayer;
	}
	public UserCard getUserCard_FirstLayer() {
		return userCard_FirstLayer;
	}
	public void setUserCard_FirstLayer(UserCard userCard_FirstLayer) {
		this.userCard_FirstLayer = userCard_FirstLayer;
	}
	public CommentInfo getCommentInfo_SecondLayer() {
		return commentInfo_SecondLayer;
	}
	public void setCommentInfo_SecondLayer(CommentInfo commentInfo_SecondLayer) {
		this.commentInfo_SecondLayer = commentInfo_SecondLayer;
	}
	public CommentInfo getCommentInfo_FirstLayer() {
		return commentInfo_FirstLayer;
	}
	public void setCommentInfo_FirstLayer(CommentInfo commentInfo_FirstLayer) {
		this.commentInfo_FirstLayer = commentInfo_FirstLayer;
	}
	@Override
	public String toString() {
		return "MyPostComments [userCard=" + userCard + ", abstractInfo="
				+ abstractInfo + ", userCard_SecondLayer="
				+ userCard_SecondLayer + ", userCard_FirstLayer="
				+ userCard_FirstLayer + ", commentInfo_SecondLayer="
				+ commentInfo_SecondLayer + ", commentInfo_FirstLayer="
				+ commentInfo_FirstLayer + "]";
	}
	@Override
	public int compareTo(MyPostComments myPostComments) {
		return this.getAbstractInfo().getCreateDate().compareTo(myPostComments.getAbstractInfo().getCreateDate());
	}
}
