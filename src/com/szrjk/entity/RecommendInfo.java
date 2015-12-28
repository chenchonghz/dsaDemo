package com.szrjk.entity;

public class RecommendInfo {
	
	private RecommendContent recommendContent;
	private String recommendType;
	public RecommendContent getRecommendContent() {
		return recommendContent;
	}
	public void setRecommendContent(RecommendContent recommendContent) {
		this.recommendContent = recommendContent;
	}
	public String getRecommendType() {
		return recommendType;
	}
	public void setRecommendType(String recommendType) {
		this.recommendType = recommendType;
	}
	@Override
	public String toString() {
		return "RecommendInfo [recommendContent=" + recommendContent
				+ ", recommendType=" + recommendType + "]";
	}
	
	

}
