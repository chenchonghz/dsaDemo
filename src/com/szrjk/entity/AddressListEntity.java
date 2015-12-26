package com.szrjk.entity;

public class AddressListEntity {
	private AddressCard UserCard;

	public AddressListEntity() {
		super();
	}

	public AddressListEntity(AddressCard userCard) {
		super();
		this.UserCard = userCard;
	}

	public AddressCard getUserCard() {
		return UserCard;
	}

	public void setUserCard(AddressCard userCard) {
		UserCard = userCard;
	}

	@Override
	public String toString() {
		return "AddressListEntity [UserCard=" + UserCard + "]";
	}

	
}
