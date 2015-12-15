package com.szrjk.entity;


public class OthersPostEntity {
	
	private OtherAbstractInfoEntity abstractInfo;
	private OtherStatisInfo statisInfo;
	private boolean isMineLike;
	
	
	
	public boolean isMineLike() {
		return isMineLike;
	}
	public void setMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}
	public OtherAbstractInfoEntity getAbstractInfo() {
		return abstractInfo;
	}
	public void setAbstractInfo(OtherAbstractInfoEntity abstractInfo) {
		this.abstractInfo = abstractInfo;
	}
	public OtherStatisInfo getStatisInfo() {
		return statisInfo;
	}
	public void setStatisInfo(OtherStatisInfo statisInfo) {
		this.statisInfo = statisInfo;
	}
	@Override
	public String toString() {
		return "OthersPostEntity [abstractInfo=" + abstractInfo
				+ ", statisInfo=" + statisInfo + "]";
	}
	
}
