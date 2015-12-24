package com.szrjk.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.adapter.CoterieMemberListAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.SelfActivity;
import com.szrjk.entity.Coterie;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.entity.UserInfo;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.util.SetListViewHeightUtils;
import com.szrjk.widget.CreatorManagePopup;
import com.szrjk.widget.OrdinaryMemberManagePopup;
import com.szrjk.widget.UserCardLayout;

@ContentView(R.layout.activity_coterie_member)
public class CoterieMemberActivity extends BaseActivity{
	
	@ViewInject(R.id.ll_coterie_member)
	private LinearLayout ll_coterie_member;
	
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	
	@ViewInject(R.id.tv_manage)
	private TextView tv_manage;
	
	@ViewInject(R.id.tv_memberCount)
	private TextView tv_memberCount;
	
	@ViewInject(R.id.ucl_usercardlayout)
	private UserCardLayout ucl_usercardlayout;
	
	@ViewInject(R.id.lv_coteriemember)
	private ListView lv_coteriemember;
	
	private CoterieMemberActivity instance;

	private UserInfo userInfo;
	private Coterie coterie;
	private static final int GET_COTERIE_SUCCESS=0;

	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_COTERIE_SUCCESS:
				coterie=(Coterie) msg.obj;
				setCoterieData();
				break;
			}
		}

	};

	private CreatorManagePopup creatorManagePopup;

	private OrdinaryMemberManagePopup ordinaryMemberManagePopup;

	private Resources resources;

	private CoterieMemberListAdapter coterieMemberListAdapter;

	private int memberCount;

	private String coterieId;
	
	private List<UserCard> memberCardList;

	private static final int COTERIE_MEMBER_SUCCESS=1;
	
	private static final int DELETE_COTERIE_MEMBER_SUCCESS=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance=this;
		initLayout();
	}

	private void initLayout() {
		resources=getResources();
		userInfo=Constant.userInfo;
		Intent intent=getIntent();
		coterieId = intent.getStringExtra(Constant.CIRCLE);
		memberCount=intent.getIntExtra("memberCount", 0);
		loadCoterieData(coterieId,memberCount-1);
	}
	private UserCard creator;
	private void setCoterieData() {
		String isMember = coterie.getIsMember();
		if (!isMember.equals("true")) {
			tv_manage.setVisibility(View.GONE);
		}
		creator = coterie.getCreator();
		ucl_usercardlayout.setContext(instance);
		/*TextView professionalTitle = (TextView) findViewById(R.id.tv_user_type);
		professionalTitle.setTextColor(resources.getColor(R.color.red));*/
		ucl_usercardlayout.setUser(creator);
		ucl_usercardlayout.closeClick();
		ucl_usercardlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.ucl_usercardlayout:
					if (Constant.userInfo.getUserSeqId().equals(creator.getUserSeqId())){
						Intent intent = new Intent(instance, SelfActivity.class);
						startActivity(intent);
						break;
					}
					Intent intent = new Intent(instance, OtherPeopleActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID,creator.getUserSeqId());
					startActivity(intent);
					break;

				}
			}
		});
		tv_memberCount.setText(Integer.parseInt(coterie.getMemberCount())-1+"");
		memberCardList= coterie.getMemberCardList();
		coterieMemberListAdapter=new CoterieMemberListAdapter(instance, memberCardList);
		lv_coteriemember.setAdapter(coterieMemberListAdapter);
		SetListViewHeightUtils.setListViewHeight(lv_coteriemember);
		lv_coteriemember.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (Constant.userInfo.getUserSeqId().equals(memberCardList.get(arg2).getUserSeqId())){
					Intent intent = new Intent(instance, SelfActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(instance, OtherPeopleActivity.class);
					intent.putExtra(Constant.USER_SEQ_ID,memberCardList.get(arg2).getUserSeqId());
					startActivity(intent);
				}
				
				
			}
		});
	};
	
	private void loadCoterieData(String coterieId,int memberCount) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getCoterieMemberListById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("coterieId", coterieId);
		busiParams.put("baseUserId", "0");
		busiParams.put("isNew", "true");
		busiParams.put("beginNum", "0");
		busiParams.put("sizeNum", "500");
		busiParams.put("userSeqId", userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					JSONObject listOut = returnObj.getJSONObject("ListOut");
					String memberType=listOut.getString("memberType");
					String isMember=listOut.getString("isMember");
					String coterieId=listOut.getString("coterieId");
					UserCard creator=JSON.parseObject(listOut.getString("creator"), UserCard.class);
					String memberCount=listOut.getString("memberCount");
					JSONArray memberCardList=listOut.getJSONArray("memberCardList");
					List<UserCard> members=new ArrayList<UserCard>();
					if (memberCardList!=null&&!memberCardList.isEmpty()) {
						for (int i = 0; i < memberCardList.size(); i++) {
							UserCard userCard=JSON.parseObject(memberCardList.getString(i), UserCard.class);
							members.add(userCard);
						}
					}
					Coterie coterie1=new Coterie();
					coterie1.setMemberType(memberType);
					coterie1.setIsMember(isMember);
					coterie1.setCoterieId(coterieId);
					coterie1.setCreator(creator);
					coterie1.setMemberCount(memberCount);
					coterie1.setMemberCardList(members);
					//Log.i("coterie1", coterie1.toString());
					
					Message message=new Message();
					message.what=GET_COTERIE_SUCCESS;
					message.obj=coterie1;
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
	public void clickBack(View view)
	{
		Intent intent=new Intent();
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		setResult(COTERIE_MEMBER_SUCCESS,intent);
		finish();
	}
	/** 点击管理 */
	@OnClick(R.id.tv_manage)
	public void clickManage(View view)
	{
		String memberType = coterie.getMemberType();
		//圈主
		if (memberType.equals("3")) {
			creatorManagePopup=new CreatorManagePopup(instance, itemsOnClick);
			creatorManagePopup.showAtLocation(ll_coterie_member, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}
		//普通成员
		if (memberType.equals("1")) {
			ordinaryMemberManagePopup=new OrdinaryMemberManagePopup(instance, itemsOnClick);
			ordinaryMemberManagePopup.showAtLocation(ll_coterie_member, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}
	private OnClickListener itemsOnClick=new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			if (creatorManagePopup!=null) {
				creatorManagePopup.dismiss();
			}
			if (ordinaryMemberManagePopup!=null) {
				ordinaryMemberManagePopup.dismiss();
			}
			switch (v.getId())
			{
			case R.id.tv_invite:
				Intent intent1=new Intent(instance, CircleInviteFirendActivity.class);
				intent1.putExtra(Constant.CIRCLE, coterie.getCoterieId());
				startActivity(intent1);
				break;
			case R.id.tv_delete:
				Intent intent2=new Intent(instance, DeleteCoterieMemberActivity.class);
				intent2.putExtra("COTERIE", coterie);
				startActivityForResult(intent2, DELETE_COTERIE_MEMBER_SUCCESS);
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DELETE_COTERIE_MEMBER_SUCCESS:
			if (data==null) {
				break;
			}
			Bundle bundle=data.getExtras();
			int checkNum=bundle.getInt("CHECKNUM");
			loadCoterieData(coterieId,memberCount-1-checkNum);
			coterieMemberListAdapter.notifyDataSetChanged();
			break;
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
             //do something...
        	
        	Intent intent=new Intent();
    		Bundle bundle = new Bundle();
    		intent.putExtras(bundle);
    		setResult(COTERIE_MEMBER_SUCCESS,intent);
    		finish();
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }
}
