package com.szrjk.dhome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import com.szrjk.config.ConstantUser;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.adapter.IndexListViewAdapter;
import com.szrjk.config.Constant;
import com.szrjk.entity.*;
import com.szrjk.pull.PullToRefreshBase;
import com.szrjk.pull.PullToRefreshListView;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.self.more.NormalPostActivity;
import com.szrjk.util.ShowDialogUtil;
import com.szrjk.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * denggm on 2015/10/29.
 * DHome
 */
public class PostListComm {


    private String mMaxPostId; // 最大postId
    private String mMinPostId; // 最小postId
    private Context context;
    private ArrayList<UserCard> userList; // 医生信息
    private ArrayList<PostInfo> postList; // 帖子详情
    private ArrayList<PostOtherImformationInfo> postOtherList; // 转发、评论、赞信息
    @ViewInject(R.id.lv_postlist)
    private PullToRefreshListView mPullRefreshListView;
    private ListView lv_postlist;
    private static final String HAVE_NOT_MORE_POST = "没有更多帖子了";
    private static final String LOADING_POST = "正在加载帖子";
    private boolean firstIn = true;  //标记是否下拉刷新
    private Dialog dialog = null;
    private IndexListViewAdapter adapter;
    private Fragment fragment;
    private Activity activity;
    private String userId;

    private IPostListCallback iPostListCallback;


    
    public void updateData(){
    	if(adapter != null){
    		adapter.notifyDataSetChanged();
    	}
    }
    
