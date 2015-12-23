package com.szrjk.entity;

import java.io.Serializable;
import java.util.List;

public class CircleInfo implements Serializable{
	//{"coterieDesc":"用于测试4","coterieName":"测试4",
	//"objUserSeqId":"100591","memberType":"3",
	//"coterieFaceUrl":"http://h.hiphotos.baidu.com/image/pic/item/e850352ac65c103834cb0d02b6119313b17e89bd.jpg",
	//"coterieId":"3000011093","isOpen":"No"}]}}
	private String coterieDesc;//圈子描述
	private String coterieName;//圈子名
	private String objUserSeqId;//用户id
	private String coterieLevel;//圈子等级（新添加）
	private String coterieType;//圈子类型,1个人，2组织（新添加）
	private String memberType; //用户类型 1.成员 2.管理员 3.创建人（群主）
	private String coterieFaceUrl;//圈子头像地址
	private String coterieId;//圈子id
	private String isOpen;//是否公开  公开"YES"  不公开"NO"
	private String memberCount;
	private String isMember;
	private List<CircleType> propList;//圈子科室
	public String getCoterieLevel() {
		return coterieLevel;
	}

	public void setCoterieLevel(String coterieLevel) {
		this.coterieLevel = coterieLevel;
	}

	public String getCoterieType() {
		return coterieType;
	}

	public void setCoterieType(String coterieType) {
		this.coterieType = coterieType;
	}

	public List<CircleType> getPropList() {
		return propList;
	}

	public void setPropList(List<CircleType> propList) {
		this.propList = propList;
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
	public String getObjUserSeqId() {
		return objUserSeqId;
	}
	public void setObjUserSeqId(String objUserSeqId) {
		this.objUserSeqId = objUserSeqId;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
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
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}
	public String getIsMember() {
		return isMember;
	}
	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}

	@Override
	public String toString() {
		return "CircleInfo [coterieDesc=" + coterieDesc + ", coterieName="
				+ coterieName + ", objUserSeqId=" + objUserSeqId
				+ ", coterieLevel=" + coterieLevel + ", coterieType="
				+ coterieType + ", memberType=" + memberType
				+ ", coterieFaceUrl=" + coterieFaceUrl + ", coterieId="
				+ coterieId + ", isOpen=" + isOpen + ", memberCount="
				+ memberCount + ", isMember=" + isMember + ", propList="
				+ propList + "]";
	}
	
}
