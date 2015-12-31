package com.szrjk.dhome;

/**
 * denggm on 2015/10/29.
 * DHome
 */
public interface IPostListCallback {

    /**
     * ��ȡ�����б�
     * @param userId2
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
    public void getNewPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


    /**
     * һ������ʱ���ȡ���б�
     * @param userId2
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
    public void getPosts(String userId2, String basePostId,
                            boolean isNew, String beginNum, String endNum);


    /**
     * ��ȡ������б�
     * @param userId
     * @param basePostId
     * @param isNew
     * @param beginNum
     * @param endNum
     */
	public void getMorePosts(String userId, String basePostId, boolean isNew,
			String beginNum, String endNum);
}
