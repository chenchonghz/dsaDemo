package com.szrjk.entity;


/**
 * 帖子其他信息实体类
 * 
 * @author Liyi
 * 
 */
public class PostOtherImformationInfo 
{

	// 转发数
	private int FORWARD_NUM;
	// 帖子ID
	private String POST_ID;
	// 点赞数量
	private int LIKE_NUM;
	// 评论数量
	private int COMMENT_NUM;
	// 阅读量
	private int READ_NUM;
	//是否已经点赞过
	private boolean isMineLike;
	
	

	public boolean isMineLike() {
		return isMineLike;
	}

	public void setMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}

	public int getFORWARD_NUM()
	{
		return FORWARD_NUM;
	}

	public void setFORWARD_NUM(int fORWARD_NUM)
	{
		FORWARD_NUM = fORWARD_NUM;
	}

	public String getPOST_ID()
	{
		return POST_ID;
	}

	public void setPOST_ID(String pOST_ID)
	{
		POST_ID = pOST_ID;
	}

	public int getLIKE_NUM()
	{
		return LIKE_NUM;
	}

	public void setLIKE_NUM(int lIKE_NUM)
	{
		LIKE_NUM = lIKE_NUM;
	}

	public int getCOMMENT_NUM()
	{
		return COMMENT_NUM;
	}

	public void setCOMMENT_NUM(int cOMMENT_NUM)
	{
		COMMENT_NUM = cOMMENT_NUM;
	}

	public int getREAD_NUM()
	{
		return READ_NUM;
	}

	public void setREAD_NUM(int rEAD_NUM)
	{
		READ_NUM = rEAD_NUM;
	}

	@Override
	public String toString() {
		return "PostOtherImformationInfo [FORWARD_NUM=" + FORWARD_NUM
				+ ", POST_ID=" + POST_ID + ", LIKE_NUM=" + LIKE_NUM
				+ ", COMMENT_NUM=" + COMMENT_NUM + ", READ_NUM=" + READ_NUM
				+ ", isMineLike=" + isMineLike + "]";
	}

	
}
