package com.szrjk.entity;

import java.util.List;

public class PostDetail {

    private String content;
    private String userSeqId;
    private String postType;
    private String createDate;
    private String picList;
    private String postId;  


    /***
     * 转发时会要用到的字体现
     ***/
    
    private List<PostAbstractList> postAbstractList;
    private String pContent;
    private String pIsDelete;
    private String pPostId;
    private String pUserName;
    private String postLevel;
    private String srcIsDelete;
    private String pUserSeqId;

    private String srcPostId;

    private String coterieName;

    private String coterieId;

    private int isOpen;

    /**
     * 下面的字段是转发病例分享时的 原贴字段
     **/
    private String backgroundPic;
    private String completeRate;
    private String deptIds;
    private String deptNames;
    private String postTitle;






   

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserSeqId() {
        return userSeqId;
    }

    public void setUserSeqId(String userSeqId) {
        this.userSeqId = userSeqId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
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
		this.postAbstractList = postAbstractList;
	}

	public String getpContent() {
		return pContent;
	}

	public void setpContent(String pContent) {
		this.pContent = pContent;
	}

	public String getpIsDelete() {
		return pIsDelete;
	}

	public void setpIsDelete(String pIsDelete) {
		this.pIsDelete = pIsDelete;
	}

	public String getpPostId() {
		return pPostId;
	}

	public void setpPostId(String pPostId) {
		this.pPostId = pPostId;
	}

	public String getpUserName() {
		return pUserName;
	}

	public void setpUserName(String pUserName) {
		this.pUserName = pUserName;
	}

	public String getPostLevel() {
		return postLevel;
	}

	public void setPostLevel(String postLevel) {
		this.postLevel = postLevel;
	}

	public String getSrcIsDelete() {
		return srcIsDelete;
	}

	public void setSrcIsDelete(String srcIsDelete) {
		this.srcIsDelete = srcIsDelete;
	}

	public String getpUserSeqId() {
		return pUserSeqId;
	}

	public void setpUserSeqId(String pUserSeqId) {
		this.pUserSeqId = pUserSeqId;
	}

	public String getSrcPostId() {
		return srcPostId;
	}

	public void setSrcPostId(String srcPostId) {
		this.srcPostId = srcPostId;
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

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
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
    
	
   
}
