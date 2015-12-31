package com.szrjk.entity;

import java.io.Serializable;
/**
 * 圈子通知实体类  2015.12.30更新
 * @author 郑斯铭
 *
 */
public class CircleRequest implements Serializable {
	//通知序号
	private String pkID;
	//用户自己的id
	private String userSeqId;
	//用户自己的usercard
	private UserCard userCard;
	//邀请对象/请求对象 objUserSeqId
	private String objUserSeqId;
	//邀请对象/请求对象 usercard
	private UserCard objUserCard;
	//通知类型
	private String notifyType; 
	//圈子名称
	private String coterieName;
	//圈子头像
	private String coterieFaceUrl;
	//圈子id
	private String coterieId;
	//请求时间
	private String opTime;
	//成员数量
	private String memberCount;
	
	public CircleRequest() {
		super();
	}
	public CircleRequest(String pkID, String userSeqId, UserCard userCard,
			String objUserSeqId, UserCard objUserCard, String notifyType,
			String coterieName, String coterieFaceUrl, String coterieId,
			String opTime, String memberCount) {
		super();
		this.pkID = pkID;
		this.userSeqId = userSeqId;
		this.userCard = userCard;
		this.objUserSeqId = objUserSeqId;
		this.objUserCard = objUserCard;
		this.notifyType = notifyType;
		this.coterieName = coterieName;
		this.coterieFaceUrl = coterieFaceUrl;
		this.coterieId = coterieId;
		this.opTime = opTime;
		this.memberCount = memberCount;
	}
	public String getPkID() {
		return pkID;
	}
	public void setPkID(String pkID) {
		this.pkID = pkID;
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
	public String getObjUserSeqId() {
		return objUserSeqId;
	}
	public void setObjUserSeqId(String objUserSeqId) {
		this.objUserSeqId = objUserSeqId;
	}
	public UserCard getObjUserCard() {
		return objUserCard;
	}
	public void setObjUserCard(UserCard objUserCard) {
		this.objUserCard = objUserCard;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getCoterieName() {
		return coterieName;
	}
	public void setCoterieName(String coterieName) {
		this.coterieName = coterieName;
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
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}
	@Override
	public String toString() {
		return "CircleRequest [pkID=" + pkID + ", userSeqId=" + userSeqId
				+ ", userCard=" + userCard + ", objUserSeqId=" + objUserSeqId
				+ ", objUserCard=" + objUserCard + ", notifyType=" + notifyType
				+ ", coterieName=" + coterieName + ", coterieFaceUrl="
				+ coterieFaceUrl + ", coterieId=" + coterieId + ", opTime="
				+ opTime + ", memberCount=" + memberCount + "]";
	}
	
}