    public ArrayList<UserCard> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<UserCard> userList) {
		this.userList = userList;
	}

	public ArrayList<PostInfo> getPostList() {
		return postList;
	}

	public void setPostList(ArrayList<PostInfo> postList) {
		this.postList = postList;
	}
	

	public void  destroy(){
        activity = null;
        userList = null;
        postList = null;
        postOtherList = null;
        dialog = null;
        userId = null;
    }

    public PostListComm(Activity activity,String userId,PullToRefreshListView mPullRefreshListView,IPostListCallback iPostListCallback) {
        this.activity = activity;
        this.userId = userId;
        this.mPullRefreshListView = mPullRefreshListView;
        this.iPostListCallback = iPostListCallback;
        //帖子列表处理
        initData();
        lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
        String basePostId = "0";
        boolean isNew = true;
        iPostListCallback.getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
        setListner();//列表的监听
    }
    
    
    public PostListComm(Activity activity,String userId,Fragment fragment,PullToRefreshListView mPullRefreshListView,IPostListCallback iPostListCallback) {
    	this.activity = activity;
        this.userId = userId;
        this.mPullRefreshListView = mPullRefreshListView;
        this.iPostListCallback = iPostListCallback;
        //帖子列表处理
        initData();
        lv_postlist = mPullRefreshListView.getRefreshableView(); // 获得listView对象
        String basePostId = "0";
        boolean isNew = true;
        iPostListCallback.getPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
        setListner();//列表的监听
        this.fragment = fragment;
    }
    

    /**
     * 初始化数据
     */
    public void initData()
    {
        context = activity;
        userList = new ArrayList<UserCard>();
        postList = new ArrayList<PostInfo>();
        postOtherList = new ArrayList<PostOtherImformationInfo>();
        mPullRefreshListView
                .setMode(com.szrjk.pull.PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel(activity.getResources().getString(R.string.pull_down_lable_text));
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
                activity.getResources().getString(R.string.pull_up_lable_text));
        mPullRefreshListView.getLoadingLayoutProxy(true, true)
                .setRefreshingLabel(
                        activity.getResources()
                                .getString(R.string.refreshing_lable_text));
        mPullRefreshListView.getLoadingLayoutProxy(true, true)
                .setReleaseLabel(
                        activity.getResources().getString(R.string.release_lable_text));
        dialog = ShowDialogUtil.createDialog(context, LOADING_POST);
    }


    /**
     * 设置各种监听
     */
    private void setListner()
    {
        // 设置listView上下拉监听
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (mPullRefreshListView.isHeaderShown()) {
                            boolean isNew = true;
                            String basePostId = mMaxPostId;
                            iPostListCallback.getNewPosts(userId, basePostId, isNew, Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);

                        } else if (mPullRefreshListView.isFooterShown()) {
                            boolean isNew = false;
                            String basePostId = mMinPostId;
                            iPostListCallback.getMorePosts(userId, mMinPostId, isNew,
                                    Constant.POST_BEGIN_NUM, Constant.POST_END_NUM);
                        }
                    }
                });
    }



    private Handler handler = new Handler()
    {

        @Override
		public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Constant.HAVE_NEW_POST:
                    if(firstIn){
                    	if(activity instanceof CircleHomepageActivity){
                    		 adapter = new IndexListViewAdapter(
                                     context, activity, userList, postList, postOtherList, userId,Constant.CIRCLE_FLAG,new IPullPostListCallback() {
                                 @Override
                                 public void skipToSelfFragment() {
                                	 Intent intent = new Intent(context, MainActivity.class);
                                     intent.putExtra("isSkipToSelf", true);
                                     context.startActivity(intent);
                                 }
                             });
                    	}else if(activity instanceof OtherPeopleActivity){
                        adapter = new IndexListViewAdapter(
                                context, activity, userList, postList, postOtherList, userId,Constant.OTHER_FLAG, new IPullPostListCallback() {
                            @Override
                            public void skipToSelfFragment() {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("isSkipToSelf", true);
                                context.startActivity(intent);
                            }
                        });
                    	}else if(activity instanceof NormalPostActivity){
                    		adapter = new IndexListViewAdapter(
                                    context, activity, userList, postList, postOtherList, userId,ConstantUser.MyNormalPost, new IPullPostListCallback() {
                                @Override
                                public void skipToSelfFragment() {
                                }
                            });
                    	}
                    	else{
                    		 adapter = new IndexListViewAdapter(
                                     context, activity, userList, postList, postOtherList, userId,Constant.UNKNOW_FLAG, new IPullPostListCallback() {
                                 @Override
                                 public void skipToSelfFragment() {
                                     
                                 }
                             });
                    	}
                        lv_postlist.setAdapter(adapter);
                        firstIn = false;
                    }
                    adapter.notifyDataSetChanged();
//                    if(getMaxPostId()!=null){   	
//                    	mMaxPostId = getMaxPostId();
//                    }else{
//                    	mMaxPostId = "0";
//                    }
//                    if(getMinPostId()!=null){     	
//                    	mMinPostId = getMinPostId();
//                    }else{
//                    	mMinPostId = "0";
//                    }
                    break;
                case Constant.NOT_NEW_POST:
                    if(firstIn){
                    	if(activity instanceof CircleHomepageActivity){
                   		 adapter = new IndexListViewAdapter(
                                    context, activity, userList, postList, postOtherList, userId,Constant.CIRCLE_FLAG,new IPullPostListCallback() {
                                @Override
                                public void skipToSelfFragment() {
                                	Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("isSkipToSelf", true);
                                    context.startActivity(intent);
                                }
                            });
                   	}else if(activity instanceof OtherPeopleActivity){
                        adapter = new IndexListViewAdapter(
                                context, activity, userList, postList, postOtherList, userId,Constant.OTHER_FLAG, new IPullPostListCallback() {
                            @Override
                            public void skipToSelfFragment() {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("isSkipToSelf", true);
                                context.startActivity(intent);
                            }
                        });
                    	}else if(activity instanceof NormalPostActivity){
                    		adapter = new IndexListViewAdapter(
                                    context, activity, userList, postList, postOtherList, userId,ConstantUser.MyNormalPost, new IPullPostListCallback() {
                                @Override
                                public void skipToSelfFragment() {
                                }
                            });
                    	}else{
                    		 adapter = new IndexListViewAdapter(
                                     context, activity, userList, postList, postOtherList, userId,Constant.UNKNOW_FLAG, new IPullPostListCallback() {
                                 @Override
                                 public void skipToSelfFragment() {
                                     
                                 }
                             });
                    	}
                        lv_postlist.setAdapter(adapter);
                        firstIn = false;
                        adapter.notifyDataSetChanged();
                        
                    }else{
                    	ToastUtils.showMessage(context, HAVE_NOT_MORE_POST);
                    }
                    break;
                case Constant.HAVE_NEW_POST_BY_REFRESH:
                     	if(activity instanceof CircleHomepageActivity){
                     		 adapter = new IndexListViewAdapter(
                                      context, activity, userList, postList, postOtherList, userId,Constant.CIRCLE_FLAG,new IPullPostListCallback() {
                                  @Override
                                  public void skipToSelfFragment() {
                                	  Intent intent = new Intent(context, MainActivity.class);
                                      intent.putExtra("isSkipToSelf", true);
                                      context.startActivity(intent);
                                  }
                              });
                     	}else if(activity instanceof OtherPeopleActivity){
                            adapter = new IndexListViewAdapter(
                                    context, activity, userList, postList, postOtherList, userId, Constant.OTHER_FLAG,new IPullPostListCallback() {
                                @Override
                                public void skipToSelfFragment() {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra("isSkipToSelf", true);
                                    context.startActivity(intent);
                                }
                            });
                        	}else if(activity instanceof NormalPostActivity){
                        		adapter = new IndexListViewAdapter(
                                        context, activity, userList, postList, postOtherList, userId,ConstantUser.MyNormalPost, new IPullPostListCallback() {
                                    @Override
                                    public void skipToSelfFragment() {
                                    }
                                });
                        	}
                     	else{
                        		 adapter = new IndexListViewAdapter(
                                         context, activity, userList, postList, postOtherList, userId,Constant.UNKNOW_FLAG, new IPullPostListCallback() {
                                     @Override
                                     public void skipToSelfFragment() {
                                         
                                     }
                                 });
                        	}
                         lv_postlist.setAdapter(adapter);
                	 adapter.notifyDataSetChanged();
//                	 if(getMaxPostId()!=null){   	
//                     	mMaxPostId = getMaxPostId();
//                     }else{
//                     	mMaxPostId = "0";
//                     }
//                     if(getMinPostId()!=null){     	
//                     	mMinPostId = getMinPostId();
//                     }else{
//                     	mMinPostId = "0";
//                     }
                	break;
                
            }

        };
    };


    /**
     * 获取最小的postId值
     *
     * @return
     */
