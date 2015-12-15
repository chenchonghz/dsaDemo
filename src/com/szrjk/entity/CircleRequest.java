package com.szrjk.entity;

import java.io.Serializable;

public class CircleRequest implements Serializable {
	//用户自己的id
	private String userSeqId;
	//用户自己的usercard
	private UserCard userCard;
	//邀请对象/请求对象 usercard
	private UserCard objUserCard;
	//邀请类型，“I”：邀请；“R”：请求
	private String invitationType; 
	//圈子名称
	private String coterieName;
	//对象id
	private String objUserSeqId;
	//圈子头像
	private String coterieFaceUrl;
	//圈子id
	private String coterieId;
	//请求时间
	private String createDate;
	//邀请id
	private String invitationId;
	public CircleRequest() {
		super();
	}
	public CircleRequest(String userSeqId, UserCard userCard,
			UserCard objUserCard, String invitationType, String coterieName,
			String objUserSeqId, String coterieFaceUrl, String coterieId,
			String createDate, String invitationId) {
		super();
		this.userSeqId = userSeqId;
		this.userCard = userCard;
		this.objUserCard = objUserCard;
		this.invitationType = invitationType;
		this.coterieName = coterieName;
		this.objUserSeqId = objUserSeqId;
		this.coterieFaceUrl = coterieFaceUrl;
		this.coterieId = coterieId;
		this.createDate = createDate;
		this.invitationId = invitationId;
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	public UserCard getObjUserCard() {
		return objUserCard;
	}
	public void setObjUserCard(UserCard objUserCard) {
		this.objUserCard = objUserCard;
	}
	public String getInvitationType() {
		return invitationType;
	}
	public void setInvitationType(String invitationType) {
		this.invitationType = invitationType;
	}
	public String getCoterieName() {
		return coterieName;
	}
	public void setCoterieName(String coterieName) {
		this.coterieName = coterieName;
	}
	public String getObjUserSeqId() {
		return objUserSeqId;
	}
	public void setObjUserSeqId(String objUserSeqId) {
		this.objUserSeqId = objUserSeqId;
	}
	public String getCoterieFaceUrl() {
		return coterieFaceUrl;
	}
	public void setCoterieFaceUrl(String coterieFaceUrl) {
		this.coterieFaceUrl = coterieFaceUrl;
	}
	public String getCoterieId() {
		return coterieId;
	}
	public void setCoterieId(String coterieId) {
		this.coterieId = coterieId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getInvitationId() {
		return invitationId;
	}
	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}
	@Override
	public String toString() {
		return "CircleRequest [userSeqId=" + userSeqId + ", userCard="
				+ userCard + ", objUserCard=" + objUserCard
				+ ", invitationType=" + invitationType + ", coterieName="
				+ coterieName + ", objUserSeqId=" + objUserSeqId
				+ ", coterieFaceUrl=" + coterieFaceUrl + ", coterieId="
				+ coterieId + ", createDate=" + createDate + ", invitationId="
				+ invitationId + "]";
	}
	
}
