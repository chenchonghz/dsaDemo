package com.szrjk.message;

import com.szrjk.entity.UserCard;

public class MessageListEntity {
	private String pkId;
	private UserCard sendUserCard;
	private UserCard receiveUserCard;
	private String createDate;
	public MessageListEntity() {
		super();
	}
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	public UserCard getSendUserCard() {
		return sendUserCard;
	}
	public void setSendUserCard(UserCard sendUserCard) {
		this.sendUserCard = sendUserCard;
	}
	public UserCard getReceiveUserCard() {
		return receiveUserCard;
	}
	public void setReceiveUserCard(UserCard receiveUserCard) {
		this.receiveUserCard = receiveUserCard;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "MessageListEntity [pkId=" + pkId + ", sendUserCard="
				+ sendUserCard + ", receiveUserCard=" + receiveUserCard
				+ ", createDate=" + createDate + "]";
	}
	
}
