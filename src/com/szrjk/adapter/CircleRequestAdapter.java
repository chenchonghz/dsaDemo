package com.szrjk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.CircleInfo;
import com.szrjk.entity.CircleRequest;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.widget.UserCardLayout;

public class CircleRequestAdapter extends BaseAdapter {
	private Context mContext;
	private List<CircleRequest> list;
	private LayoutInflater mInflater;
	//	private static HashMap<String, Boolean> isSelected;
	//此处String 是由objUserID与CircleID拼接而成
	private HashMap<String, Integer> state;
	//state
	private static final int STATE_AGREE = 0;
	private static final int STATE_AGREED = 1;
	//type
	//* 11: 邀请加入圈子 ； * 12: 请求加入圈子 ；* 14: 加入圈子成功；* 21: 成员退出；* 22: 群主踢出成员； * 23: 解散圈子
	private static final int TYPE_INVITE = 11;
	private static final int TYPE_REQUEST = 12;
	private static final int TYPE_JOIN_SUCCESS = 14;
	private static final int TYPE_MENBER_QUIT =21;
	private static final int TYPE_KICK_OUT =22;
	private static final int TYPE_DISSOLVE =23;
	private static final int TYPE_MAX_COUNT = 6; 


	public CircleRequestAdapter(Context mContext, List<CircleRequest> list,
			HashMap<String, Integer> RequestState) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mInflater=LayoutInflater.from(mContext);
		this.state = RequestState;
		//		isSelected = new HashMap<String, Boolean>();
		//		initDate();

	}
	//	private void initDate() {
	//		for (int i = 0; i < list.size(); i++) {  
	////			isSelected.put(list.get(i).getCoterieId(), false);  
	//		}  
	//	}
	//	public  HashMap<String, Boolean> getIsSelected() {  
	//		return isSelected;  
	//	}  

	//	public  void setIsSelected(HashMap<String, Boolean> isSelected) {  
	//		CircleRequestAdapter.isSelected = isSelected;  
	//	}  
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_MAX_COUNT;
	}
	//获得hash表对应的
	public String getKey(int position){
		StringBuffer sb = new StringBuffer(list.get(position).getObjUserSeqId());
		sb.append(list.get(position).getCoterieId());
		String key = sb.toString();
		return key;

	}
	@Override
	public int getItemViewType(int position) {
		int type = 0;
		String Rtype = list.get(position).getNotifyType();
		switch (Integer.valueOf(Rtype)) {
		case 11:type = TYPE_INVITE;break;//邀请
		case 12:type = TYPE_REQUEST;break;//请求
		case 14:type = TYPE_JOIN_SUCCESS;break;//加入成功
		case 21:type = TYPE_MENBER_QUIT;break;//用户退出
		case 22:type = TYPE_KICK_OUT;break;//被踢
		case 23:type = TYPE_DISSOLVE;break;//解散
		}
		return type;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		ViewHolder viewHolder = null;
		int type = getItemViewType(position);
		if (convertview==null) {
			viewHolder = new ViewHolder();
			convertview = mInflater.inflate(R.layout.item_list_circlerequest, null);
			viewHolder.usercard = (UserCardLayout) convertview.findViewById(R.id.request_usercard);
			viewHolder.tv_date = (TextView) convertview.findViewById(R.id.tv_request_date);
			viewHolder.btn_agree = (Button) convertview.findViewById(R.id.bt_request);
			viewHolder.tv_invitationType = (TextView) convertview.findViewById(R.id.tv_invitationType);
			viewHolder.tv_circle_name = (TextView) convertview.findViewById(R.id.tv_circle_name);
			viewHolder.rly_fromCircle = (RelativeLayout) convertview.findViewById(R.id.rly_fromcircle);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
		viewHolder.usercard.setContext(mContext);
		UserCard userCard = list.get(position).getObjUserCard();
		viewHolder.usercard.setUser(userCard);
		viewHolder.usercard.changeline(userCard);
		try {
			viewHolder.tv_date.setText(DisplayTimeUtil.displayTimeString(list.get(position).getOpTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (type) {
		case TYPE_INVITE:viewHolder.tv_invitationType.setText("邀请您加入圈");break;//邀请
		case TYPE_REQUEST:viewHolder.tv_invitationType.setText("请求加入圈");break;//请求
		case TYPE_JOIN_SUCCESS:viewHolder.tv_invitationType.setText("同意你加入圈");break;//加入成功
		case TYPE_MENBER_QUIT:viewHolder.tv_invitationType.setText("已经退出圈");break;//用户退出
		case TYPE_KICK_OUT:viewHolder.tv_invitationType.setText("将您请出圈");break;//被踢
		case TYPE_DISSOLVE:viewHolder.tv_invitationType.setText("解散了圈");break;//解散
		}
		viewHolder.tv_circle_name.setText(list.get(position).getCoterieName());
		//圈子用户名片过长的换行处理
		if (userCard.getCompanyName().length()+userCard.getDeptName().length()>18) {
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.request_usercard);
			layoutParams.setMargins(118, -5, 0, 0);
			viewHolder.tv_date.setLayoutParams(layoutParams);
			RelativeLayout.LayoutParams layoutParams2 = 
					new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
							LayoutParams.WRAP_CONTENT);
			layoutParams2.addRule(RelativeLayout.BELOW, R.id.tv_request_date);
			layoutParams2.setMargins(118, 0, 0, 0);
			viewHolder.rly_fromCircle.setLayoutParams(layoutParams2);
		}

		viewHolder.rly_fromCircle.setOnClickListener(new requestBtnListener(viewHolder, position));
		//按钮设置
		switch (type) {
		case TYPE_INVITE:
			switch (state.get(getKey(position))) {
			case STATE_AGREE:
				viewHolder.btn_agree.setOnClickListener(new requestBtnListener(viewHolder,position));
				break;
			case STATE_AGREED:
				viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
				viewHolder.btn_agree.setText("已同意");
				viewHolder.btn_agree.setClickable(false);
				break;
			}
		case TYPE_REQUEST:
			switch (state.get(getKey(position))) {
			case STATE_AGREE:
				viewHolder.btn_agree.setOnClickListener(new requestBtnListener(viewHolder,position));
				break;
			case STATE_AGREED:
				viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
				viewHolder.btn_agree.setText("已同意");
				viewHolder.btn_agree.setClickable(false);
				break;
			}
		case TYPE_JOIN_SUCCESS:viewHolder.btn_agree.setVisibility(View.GONE);break;//加入成功
		case TYPE_MENBER_QUIT:viewHolder.btn_agree.setVisibility(View.GONE);break;//用户退出
		case TYPE_KICK_OUT:viewHolder.btn_agree.setVisibility(View.GONE);break;//被踢
		case TYPE_DISSOLVE:viewHolder.btn_agree.setVisibility(View.GONE);break;//解散

		}
		return convertview;
	}
	private class ViewHolder{
		private com.szrjk.widget.UserCardLayout usercard;
		private TextView tv_date;
		private Button btn_agree;
		private TextView tv_invitationType;
		private TextView tv_circle_name;
		private RelativeLayout rly_fromCircle;
	}
	class requestBtnLongListener implements OnLongClickListener{
		private int position;
		private ViewHolder viewHolder;

		requestBtnLongListener(ViewHolder viewHolder,int position) {
			super();
			this.position = position;
			this.viewHolder = viewHolder;
		}
		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.bt_request:
				state.put(getKey(position), 1);
				notifyDataSetChanged();
				//				viewHolder.btn_agree.setVisibility(View.GONE);
				//				viewHolder.btn_ignore.setVisibility(View.VISIBLE);
				break;
			}
			return true;
		}

	}
	class requestBtnListener implements OnClickListener{
		private int position;
		private ViewHolder viewHolder;

		requestBtnListener(ViewHolder viewHolder,int position) {
			super();
			this.position = position;
			this.viewHolder = viewHolder;
		}
		//		private int isAgree = 0;
		//		private int isIgnore = 0;

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.bt_request:
				sendAgree();
				break;
			case R.id.rly_fromcircle:
				getcircle(position);
				break;
			}


		}

		//		private void checkRequset() {
		//			int num = 0;
		//			for (int i = 0; i < list.size(); i++) {
		//				if (isSelected.get(list.get(i).getCoterieId())==true) {
		//					num+=1;
		//				}
		//			}
		//			if (num==list.size()) {
		//				CircleActivity.changeremind(2);
		//			}
		//		}

		private void sendAgree() {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "dealCoterieInvitation");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId",list.get(position).getUserSeqId());//目标用户ID是上一层传过来
			busiParams.put("pkId", list.get(position).getPkID());
			busiParams.put("coterieId", list.get(position).getCoterieId());
			busiParams.put("isAgree", "Y");
			paramMap.put("BusiParams", busiParams);
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				@Override
				public void success(JSONObject jsonObject) {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						Toast.makeText(mContext, "已同意", Toast.LENGTH_SHORT).show();
						state.put(getKey(position), 1);
						notifyDataSetChanged();
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
	}
	public void getcircle(int position) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "getCoterieInfoById");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("coterieId",list.get(position).getCoterieId());//目标用户ID是上一层传过来
		busiParams.put("userSeqId", list.get(position).getUserSeqId());
		busiParams.put("memberLimitCount", "0");
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					JSONObject returnObj = jsonObject
							.getJSONObject("ReturnInfo");
					JSONObject resObj = returnObj
							.getJSONObject("ListOut");
					CircleInfo circleInfo = JSON.parseObject(
							resObj.toJSONString(), CircleInfo.class);
					Intent intent = new Intent(mContext, CircleHomepageActivity.class);
					intent.putExtra("circle", circleInfo);
					mContext.startActivity(intent);
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

	//发送同意申请

}
