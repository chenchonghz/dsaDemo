package com.szrjk.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 帖子信息实体类
 * 
 * @author Liyi
 * 
 */
public class PostInfo
{

	// 用户ID
	private String userSeqId;
	// 背景图片
	private String backgroundPic;
	// 帖子类型
	private String postType;
	// 科室ID
	private String deptIds;
	// 帖子标题
	private String postTitle;
	// 科室名称
	private String deptNames;
	// 帖子ID
	private String postId;
	// 完整度
	private String completeRate;
	// 图片列表
	private String picList;
	//图片List
	private String[] picsList;
	// 发帖时间
	private String createDate;
	//帖子正文
	private String content;
	//转发帖子用户名片实体
	private SrcUserCard srcUserCard;
	//转发帖子内容实体
	private SrcPostInfo srcPostAbstractCard;
	//中间层转发帖子用户id
	private String pUserSeqId;
	//中间层转发帖子用户名
	private String pUserName;
	//转发帖子层级
	private String postLevel;
	//转发帖子的帖子id
	private String srcPostId;
	//中间层转发帖子文字内容
	private String pContent;
	//中间层是否已删除
	private String pIsDelete;
	//转发原帖是否已删除
	private String srcIsDelete;
	//中间层postId
	private String pPostId;
	//推荐好友列表
	private List<UserCard> recommendUser;
	//圈子名字
	private String coterieName;
	//圈子Id
	private String coterieId;
	//是否公开圈子
	private int isOpen;
	//转发帖子list
	private List<PostAbstractList> postAbstractList;


	public List<PostAbstractList> getPostAbstractList() {
		return postAbstractList;
	}


	public void setPostAbstractList(List<PostAbstractList> postAbstractList) {
		Collections.reverse(postAbstractList);
		this.postAbstractList = postAbstractList;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getCoterieName() {
		return coterieName;
	}

	public void setCoterieName(String coterieName) {
		this.coterieName = coterieName;
	}

	public String getCoterieId() {
		return coterieId;
	}

	public void setCoterieId(String coterieId) {
		this.coterieId = coterieId;
	}

	public List<UserCard> getRecommendUser() {
		return recommendUser;
	}

	public void setRecommendUser(List<UserCard> recommendUser) {
		this.recommendUser = recommendUser;
	}

	public String getPPostId() {
		return pPostId;
	}

	public void setPPostId(String pPostId) {
		this.pPostId = pPostId;
	}

	public String getPIsDelete() {
		return pIsDelete;
	}

	public void setPIsDelete(String pIsDelete) {
		this.pIsDelete = pIsDelete;
	}

	public String getSrcIsDelete() {
		return srcIsDelete;
	}

	public void setSrcIsDelete(String srcIsDelete) {
		this.srcIsDelete = srcIsDelete;
	}

	public String getPUserSeqId() {
		return pUserSeqId;
	}

	public void setPUserSeqId(String pUserSeqId) {
		this.pUserSeqId = pUserSeqId;
	}

	public String getPUserName() {
		return pUserName;
	}

	public void setPUserName(String pUserName) {
		this.pUserName = pUserName;
	}

	public String getPostLevel() {
		return postLevel;
	}

	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}

	public String getSrcPostId() {
		return srcPostId;
	}

	public void setSrcPostId(String srcPostId) {
		this.srcPostId = srcPostId;
	}

	public String getPContent() {
		return pContent;
	}

	public void setPContent(String pContent) {
		this.pContent = pContent;
	}

	public SrcUserCard getSrcUserCard() {
		return srcUserCard;
	}

	public void setSrcUserCard(SrcUserCard srcUserCard) {
		this.srcUserCard = srcUserCard;
	}

	public SrcPostInfo getSrcPostAbstractCard() {
		return srcPostAbstractCard;
	}

	public void setSrcPostAbstractCard(SrcPostInfo srcPostAbstractCard) {
		this.srcPostAbstractCard = srcPostAbstractCard;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicListstr(){
		return picList;
	}

	public String[] getPicList()
	{
		return picsList;
	}

	public void setPicList(String picList)
	{
		this.picList = picList;
		picsList = picList.split("\\|");	
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getUserSeqId()
	{
		return userSeqId;
	}

	public void setUserSeqId(String userSeqId)
	{
		this.userSeqId = userSeqId;
	}

	public String getBackgroundPic()
	{
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic)
	{
		this.backgroundPic = backgroundPic;
	}

	public String getPostType()
	{
		return postType;
	}

	public void setPostType(String postType)
	{
		this.postType = postType;
	}

	public String getDeptIds()
	{
		return deptIds;
	}

	public void setDeptIds(String deptIds)
	{
		this.deptIds = deptIds;
	}

	public String getPostTitle()
	{
		return postTitle;
	}

	public void setPostTitle(String postTitle)
	{
		this.postTitle = postTitle;
	}

	public String getDeptNames()
	{
		return deptNames;
	}

	public void setDeptNames(String deptNames)
	{
		this.deptNames = deptNames;
	}

	public String getPostId()
	{
		return postId;
	}

	public void setPostId(String postId)
	{
		this.postId = postId;
	}

	public String getCompleteRate()
	{
		return completeRate;
	}

	public void setCompleteRate(String completeRate)
	{
		this.completeRate = completeRate;
	}

	@Override
	public String toString() {
		return "PostInfo [userSeqId=" + userSeqId + ", backgroundPic="
				+ backgroundPic + ", postType=" + postType + ", deptIds="
				+ deptIds + ", postTitle=" + postTitle + ", deptNames="
				+ deptNames + ", postId=" + postId + ", completeRate="
				+ completeRate + ", picList=" + picList + ", picsList="
				+ Arrays.toString(picsList) + ", createDate=" + createDate
				+ ", content=" + content + ", srcUserCard=" + srcUserCard
				+ ", srcPostAbstractCard=" + srcPostAbstractCard
				+ ", pUserSeqId=" + pUserSeqId + ", pUserName=" + pUserName
				+ ", postLevel=" + postLevel + ", srcPostId=" + srcPostId
				+ ", pContent=" + pContent + ", pIsDelete=" + pIsDelete
				+ ", srcIsDelete=" + srcIsDelete + ", pPostId=" + pPostId
				+ ", recommendUser=" + recommendUser + ", coterieName="
				+ coterieName + ", coterieId=" + coterieId + ", isOpen="
				+ isOpen + ", postAbstractList=" + postAbstractList + "]";
	}


	
	



	

	

	


}
