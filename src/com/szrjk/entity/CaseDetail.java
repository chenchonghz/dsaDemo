package com.szrjk.entity;

import java.util.ArrayList;

public class CaseDetail {

	private UserCard userCard;
	private PostStatis postStatis;
	private int postType;
	private ArrayList<Forward> forwardList;
	private ArrayList<Like> likeList;
	private ArrayList<Comment> commentList;
	private CasePostDetail postDetail;
	private boolean isMineLike;
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	public PostStatis getPostStatis() {
		return postStatis;
	}
	public void setPostStatis(PostStatis postStatis) {
		this.postStatis = postStatis;
	}
	public int getPostType() {
		return postType;
	}
	public void setPostType(int postType) {
		this.postType = postType;
	}
	public ArrayList<Forward> getForwardList() {
		return forwardList;
	}
	public void setForwardList(ArrayList<Forward> forwardList) {
		this.forwardList = forwardList;
	}
	public ArrayList<Like> getLikeList() {
		return likeList;
	}
	public void setLikeList(ArrayList<Like> likeList) {
		this.likeList = likeList;
	}
	public ArrayList<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}
	public CasePostDetail getPostDetail() {
		return postDetail;
	}
	public void setPostDetail(CasePostDetail postDetail) {
		this.postDetail = postDetail;
	}
	public boolean isMineLike() {
		return isMineLike;
	}
	public void setMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}
	
	
	
}
