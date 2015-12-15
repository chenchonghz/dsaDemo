package com.szrjk.entity;

public class PostAbstractList implements Comparable{
	
	private String isDelete;
	private UserCard userCard;
	private String postLevel;
	private PostInfo postAbstract;
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public UserCard getUserCard() {
		return userCard;
	}
	public void setUserCard(UserCard userCard) {
		this.userCard = userCard;
	}
	public String getPostLevel() {
		return postLevel;
	}
	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}
	public PostInfo getPostAbstract() {
		return postAbstract;
	}
	public void setPostAbstract(PostInfo postAbstract) {
		this.postAbstract = postAbstract;
	}
	@Override
	public String toString() {
		return "PostAbstractList [isDelete=" + isDelete + ", userCard="
				+ userCard + ", postLevel=" + postLevel + ", postAbstract="
				+ postAbstract + "]";
	}
	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		PostAbstractList postList = (PostAbstractList)another;
		int otherLevel = Integer.valueOf(postList.getPostLevel());
		return Integer.valueOf(this.postLevel).compareTo(otherLevel);
	}
	
	

}
