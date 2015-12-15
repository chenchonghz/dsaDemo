package com.szrjk.entity;

public class Like {

	private UserCard userCard;

	private LikeInfo likeInfo;

	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	public LikeInfo getLikeInfo() {
		return likeInfo;
	}

	public void setLikeInfo(LikeInfo likeInfo) {
		this.likeInfo = likeInfo;
	}

	@Override
	public String toString() {
		return "Like [userCard=" + userCard + ", likeInfo=" + likeInfo + "]";
	}
	
	
}
