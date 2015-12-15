package com.szrjk.entity;

public class FriendList {
	private UserCard UserCard;

	public FriendList() {
		super();
	}

	public FriendList(com.szrjk.entity.UserCard userCard) {
		super();
		this.UserCard = userCard;
	}

	public UserCard getUserCard() {
		return UserCard;
	}

	public void setUserCard(UserCard userCard) {
		UserCard = userCard;
	}

	@Override
	public String toString() {
		return "FriendList [UserCard=" + UserCard + "]";
	}
	
}
