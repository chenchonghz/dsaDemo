package com.szrjk.entity;

public class CommentInfo {

    private String content;
    private String createDate;
    private String level;
    private String pPostId;
    private UserCard pUserCard;
    private String postId;//当前评论的id
    private String postType;
    private String srcPostId;//原贴的id

    public String getSrcPostId() {
        return srcPostId;
    }

    public void setSrcPostId(String srcPostId) {
        this.srcPostId = srcPostId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getPostId() {
        return postId;
    }

    public String getpPostId() {
        return pPostId;
    }

    public void setpPostId(String pPostId) {
        this.pPostId = pPostId;
    }

    public UserCard getpUserCard() {
        return pUserCard;
    }

    public void setpUserCard(UserCard pUserCard) {
        this.pUserCard = pUserCard;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


}