//    protected String getMinPostId()
//    {
//        BigDecimal minPostId = null;
//        if (postList != null)
//        {
//        	if(postList.get(0).getPostId()!=null){       		
//        		minPostId = new BigDecimal(postList.get(0).getPostId());
//        		for (int i = 1; i < postList.size(); i++)
//        		{
//        			if(postList.get(i).getPostId()!=null){
//        				BigDecimal bd = new BigDecimal(postList.get(i).getPostId());
//        				minPostId = minPostId.compareTo(bd) == -1 ? minPostId : bd;
//        			}else{
//        				minPostId = new BigDecimal(0);
//        			}
//        		}
//        	}else{
//        		minPostId = new BigDecimal(0);
//        	}
//        }
//        System.out.println("最小：" + minPostId);
//        return minPostId.toString();
//    }

    /**
     * 获取最大的postId
     *
     * @return
     */
//    protected String getMaxPostId()
//    {
//        BigDecimal maxPostId = null;
//        if (postList != null)
//        {
//        	if(postList.get(0).getPostId()!=null){		
//        		maxPostId = new BigDecimal(postList.get(0).getPostId());
//        		for (int i = 1; i < postList.size(); i++)
//                {
//        			if(postList.get(i).getPostId()!=null){				
//        				BigDecimal bd = new BigDecimal(postList.get(i).getPostId());
//        				maxPostId = maxPostId.compareTo(bd) == 1 ? maxPostId : bd;
//        			}else{
//        				maxPostId = new BigDecimal(0);
//        			}
//                }
//        	}else{
//        		maxPostId = new BigDecimal(0);
//        	}       
//        }
//        System.out.println("最大：" + maxPostId);
//        return maxPostId.toString();
//    }

    public void operPostsSucc(List<PostList> postLists){
        if (!postLists.isEmpty() && postLists != null) {

            for (PostList list : postLists) {
                UserCard doctorInfo = list.getUserCard();
                userList.add(doctorInfo);
                PostInfo postInfo = list.getAbstractInfo();
                postList.add(postInfo);
                boolean isMineLike = list.isMineLike();
                PostOtherImformationInfo postOtherInfo = list
                        .getStatisInfo();
                if(postOtherInfo != null){	
                	postOtherInfo.setMineLike(isMineLike);
                }
                postOtherList.add(postOtherInfo);
            }
			if(postList.get(0).getPostId()!= null){
				mMaxPostId = postList.get(0).getPostId();
			}else{
				mMaxPostId = "0";
			}
			if(postList.get(postList.size()-1).getPostId()!=null){
				mMinPostId = postList.get(postList.size()-1).getPostId();
			}else{
				mMinPostId = "0";
			}
            Log.e("userList", userList.toString());
            Log.e("postList",postList.toString());
            Log.e("postOtherList",postOtherList.toString());
            handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
            if (mPullRefreshListView.isRefreshing()) {
                mPullRefreshListView.onRefreshComplete();
            }
        } else {
        	mMaxPostId = "0";
        	mMinPostId = "0";
            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
            if (mPullRefreshListView.isRefreshing()) {
                mPullRefreshListView.onRefreshComplete();
            }
        }

    }
    
    public void operMorePostsSucc(List<PostList> postLists){
        if (!postLists.isEmpty() && postLists != null) {

            for (PostList list : postLists) {
                UserCard doctorInfo = list.getUserCard();
                userList.add(doctorInfo);
                PostInfo postInfo = list.getAbstractInfo();
                postList.add(postInfo);
                boolean isMineLike = list.isMineLike();
                PostOtherImformationInfo postOtherInfo = list
                        .getStatisInfo();
                if(postOtherInfo != null){	
                	postOtherInfo.setMineLike(isMineLike);
                }
                postOtherList.add(postOtherInfo);
            }
            if(postList.get(postList.size()-1).getPostId()!=null){	
				mMinPostId = postList.get(postList.size()-1).getPostId();
			}else{
				mMinPostId = "0";
			}
            Log.e("userList", userList.toString());
            Log.e("postList",postList.toString());
            Log.e("postOtherList",postOtherList.toString());
            handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
            if (mPullRefreshListView.isRefreshing()) {
                mPullRefreshListView.onRefreshComplete();
            }
        } else {
            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
            if (mPullRefreshListView.isRefreshing()) {
                mPullRefreshListView.onRefreshComplete();
            }
        }

    }


    public void operNewPostsSucc(List<PostList> postLists){

        if(!postLists.isEmpty()&& postLists != null){
            for (PostList list : postLists)
            {
                UserCard doctorInfo = list.getUserCard();
                userList.add(0, doctorInfo);
                PostInfo postInfo = list.getAbstractInfo();
                postList.add(0, postInfo);
                boolean isMineLike = list.isMineLike();
				PostOtherImformationInfo postOtherInfo = list
						.getStatisInfo();
				if(postOtherInfo!=null){
					postOtherInfo.setMineLike(isMineLike);
				}
				postOtherList.add(0, postOtherInfo);
            }
            if(postList.get(0).getPostId()!=null){		
				mMaxPostId = postList.get(0).getPostId();
			}else{
				mMaxPostId = "0";
			}
            Log.e("userList", userList.toString());
            Log.e("postList",postList.toString());
            Log.e("postOtherList",postOtherList.toString());
            handler.sendEmptyMessage(Constant.HAVE_NEW_POST_BY_REFRESH );
            operrefreshComplete();
        }else{
            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
            operrefreshComplete();
        }
    }

    public void operNOT_NEW_POST(){
        handler.sendEmptyMessage(Constant.NOT_NEW_POST);
    }


    

    public void operrefreshComplete(){
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }
        if (mPullRefreshListView.isRefreshing())
        {
            mPullRefreshListView.onRefreshComplete();
        }
    }


	public ArrayList<PostOtherImformationInfo> getPostOtherList() {
		return postOtherList;
	}


	public void setPostOtherList(ArrayList<PostOtherImformationInfo> postOtherList) {
		this.postOtherList = postOtherList;
	}


	public IndexListViewAdapter getAdapter() {
		return adapter;
	}

	public void operPostsSucc(List<PostList> postLists, String normalPost) {
		 if (!postLists.isEmpty() && postLists != null) {

	            for (PostList list : postLists) {
	                UserCard doctorInfo = list.getUserCard();
	                userList.add(doctorInfo);
	                PostInfo postInfo = list.getAbstractInfo();
	                postInfo.setPostType(normalPost);
	                postList.add(postInfo);
	                boolean isMineLike = list.isMineLike();
	                PostOtherImformationInfo postOtherInfo = list
	                        .getStatisInfo();
	                if(postOtherInfo != null){	
	                	postOtherInfo.setMineLike(isMineLike);
	                }
	                postOtherList.add(postOtherInfo);
	            }
	            if(postList.get(0).getPostId()!= null){
					mMaxPostId = postList.get(0).getPostId();
				}else{
					mMaxPostId = "0";
				}
				if(postList.get(postList.size()-1).getPostId()!=null){
					mMinPostId = postList.get(postList.size()-1).getPostId();
				}else{
					mMinPostId = "0";
				}
	            Log.e("userList", userList.toString());
	            Log.e("postList",postList.toString());
	            Log.e("postOtherList",postOtherList.toString());
	            handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
	            if (mPullRefreshListView.isRefreshing()) {
	                mPullRefreshListView.onRefreshComplete();
	            }
	        } else {
            	mMaxPostId = "0";
            	mMinPostId = "0";
	            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
	            if (mPullRefreshListView.isRefreshing()) {
	                mPullRefreshListView.onRefreshComplete();
	            }
	        }
	}

	public void operNewPostsSucc(List<PostList> postLists, String normalPost) {
		// TODO Auto-generated method stub
		if(!postLists.isEmpty()&& postLists != null){
            for (PostList list : postLists)
            {
                UserCard doctorInfo = list.getUserCard();
                userList.add(0, doctorInfo);
                PostInfo postInfo = list.getAbstractInfo();
                postInfo.setPostType(normalPost);
                postList.add(0, postInfo);
                boolean isMineLike = list.isMineLike();
				PostOtherImformationInfo postOtherInfo = list
						.getStatisInfo();
				if(postOtherInfo!=null){
					postOtherInfo.setMineLike(isMineLike);
				}
				postOtherList.add(0, postOtherInfo);
            }
            if(postList.get(0).getPostId()!=null){		
				mMaxPostId = postList.get(0).getPostId();
			}else{
				mMaxPostId = "0";
			}
            Log.e("userList", userList.toString());
            Log.e("postList",postList.toString());
            Log.e("postOtherList",postOtherList.toString());
            handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
            operrefreshComplete();
        }else{
            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
            operrefreshComplete();
        }
	}

	public void operMorePostsSucc(List<PostList> postLists, String normalPost) {
		// TODO Auto-generated method stub
		 if (!postLists.isEmpty() && postLists != null) {

	            for (PostList list : postLists) {
	                UserCard doctorInfo = list.getUserCard();
	                userList.add(doctorInfo);
	                PostInfo postInfo = list.getAbstractInfo();
	                postInfo.setPostType(normalPost);
	                postList.add(postInfo);
	                boolean isMineLike = list.isMineLike();
	                PostOtherImformationInfo postOtherInfo = list
	                        .getStatisInfo();
	                if(postOtherInfo != null){	
	                	postOtherInfo.setMineLike(isMineLike);
	                }
	                postOtherList.add(postOtherInfo);
	            }
	            if(postList.get(postList.size()-1).getPostId()!=null){	
					mMinPostId = postList.get(postList.size()-1).getPostId();
				}else{
					mMinPostId = "0";
				}
	            Log.e("userList", userList.toString());
	            Log.e("postList",postList.toString());
	            Log.e("postOtherList",postOtherList.toString());
	            handler.sendEmptyMessage(Constant.HAVE_NEW_POST);
	            if (mPullRefreshListView.isRefreshing()) {
	                mPullRefreshListView.onRefreshComplete();
	            }
	        } else {
	            handler.sendEmptyMessage(Constant.NOT_NEW_POST);
	            if (mPullRefreshListView.isRefreshing()) {
	                mPullRefreshListView.onRefreshComplete();
	            }
	        }
	}
    
    


}
