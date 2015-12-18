package com.szrjk.entity;

public class LikeInfo {

    private String createDate;
    private String postId;
    private String postType;
    private String srcPostId;
    private String userSeqId;


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getSrcPostId() {
        return srcPostId;
    }

    public void setSrcPostId(String srcPostId) {
        this.srcPostId = srcPostId;
    }

    public String getUserSeqId() {
        return userSeqId;
    }

    public void setUserSeqId(String userSeqId) {
        this.userSeqId = userSeqId;
    }

	@Override
	public String toString() {
		return "LikeInfo [createDate=" + createDate + ", postId=" + postId
				+ ", postType=" + postType + ", srcPostId=" + srcPostId
				+ ", userSeqId=" + userSeqId + "]";
	}
    
    
}
