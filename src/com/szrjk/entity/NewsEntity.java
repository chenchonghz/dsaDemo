package com.szrjk.entity;

public class NewsEntity {

	private String infSignTime;   //资讯创建时间
	private String infCreateTime;
	private int infShowType;     //资讯展示类别
	private String infTab;       //资讯标签   
	private String infId;       //资讯ID
	private int infPraiseCount;   //资讯点赞数量
	private String infImageExt;    //资讯扩展图片信息
	private int infForwardCount;    //资讯转发数量
	private String infTitleAbstract;    //资讯标题摘要
	private int infCommentCount;     //资讯评论数量
	private int infLevel;       //执行显示级别
	private String infImage;    //资讯图片
	
	
	
	public String getInfCreateTime() {
		return infCreateTime;
	}
	public void setInfCreateTime(String infCreateTime) {
		this.infCreateTime = infCreateTime;
	}
	public String getInfSignTime() {
		return infSignTime;
	}
	public void setInfSignTime(String infSignTime) {
		this.infSignTime = infSignTime;
	}
	public int getInfShowType() {
		return infShowType;
	}
	public void setInfShowType(int infShowType) {
		this.infShowType = infShowType;
	}
	public String getInfTab() {
		return infTab;
	}
	public void setInfTab(String infTab) {
		this.infTab = infTab;
	}
	public String getInfId() {
		return infId;
	}
	public void setInfId(String infId) {
		this.infId = infId;
	}
	public int getInfPraiseCount() {
		return infPraiseCount;
	}
	public void setInfPraiseCount(int infPraiseCount) {
		this.infPraiseCount = infPraiseCount;
	}
	public String getInfImageExt() {
		return infImageExt;
	}
	public void setInfImageExt(String infImageExt) {
		this.infImageExt = infImageExt;
	}
	public int getInfForwardCount() {
		return infForwardCount;
	}
	public void setInfForwardCount(int infForwardCount) {
		this.infForwardCount = infForwardCount;
	}
	public String getInfTitleAbstract() {
		return infTitleAbstract;
	}
	public void setInfTitleAbstract(String infTitleAbstract) {
		this.infTitleAbstract = infTitleAbstract;
	}
	public int getInfCommentCount() {
		return infCommentCount;
	}
	public void setInfCommentCount(int infCommentCount) {
		this.infCommentCount = infCommentCount;
	}
	public int getInfLevel() {
		return infLevel;
	}
	public void setInfLevel(int infLevel) {
		this.infLevel = infLevel;
	}
	public String getInfImage() {
		return infImage;
	}
	public void setInfImage(String infImage) {
		this.infImage = infImage;
	}
	@Override
	public String toString() {
		return "NewsEntity [infSignTime=" + infSignTime + ", infCreateTime="
				+ infCreateTime + ", infShowType=" + infShowType + ", infTab="
				+ infTab + ", infId=" + infId + ", infPraiseCount="
				+ infPraiseCount + ", infImageExt=" + infImageExt
				+ ", infForwardCount=" + infForwardCount
				+ ", infTitleAbstract=" + infTitleAbstract
				+ ", infCommentCount=" + infCommentCount + ", infLevel="
				+ infLevel + ", infImage=" + infImage + "]";
	}

	
	
	
	
}
