package com.szrjk.entity;

public class RemindEvent {

	private int RemindMessage;
	private String remindString;
	/*
	 * 我的首页加红点实体类
	 *  RemindMessage  1:消息加红点		11:消息取消红点
	 *  			   2:好友加红点		21:好友取消红点
	 *  			   3:圈子加红点		31:圈子取消红点
	 */
	
	public RemindEvent(int remindMessage) {
		RemindMessage = remindMessage;
	}
	

	public RemindEvent(int remindMessage, String remindString) {
		super();
		RemindMessage = remindMessage;
		this.remindString = remindString;
	}


	public int getRemindMessage() {
		return RemindMessage;
	}

	public void setRemindMessage(int remindMessage) {
		RemindMessage = remindMessage;
	}


	public String getRemindString() {
		return remindString;
	}


	public void setRemindString(String remindString) {
		this.remindString = remindString;
	}
	
	
}
