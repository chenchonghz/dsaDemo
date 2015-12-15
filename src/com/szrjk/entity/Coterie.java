package com.szrjk.entity;

import java.io.Serializable;
import java.util.List;

public class Coterie implements Serializable{

	private String coterieDesc;
	private String coterieName;
	private String coterieFaceUrl;
	private String coterieId;
	private String creatorId;//圈子创建者Id
	private UserCard creator;//圈子创建者名片
	private String createDate;
	private String memberCount;
	private String isOpen;
	private String isMember;//是否是该群内成员
	private String memberType;//查询者在圈内的身份0：非圈内成员	1：普通成员	3：群主
	private List<UserCard> memberCardList;
	
	public Coterie() {
		super();
	}

	public Coterie(String coterieDesc, String coterieName,
			String coterieFaceUrl, String coterieId, String createDate,
			String memberCount, String isOpen, List<UserCard> memberCardList) {
		super();
		this.coterieDesc = coterieDesc;
		this.coterieName = coterieName;
		this.coterieFaceUrl = coterieFaceUrl;
		this.coterieId = coterieId;
		this.createDate = createDate;
		this.memberCount = memberCount;
		this.isOpen = isOpen;
		this.memberCardList = memberCardList;
	}

	public String getCoterieDesc() {
		return coterieDesc;
	}

	public void setCoterieDesc(String coterieDesc) {
		this.coterieDesc = coterieDesc;
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
	
	

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public UserCard getCreator() {
		return creator;
	}

	public void setCreator(UserCard creator) {
		this.creator = creator;
	}

	public String getIsMember() {
		return isMember;
	}

	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public List<UserCard> getMemberCardList() {
		return memberCardList;
	}

	public void setMemberCardList(List<UserCard> memberCardList) {
		this.memberCardList = memberCardList;
	}

	@Override
	public String toString() {
		return "Coterie [coterieDesc=" + coterieDesc + ", coterieName="
				+ coterieName + ", coterieFaceUrl=" + coterieFaceUrl
				+ ", coterieId=" + coterieId + ", creatorId=" + creatorId
				+ ", creator=" + creator + ", createDate=" + createDate
				+ ", memberCount=" + memberCount + ", isOpen=" + isOpen
				+ ", isMember=" + isMember + ", memberType=" + memberType
				+ ", memberCardList=" + memberCardList + "]";
	}

	
	
	
}
