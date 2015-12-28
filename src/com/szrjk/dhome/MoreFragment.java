package com.szrjk.dhome;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.MineCount;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.message.MessageListActivity;
import com.szrjk.self.FriendActivity;
import com.szrjk.self.more.AboutActivity;
import com.szrjk.self.more.CaseSharePostActivity;
import com.szrjk.self.more.FeedbackActivity;
import com.szrjk.self.more.MainAuthenticationActivity;
import com.szrjk.self.more.MineGetLikeActivity;
import com.szrjk.self.more.MineSendLikeActivity;
import com.szrjk.self.more.MyAttentionActivity;
import com.szrjk.self.more.MyFansActivity;
import com.szrjk.self.more.NormalPostActivity;
import com.szrjk.self.more.PhotoAlbumActivity;
import com.szrjk.self.more.ProblemHelpActivity;
import com.szrjk.self.more.SetingActivity;
import com.szrjk.util.ImageLoaderUtil;

public class MoreFragment extends Fragment {
	private static final int CHANGE_PORTRAIT_SUCCESS = 0;
	@ViewInject(R.id.rl_home)
	private RelativeLayout rl_home;

	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	@ViewInject(R.id.iv_hportrait)
	private ImageView iv_hportrait;
	
	@ViewInject(R.id.iv_vip)
	private ImageView iv_vip;

	@ViewInject(R.id.ll_friend)
	private LinearLayout ll_friend;

	@ViewInject(R.id.tv_friend)
	private TextView tv_friend;

	@ViewInject(R.id.ll_attention)
	private LinearLayout ll_attention;

	@ViewInject(R.id.tv_attention)
	private TextView tv_attention;

	@ViewInject(R.id.ll_fans)
	private LinearLayout ll_fans;

	@ViewInject(R.id.tv_fans)
	private TextView tv_fans;

	@ViewInject(R.id.rl_letter)
	private RelativeLayout rl_letter;

	@ViewInject(R.id.rl_photo)
	private RelativeLayout rl_photo;

	@ViewInject(R.id.rl_profile)
	private RelativeLayout rl_profile;

	@ViewInject(R.id.rl_identification)
	private RelativeLayout rl_identification;

	@ViewInject(R.id.rl_post)
	private RelativeLayout rl_post;

	@ViewInject(R.id.tv_postCount)
	private TextView tv_postCount;

	@ViewInject(R.id.rl_sick)
	private RelativeLayout rl_sick;

	@ViewInject(R.id.tv_sickCount)
	private TextView tv_sickCount;

	@ViewInject(R.id.rl_help)
	private RelativeLayout rl_help;

	@ViewInject(R.id.tv_helpCount)
	private TextView tv_helpCount;

	@ViewInject(R.id.rl_send_like)
	private RelativeLayout rl_send_like;

	@ViewInject(R.id.tv_sendLikeCount)
	private TextView tv_sendLikeCount;

	@ViewInject(R.id.rl_get_like)
	private RelativeLayout rl_get_like;

	@ViewInject(R.id.tv_getLikeCount)
	private TextView tv_getLikeCount;

	@ViewInject(R.id.rl_comment)
	private RelativeLayout rl_comment;

	@ViewInject(R.id.rl_feed_back)
	private RelativeLayout rl_feed_back;

	@ViewInject(R.id.rl_about)
	private RelativeLayout rl_about;

	@ViewInject(R.id.rl_seting)
	private RelativeLayout rl_seting;

	public static MainActivity instance;

	private UserInfo userInfo;

