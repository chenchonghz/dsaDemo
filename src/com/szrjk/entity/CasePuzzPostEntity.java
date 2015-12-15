package com.szrjk.entity;

public class CasePuzzPostEntity {

	private String title;
	private String deptId;
	private String deptValue;
	private String caseImg;
	private String checkImg;
	private String treatImg;
	private String visitImg;
	private String caseContent;
	private String checkContent;
	private String treatContent;
	private String visitContent;
	private int px;
	public int getPx() {
		return px;
	}
	public void setPx(int px) {
		this.px = px;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptValue() {
		return deptValue;
	}
	public void setDeptValue(String deptValue) {
		this.deptValue = deptValue;
	}
	public String getCaseImg() {
		return caseImg;
	}
	public void setCaseImg(String caseImg) {
		this.caseImg = caseImg;
	}
	public String getCheckImg() {
		return checkImg;
	}
	public void setCheckImg(String checkImg) {
		this.checkImg = checkImg;
	}
	public String getTreatImg() {
		return treatImg;
	}
	public void setTreatImg(String treatImg) {
		this.treatImg = treatImg;
	}
	public String getVisitImg() {
		return visitImg;
	}
	public void setVisitImg(String visitImg) {
		this.visitImg = visitImg;
	}
	public String getCaseContent() {
		return caseContent;
	}
	public void setCaseContent(String caseContent) {
		this.caseContent = caseContent;
	}
	public String getCheckContent() {
		return checkContent;
	}
	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}
	public String getTreatContent() {
		return treatContent;
	}
	public void setTreatContent(String treatContent) {
		this.treatContent = treatContent;
	}
	public String getVisitContent() {
		return visitContent;
	}
	public void setVisitContent(String visitContent) {
		this.visitContent = visitContent;
	}
	@Override
	public String toString() {
		return "CasePuzzPostEntity [title=" + title + ", deptId=" + deptId
				+ ", deptValue=" + deptValue + ", caseImg=" + caseImg
				+ ", checkImg=" + checkImg + ", treatImg=" + treatImg
				+ ", visitImg=" + visitImg + ", caseContent=" + caseContent
				+ ", checkContent=" + checkContent + ", treatContent="
				+ treatContent + ", visitContent=" + visitContent + ", px="
				+ px + "]";
	}
	
}
