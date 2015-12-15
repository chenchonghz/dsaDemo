package com.szrjk.entity;

public class PostList
{
	private UserCard userCard;
	private PostOtherImformationInfo statisInfo;
	private PostInfo abstractInfo;
	private boolean isMineLike;
	


	public boolean isMineLike() {
		return isMineLike;
	}

	public void setIsMineLike(boolean isMineLike) {
		this.isMineLike = isMineLike;
	}


	public UserCard getUserCard() {
		return userCard;
	}

	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}

	public PostOtherImformationInfo getStatisInfo()
	{
		return statisInfo;
	}

	public void setStatisInfo(PostOtherImformationInfo statisInfo)
	{
		this.statisInfo = statisInfo;
	}

	public PostInfo getAbstractInfo()
	{
		return abstractInfo;
	}

	public void setAbstractInfo(PostInfo abstractInfo)
	{
		this.abstractInfo = abstractInfo;
	}
}
