package com.szrjk.entity;

import java.io.Serializable;

public class NewType implements Serializable{

	private String typeName;
	private String channel;
	private String typeId;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	@Override
	public String toString() {
		return "NewType [typeName=" + typeName + ", channel=" + channel
				+ ", typeId=" + typeId + "]";
	}
			
}
