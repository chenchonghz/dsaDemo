package com.szrjk.entity;

import java.util.ArrayList;

public class OrdinaryPostDetail {
	
	private String postId;
	private int postType;
	private UserCard userCard;
	private PostStatis postStatis;
	private PostDetail postDetail;
	private ArrayList<Forward> forwardList;
	private ArrayList<Comment> commentList;
	private ArrayList<Like> likeList;
	private boolean isMineLike;




	
	public OrdinaryPostDetail() {
		super();
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public int getPostType() {
		return postType;
	}

	public void setPostType(int postType) {
		this.postType = postType;
	}

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

	public PostDetail getPostDetail() {
		return postDetail;
	}

	public void setPostDetail(PostDetail postDetail) {
		this.postDetail = postDetail;
	}

	public ArrayList<Forward> getForwardList() {
		return forwardList;
	}

	public void setForwardList(ArrayList<Forward> forwardList) {
		this.forwardList = forwardList;
	}

	public ArrayList<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<Comment> commentList) {
		this.commentList = commentList;
	}

	public ArrayList<Like> getLikeList() {
		return likeList;
	}

	public void setLikeList(ArrayList<Like> likeList) {
		this.likeList = likeList;
	}

	public boolean isMineLike() {
		return isMineLike;
	}

	public void setMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}

	@Override
	public String toString() {
		return "OrdinaryPostDetail [postId=" + postId + ", postType="
				+ postType + ", userCard=" + userCard + ", postStatis="
				+ postStatis + ", postDetail=" + postDetail + ", forwardList="
				+ forwardList + ", commentList=" + commentList + ", likeList="
				+ likeList + ", isMineLike=" + isMineLike + "]";
	}

	

	
	
}
