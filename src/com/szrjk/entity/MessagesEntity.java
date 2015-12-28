package com.szrjk.entity;

public class MessagesEntity {

	private String opTime;
	private long pushMsgCount;
	private UserCard userCard;
	private int pushType;
	private String pushContent;
	private String reciverUserId;
	private String doneDate;
	private String opTimestamp;
	private String pushUserId;
	private String pushDesc;
	private String pushRemark;
	private String createDate;
	private String pushTitle;

	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public long getPushMsgCount() {
		return pushMsgCount;
	}
	public void setPushMsgCount(long pushMsgCount) {
		this.pushMsgCount = pushMsgCount;
	}
	public int getPushType() {
		return pushType;
	}
	public void setPushType(int pushType) {
		this.pushType = pushType;
	}
	public String getPushContent() {
		return pushContent;
	}
	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}
	public String getReciverUserId() {
		return reciverUserId;
	}
	public void setReciverUserId(String reciverUserId) {
		this.reciverUserId = reciverUserId;
	}
	public String getDoneDate() {
		return doneDate;
	}
	public void setDoneDate(String doneDate) {
		this.doneDate = doneDate;
	}
	public String getOpTimestamp() {
		return opTimestamp;
	}
	public void setOpTimestamp(String opTimestamp) {
		this.opTimestamp = opTimestamp;
	}
	public String getPushUserId() {
		return pushUserId;
	}
	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}
	public String getPushDesc() {
		return pushDesc;
	}
	public void setPushDesc(String pushDesc) {
		this.pushDesc = pushDesc;
	}
	public String getPushRemark() {
		return pushRemark;
	}
	public void setPushRemark(String pushRemark) {
		this.pushRemark = pushRemark;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getPushTitle() {
		return pushTitle;
	}
	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}
	
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	@Override
	public String toString() {
		return "MessagesEntity [opTime=" + opTime
				+ ", pushMsgCount=" + pushMsgCount + ", userCard=" + userCard
				+ ", pushType=" + pushType + ", pushContent=" + pushContent
				+ ", reciverUserId=" + reciverUserId + ", doneDate=" + doneDate
				+ ", opTimestamp=" + opTimestamp + ", pushUserId=" + pushUserId
				+ ", pushDesc=" + pushDesc + ", pushRemark=" + pushRemark
				+ ", createDate=" + createDate + ", pushTitle=" + pushTitle
				+ "]";
	}	
	
}
