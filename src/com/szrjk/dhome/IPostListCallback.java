package com.szrjk.dhome;

/**
 * denggm on 2015/10/29.
 * DHome
 */
public interface IPostListCallback {

    public void getNewPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


    public void getPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


	public void getMorePosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum);
}
