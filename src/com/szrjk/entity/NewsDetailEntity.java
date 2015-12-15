package com.szrjk.entity;

import java.util.ArrayList;

public class NewsDetailEntity {
	private int forwardCount;
	private int praiseCount;
	private int commentCount;
	private String infCotent;
	private String infId;
	private ArrayList<NewsEntity> relInfs;
	private ArrayList<NewsCommentEntity> comments;
	boolean isPraiseed;
	public int getForwardCount() {
		return forwardCount;
	}
	public void setForwardCount(int forwardCount) {
		this.forwardCount = forwardCount;
	}
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getInfCotent() {
		return infCotent;
	}
	public void setInfCotent(String infCotent) {
		this.infCotent = infCotent;
	}
	public String getInfId() {
		return infId;
	}
	public void setInfId(String infId) {
		this.infId = infId;
	}
	public ArrayList<NewsEntity> getRelInfs() {
		return relInfs;
	}
	public void setRelInfs(ArrayList<NewsEntity> relInfs) {
		this.relInfs = relInfs;
	}
	public ArrayList<NewsCommentEntity> getComments() {
		return comments;
	}
	public void setComments(ArrayList<NewsCommentEntity> comments) {
		this.comments = comments;
	}
	public boolean getIsPraiseed() {
		return isPraiseed;
	}
	public void setIsPraiseed(boolean isPraiseed) {
		this.isPraiseed = isPraiseed;
	}
	@Override
	public String toString() {
		return "NewsDetailEntity [forwardCount=" + forwardCount
				+ ", praiseCount=" + praiseCount + ", commentCount="
				+ commentCount + ", infCotent=" + infCotent + ", infId="
				+ infId + ", relInfs=" + relInfs + ", comments=" + comments
				+ ", isPraiseed=" + isPraiseed + "]";
	}
	
	
	
	
	
	

}
