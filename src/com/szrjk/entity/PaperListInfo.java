package com.szrjk.entity;

import java.io.Serializable;

public class PaperListInfo implements Serializable{
	private String summary;//论文摘要
	private String provenance;//论文出版文献名
	private String author; //论文作者
	private String releaseDate;//上传日期
	private String keyword;//论文关键字
	private String paperTitle;//论文标题
	private String knowledgeId;//论文id
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getProvenance() {
		return provenance;
	}
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPaperTitle() {
		return paperTitle;
	}
	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}
	public String getKnowledgeId() {
		return knowledgeId;
	}
	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	@Override
	public String toString() {
		return "PaperListInfo [summary=" + summary + ", provenance=" + provenance
				+ ", author=" + author + ", releaseDate=" + releaseDate
				+ ", keyword=" + keyword + ", paperTitle=" + paperTitle
				+ ", knowledgeId=" + knowledgeId + "]";
	}

}
