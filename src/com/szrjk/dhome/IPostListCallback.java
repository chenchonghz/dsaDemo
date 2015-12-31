package com.szrjk.dhome;

/**
 * denggm on 2015/10/29.
 * DHome
 */
public interface IPostListCallback {

    /**
     * 获取最新列表
     * @param userId2
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
    public void getNewPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


    /**
     * 一进来的时候获取的列表
     * @param userId2
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
    public void getPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


    /**
     * 获取下面的列表
     * @param userId
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
	public void getMorePosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum);
}