	private Resources resources;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_more_fragment, null);
		ViewUtils.inject(this, view);
		instance = (MainActivity) getActivity();
		initLayout();
		return view;
	}

	private void initLayout() {
		userInfo = Constant.userInfo;
		if (userInfo == null)
			return;
		resources = getResources();
		try {
			// Log.i("head", userInfo.getUserFaceUrl());
			setPortrait();
		} catch (Exception e) {
			Log.e("ImageLoader", e.toString());
		}
		tv_name.setText(userInfo.getUserName());
		queryFriendFollowFans();
		queryMineCount();

	}

	public void setPortrait() {
		ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil(instance,
				userInfo.getUserFaceUrl(), iv_hportrait, R.drawable.header,
				R.drawable.header);
		imageLoaderUtil.showImage();
		if (userInfo.getUserLevel().equals("11")) {
			iv_vip.setVisibility(View.VISIBLE);
		}
	}

	/** 查询好友、关注、粉丝数 */
	public void queryFriendFollowFans() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getUserRelByUserId");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					// 用户好友数
					Integer userFriendCount = returnObj
							.getInteger("userFriendCount");
					// 用户的粉丝数
					Integer followUserCount = returnObj
							.getInteger("followUserCount");
					// 用户关注的人数
					Integer userFollowCount = returnObj
							.getInteger("userFollowCount");
					tv_friend.setText(userFriendCount + "");
					tv_attention.setText(userFollowCount + "");
					tv_fans.setText(followUserCount + "");
				}
			}

			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
		});
	}

	/** 查询三大帖子数，赞数 */
	public void queryMineCount() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryMinePostCounts");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					JSONArray listOut = returnObj.getJSONArray("ListOut");
					JSONObject object = (JSONObject) listOut.get(0);
					MineCount mineCount = new MineCount();
					mineCount.setMineNormal(object.getIntValue("mineNormal"));
					mineCount.setMineIllcase(object.getIntValue("mineIllcase"));
					mineCount.setMinePuzzle(object.getIntValue("minePuzzle"));
					mineCount.setMineSendLike(object
							.getIntValue("mineSendLike"));
					mineCount.setMineReceivelike(object
							.getIntValue("mineReceivelike"));
					tv_postCount.setText("(" + mineCount.getMineNormal() + ")");
					tv_sickCount.setText("(" + mineCount.getMineIllcase() + ")");
					tv_helpCount.setText("(" + mineCount.getMinePuzzle() + ")");
					tv_sendLikeCount.setText("(" + mineCount.getMineSendLike()
							+ ")");
					tv_getLikeCount.setText("("
							+ mineCount.getMineReceivelike() + ")");
				}
			}

			@Override
			public void start() {
			}

			@Override
			public void loading(long total, long current, boolean isUploading) {
			}

			@Override
			public void failure(HttpException exception, JSONObject jobj) {
			}
		});
	}

	@OnClick(R.id.rl_home)
	public void homeClick(View view) {
		// finish();
		Intent intent = new Intent(instance, SelfActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.ll_friend)
	public void friendClick(View view) {
		Intent intent = new Intent(instance, FriendActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.ll_attention)
	public void attentionClick(View view) {
		Intent intent = new Intent(instance, MyAttentionActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userInfo.getUserSeqId());
		startActivity(intent);
	}

	@OnClick(R.id.ll_fans)
	public void fansClick(View view) {
		Intent intent = new Intent(instance, MyFansActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userInfo.getUserSeqId());
		startActivity(intent);
	}

	@OnClick(R.id.rl_photo)
	public void rl_photoClick(View view) {
		Intent intent = new Intent(instance, PhotoAlbumActivity.class);
		startActivity(intent);
		// ToastUtils.showMessage(instance, "敬请期待！");
	}

	@OnClick(R.id.rl_letter)
	public void letterClick(View view) {
		Intent intent = new Intent(instance, MessageListActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_profile)
	public void profileClick(View view) {
		Intent intent = new Intent(instance, IntroduceActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userInfo.getUserSeqId());
		startActivity(intent);
	}

	@OnClick(R.id.rl_identification)
	public void identificationClick(View view) {
		Intent intent = new Intent(instance, MainAuthenticationActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_post)
	public void postClick(View view) {
		Intent intent = new Intent(instance, NormalPostActivity.class);
		intent.putExtra("postType", "MINE");
		startActivity(intent);
	}

	@OnClick(R.id.rl_sick)
	public void sickClick(View view) {
		Intent intent = new Intent(instance, CaseSharePostActivity.class);
		intent.putExtra("postType", "MINE");
		startActivity(intent);
	}

	@OnClick(R.id.rl_help)
	public void helpClick(View view) {
		Intent intent = new Intent(instance, ProblemHelpActivity.class);
		intent.putExtra("postType", "MINE");
		startActivity(intent);
	}

	@OnClick(R.id.rl_send_like)
	public void sendLikeClick(View view) {
		Intent intent = new Intent(instance, MineSendLikeActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_get_like)
	public void getLikeClick(View view) {
		Intent intent = new Intent(instance, MineGetLikeActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_feed_back)
	public void feedBackClick(View view) {
		Intent intent = new Intent(instance, FeedbackActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.rl_comment)
	public void commentClick(View view) {
		
	}

	@OnClick(R.id.rl_about)
	public void aboutClick(View view) {
		Intent intent = new Intent(instance, AboutActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.rl_seting)
	public void setingClick(View view) {
		Intent intent = new Intent(instance, SetingActivity.class);
		startActivityForResult(intent, CHANGE_PORTRAIT_SUCCESS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CHANGE_PORTRAIT_SUCCESS:
			if (data == null) {
				break;
			}
			Bundle bundle = data.getExtras();
			if (bundle.getBoolean("CHANGE_PORTRAIT_SUCCESS")) {
				Intent intent = new Intent();
				intent.putExtras(bundle);
				instance.setResult(CHANGE_PORTRAIT_SUCCESS, intent);
				// finish();
			}
			break;
		}
	}
}
