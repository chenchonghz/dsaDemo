package com.szrjk.entity;

public class CasePostDetail {

	private String userSeqId;
	private String postId;
	private String postType;
	//完成率
	private String completeRate;
	//科室id："10011;11004;11;16"
	private String deptIds;
	//科室名字："风湿免疫内科,神经外科,外科,中医科"
	private String deptNames;
	//主题
	private String postTitle;
	//主诉，病史
	private String chapterTitle1;
	private String content1;
	private String picList1;//图片列表："http://test1.jpg|http://test2.jpg|http://test3.jpg|http://test4.jpg"
	//查体，辅查
	private String chapterTitle2;
	private String content2;
	private String picList2;
	//诊断，处理
	private String chapterTitle3;
	private String content3;
	private String picList3;
	//讨论，随访
	private String chapterTitle4;
	private String content4;
	private String picList4;
	//创建时间:"2015-10-27 10:00:17.74"
	private String createDate;

	private String postLevel;

	private String srcPostId;

	public String getSrcPostId() {
		return srcPostId;
	}

	public void setSrcPostId(String srcPostId) {
		this.srcPostId = srcPostId;
	}

	public String getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	public CasePostDetail() {
		super();
	}
	public String getUserSeqId() {
		return userSeqId;
	}
	public void setUserSeqId(String userSeqId) {
		this.userSeqId = userSeqId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getCompleteRate() {
		return completeRate;
	}
	public void setCompleteRate(String completeRate) {
		this.completeRate = completeRate;
	}
	public String getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}
	public String getDeptNames() {
		return deptNames;
	}
	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}
	public String getPostTitle() {
		return postTitle;
	}
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	public String getChapterTitle1() {
		return chapterTitle1;
	}
	public void setChapterTitle1(String chapterTitle1) {
		this.chapterTitle1 = chapterTitle1;
	}
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getPicList1() {
		return picList1;
	}
	public void setPicList1(String picList1) {
		this.picList1 = picList1;
	}
	public String getChapterTitle2() {
		return chapterTitle2;
	}
	public void setChapterTitle2(String chapterTitle2) {
		this.chapterTitle2 = chapterTitle2;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	public String getPicList2() {
		return picList2;
	}
	public void setPicList2(String picList2) {
		this.picList2 = picList2;
	}
	public String getChapterTitle3() {
		return chapterTitle3;
	}
	public void setChapterTitle3(String chapterTitle3) {
		this.chapterTitle3 = chapterTitle3;
	}
	public String getContent3() {
		return content3;
	}
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	public String getPicList3() {
		return picList3;
	}
	public void setPicList3(String picList3) {
		this.picList3 = picList3;
	}
	public String getChapterTitle4() {
		return chapterTitle4;
	}
	public void setChapterTitle4(String chapterTitle4) {
		this.chapterTitle4 = chapterTitle4;
	}
	public String getContent4() {
		return content4;
	}
	public void setContent4(String content4) {
		this.content4 = content4;
	}
	public String getPicList4() {
		return picList4;
	}
	public void setPicList4(String picList4) {
		this.picList4 = picList4;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	
}
