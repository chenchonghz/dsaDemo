package com.szrjk.entity;

public class RecommendUserList {

	private UserCard userCard;

	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	@Override
	public String toString() {
		return "RecommendUserList [userCard=" + userCard + "]";
	}
	
	
}
