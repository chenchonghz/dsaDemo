package com.szrjk.entity;

import java.util.Collections;
import java.util.List;

public class ForwardInfo {

	private String postType;
	private String postId;
	private List<PostAbstractList> postAbstractList;
	public String getPostType() {
		return postType;
	}
	public void setPostType(String postType) {
		this.postType = postType;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	
	public List<PostAbstractList> getPostAbstractList() {
		return postAbstractList;
	}
	public void setPostAbstractList(List<PostAbstractList> postAbstractList) {
		Collections.reverse(postAbstractList);
		this.postAbstractList = postAbstractList;
	}
	@Override
	public String toString() {
		return "ForwardInfo [postType=" + postType + ", postId=" + postId
				+ ", postAbstractList=" + postAbstractList + "]";
	}
	
	
} 
