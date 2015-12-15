package com.szrjk.message;

import com.szrjk.entity.UserCard;

public class MessageEntity {
	private String pkId;
	private String content;
	private String createDate;
	private UserCard receiveUserCard;
	private UserCard sendUserCard;
	public MessageEntity() {
		super();
	}
	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public UserCard getReceiveUserCard() {
		return receiveUserCard;
	}
	public void setReceiveUserCard(UserCard receiveUserCard) {
		this.receiveUserCard = receiveUserCard;
	}
	public UserCard getSendUserCard() {
		return sendUserCard;
	}
	public void setSendUserCard(UserCard sendUserCard) {
		this.sendUserCard = sendUserCard;
	}
	@Override
	public String toString() {
		return "MessageEntity [pkId=" + pkId + ", content=" + content
				+ ", createDate=" + createDate + ", receiveUserCard="
				+ receiveUserCard + ", sendUserCard=" + sendUserCard + "]";
	}

}

