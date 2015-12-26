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
	//此处String 是由UserID与CircleID拼接而成
	private HashMap<String, Integer> state;
	//I=邀请   R=请求
	private static final int TYPE_AGREE = 0;
	private static final int TYPE_INGORE = 1;
	private static final int TYPE_AGREED = 2;
	private static final int TYPE_INGORED = 3;
	private static final int TYPE_MAX_COUNT = TYPE_INGORED + 1; 


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
	public String getKey(int position){
		StringBuffer sb = new StringBuffer(list.get(position).getObjUserSeqId());
		sb.append(list.get(position).getCoterieId());
		String key = sb.toString();
		return key;

	}
	@Override
	public int getItemViewType(int position) {
		int type = 0;
		String key = getKey(position);
		switch (state.get(key)) {
		case 0:type = TYPE_AGREE;break;
		case 1:type = TYPE_INGORE;break;
		case 2:type = TYPE_AGREED;break;
		case 3:type = TYPE_INGORED;break;
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
			viewHolder.tv_date.setText(DisplayTimeUtil.displayTimeString(list.get(position).getCreateDate()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		else{
//			RelativeLayout.LayoutParams layoutParams = 
//					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//			layoutParams.addRule(RelativeLayout.BELOW, R.id.request_usercard);
//			layoutParams.setMargins(118, -30, 0, 0);
//			viewHolder.tv_date.setLayoutParams(layoutParams);
//		}
		if (list.get(position).getInvitationType().equals("I")) {
			viewHolder.tv_invitationType.setText("邀请您加入");
		}else if (list.get(position).getInvitationType().equals("R")) {
			viewHolder.tv_invitationType.setText("请求加入");
		}
		viewHolder.tv_circle_name.setText(list.get(position).getCoterieName());
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
		//		viewHolder.btn_agree.setOnClickListener(new requestBtnListener(viewHolder,position));
		//		viewHolder.btn_ignore.setOnClickListener(new requestBtnListener(viewHolder,position));
		//		viewHolder.btn_agree.setOnLongClickListener(new requestBtnLongListener(viewHolder, position));
		switch (type) {
		case TYPE_AGREE:
			viewHolder.btn_agree.setOnClickListener(new requestBtnListener(viewHolder,position));
			viewHolder.btn_agree.setOnLongClickListener(new requestBtnLongListener(viewHolder, position));
			break;
		case TYPE_AGREED:
			viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
			viewHolder.btn_agree.setText("已同意");
			viewHolder.btn_agree.setClickable(false);
			break;
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
				//				if (isAgree==0) {
				//					viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
				//					viewHolder.btn_agree.setText("已同意");
				sendAgree();
				//					isSelected.put(list.get(position).getCoterieId(), true);
				//					checkRequset();
				//					isAgree =1;
				//				}
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
			busiParams.put("invitationId", list.get(position).getInvitationId());
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
						state.put(getKey(position), 2);
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
		private void sendIgnore() {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "dealCoterieInvitation");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId",list.get(position).getUserSeqId());//目标用户ID是上一层传过来
			busiParams.put("invitationId", list.get(position).getInvitationId());
			busiParams.put("coterieId", list.get(position).getCoterieId());
			busiParams.put("isAgree", "N");
			paramMap.put("BusiParams", busiParams);
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				@Override
				public void success(JSONObject jsonObject) {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						Toast.makeText(mContext, "已忽略", Toast.LENGTH_SHORT).show();
						state.put(getKey(position), 3);
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
