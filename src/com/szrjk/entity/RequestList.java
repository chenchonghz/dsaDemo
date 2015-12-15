package com.szrjk.entity;

public class RequestList {
	private UserCard UserCard;
	private String requestDate;
	private String requestDesc;
	public RequestList() {
		super();
	}
	public RequestList(UserCard UserCard, String requestDate, String requestDesc) {
		super();
		this.UserCard = UserCard;
		this.requestDate = requestDate;
		this.requestDesc = requestDesc;
	}
	public UserCard getUserCard() {
		return UserCard;
	}
	public void setUserCard(UserCard UserCard) {
		this.UserCard = UserCard;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getRequestDesc() {
		return requestDesc;
	}
	public void setRequestDesc(String requestDesc) {
		this.requestDesc = requestDesc;
	}
	@Override
	public String toString() {
		return "RequestList [UserCard=" + UserCard + ", requestDate="
				+ requestDate + ", requestDesc=" + requestDesc + "]";
	}
	
}
