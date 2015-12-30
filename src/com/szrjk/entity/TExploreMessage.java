package com.szrjk.entity;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "TEXPLOREMESSAGE")
public class TExploreMessage extends AbstractUserEntity<TMessage>{
	@Id(column ="messageId")@Transient
	private int messageId;
	
	@Column(column = "faceUrl")
	private String faceUrl;
	
	@Column(column = "messageName")
	private String messageName;
	
	@Column(column = "messageType")
	private int messageType;

	@Column(column = "messageContent")
	private String messageContent;
	
	@Column(column = "messageTime")
	private String messageTime;

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public String getMessageName() {
		return messageName;
	}

	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	@Override
	public String toString() {
		return "TExploreMessage [messageId=" + messageId + ", faceUrl="
				+ faceUrl + ", messageName=" + messageName + ", messageType="
				+ messageType + ", messageContent=" + messageContent
				+ ", messageTime=" + messageTime + "]";
	}
	
	@Override
	public  void initTable(final DbUtils db)
	{
		super.initTable(db);
	}
}
