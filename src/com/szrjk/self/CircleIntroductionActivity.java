package com.szrjk.self;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.CoterieMemberPortraitGridAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.Coterie;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.DDateUtils;
import com.szrjk.util.ImageLoaderUtil;

@ContentView(R.layout.activity_circle_introduction)
public class CircleIntroductionActivity extends BaseActivity {

	@ViewInject(R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(R.id.tv_edit)
	private TextView tv_edit;

	@ViewInject(R.id.iv_coterieFace)
	private ImageView iv_coterieFace;

	@ViewInject(R.id.tv_coterieName)
	private TextView tv_coterieName;

	@ViewInject(R.id.tv_createDate)
	private TextView tv_createDate;

	@ViewInject(R.id.rl_coterieMember)
	private RelativeLayout rl_coterieMember;

	@ViewInject(R.id.tv_memberCount)
	private TextView tv_memberCount;

	@ViewInject(R.id.tv_type)
	private TextView tv_type;
	@ViewInject(R.id.tv_dept)
	private TextView tv_dept;
	@ViewInject(R.id.tv_isOpen)
	private TextView tv_isOpen;

	@ViewInject(R.id.tv_coterieDesc)
	private TextView tv_coterieDesc;

	@ViewInject(R.id.gv_coterieFace)
	private GridView gv_coterieFace;

	private CircleIntroductionActivity instance;
	private Resources resources;
	private Coterie coterie;
	private UserInfo userInfo;
	private static final int GET_COTERIE_SUCCESS = 0;
	private static final int COTERIE_MEMBER_SUCCESS = 1;

	private static final int EDIT_DATA_SUCCESS = 2;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_COTERIE_SUCCESS:
				coterie = (Coterie) msg.obj;
				// Log.i("time1", System.currentTimeMillis()+"");
				setCoterieData();
				break;
			}
		};
	};

	private String coterieId;

	private CoterieMemberPortraitGridAdapter coterieMemberPortraitGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		initLayout();
	}

	private void initLayout() {
		resources = getResources();
		Intent intent = getIntent();
		coterieId = intent.getStringExtra(Constant.CIRCLE);
		userInfo = Constant.userInfo;
		loadCoterieData(coterieId);
		new Thread() {
			@Override
			public void run() {
				try {
					// Log.i("time2", System.currentTimeMillis()+"");
					Thread.sleep(500);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// Log.i("time3", System.currentTimeMillis()+"");
							if (coterie.getMemberType().equals("3")) {
								tv_edit.setVisibility(View.VISIBLE);
							}
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	private void setCoterieData() {
		try {
			new ImageLoaderUtil(instance, coterie.getCoterieFaceUrl(),
					iv_coterieFace, 0, 0).showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_coterieName.setText(coterie.getCoterieName());

		String createDate = coterie.getCreateDate();
		try {
			createDate = DDateUtils.dformatOldstrToNewstr(createDate,
					"yyyy-MM-dd HH:mm:ss", "yyyy年MM月dd日");
		} catch (ParseException e) {
			Log.e(this.getPackageName(), "", e);
		}
		tv_type.setText(coterie.getCoterieType().equals("1")?"个人":"组织/机构");
		if (coterie.getPropList().get(0).getPropertyId().equals("")) {
			tv_dept.setText("");
		}else{
			switch (Integer.valueOf(coterie.getPropList().get(0).getPropertyId())) {
			case 10:tv_dept.setText("内科");break;
			case 11:tv_dept.setText("外科");break;
			case 12:tv_dept.setText("妇科");break;
			case 13:tv_dept.setText("儿科");break;
			case 14:tv_dept.setText("更多");break;
		}
		}
		
		// Log.i("createDate", createDate);
		tv_createDate.setText("本圈创建于" + createDate);
		tv_memberCount.setText(coterie.getMemberCount() + "人");
		int length = coterie.getMemberCardList().size();
		String[] coterieFaceUrls = new String[length < 5 ? length : 5];
		for (int i = 0; i < (length < 5 ? length : 5); i++) {
			String coterieFaceUrl = coterie.getMemberCardList().get(i)
					.getUserFaceUrl();
			coterieFaceUrls[i] = coterieFaceUrl;
		}
		// Log.i("coterieFaceUrls", Arrays.toString(coterieFaceUrls));
		coterieMemberPortraitGridAdapter = new CoterieMemberPortraitGridAdapter(
				instance, coterieFaceUrls);
		gv_coterieFace.setAdapter(coterieMemberPortraitGridAdapter);
		tv_isOpen.setText(coterie.getIsOpen().equals("1") ? "公开" : "私密");
		tv_coterieDesc.setText(coterie.getCoterieDesc());
	}

	private void loadCoterieData(String coterieId) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getCoterieInfoById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("coterieId", coterieId);
		busiParams.put("memberLimitCount", "5");// 用于获取成员图片
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {

			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					JSONObject listOut = returnObj.getJSONObject("ListOut");
//					String coterieDesc = listOut.getString("coterieDesc");
//					String coterieName = listOut.getString("coterieName");
//					String coterieLevel = listOut.getString("coterieLevel");
//					String coterieFaceUrl = listOut.getString("coterieFaceUrl");
//					String coterieId = listOut.getString("coterieId");
//					String createDate = listOut.getString("createDate");
//					String memberCount = listOut.getString("memberCount");
//					String memberType = listOut.getString("memberType");
//					String isOpen = listOut.getString("isOpen");
//					JSONArray memberCardList = listOut
//							.getJSONArray("memberCardList");
//					List<UserCard> members = new ArrayList<UserCard>();
//					if (memberCardList != null && !memberCardList.isEmpty()) {
//						for (int i = 0; i < memberCardList.size(); i++) {
//							UserCard userCard = JSON.parseObject(
//									memberCardList.getString(i), UserCard.class);
//							members.add(userCard);
//						}
//					}
//					Coterie coterie1 = new Coterie();
//					coterie1.setCoterieDesc(coterieDesc);
//					coterie1.setCoterieFaceUrl(coterieFaceUrl);
//					coterie1.setCoterieId(coterieId);
//					coterie1.setCoterieName(coterieName);
//					coterie1.setCreateDate(createDate);
//					coterie1.setIsOpen(isOpen);
//					coterie1.setMemberCount(memberCount);
//					coterie1.setMemberType(memberType);
//					coterie1.setMemberCardList(members);
					// Log.i("coterie", coterie1.toString());
					Coterie coterie1=JSON.parseObject(
							returnObj.getString("ListOut"), Coterie.class);
					Log.i("TAG", coterie1.toString());
					Message message = new Message();
					message.what = GET_COTERIE_SUCCESS;
					message.obj = coterie1;
					handler.sendMessage(message);
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

	/** 点击返回 */
	@OnClick(R.id.iv_back)
	public void clickBack(View view) {
		setResult(EDIT_DATA_SUCCESS);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			setResult(EDIT_DATA_SUCCESS);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 点击圈子成员 */
	@OnClick(R.id.rl_coterieMember)
	public void clickCoterieMember(View view) {
		Intent intent = new Intent(instance, CoterieMemberActivity.class);
		intent.putExtra(Constant.CIRCLE, coterieId);
		intent.putExtra("memberCount", Integer.valueOf(coterie.getMemberCount()));
		startActivityForResult(intent, COTERIE_MEMBER_SUCCESS);
	}

	/** 点击编辑资料 */
	@OnClick(R.id.tv_edit)
	public void clickEditData(View view) {
		Intent intent = new Intent(instance, CreateCircleActivity.class);
		intent.putExtra("COTERIE", coterie);
		startActivityForResult(intent, EDIT_DATA_SUCCESS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case COTERIE_MEMBER_SUCCESS:
			if (data == null) {
				break;
			}
			Bundle bundle = data.getExtras();
			loadCoterieData(coterieId);
			tv_memberCount.setText(coterie.getMemberCount() + "人");
			coterieMemberPortraitGridAdapter.notifyDataSetChanged();
			break;
		case EDIT_DATA_SUCCESS:
			finish();
			break;
		}
	};

}
