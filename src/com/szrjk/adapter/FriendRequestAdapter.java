package com.szrjk.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import u.aly.bt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.RequestList;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.self.CircleActivity;
import com.szrjk.self.FriendActivity;
import com.szrjk.util.DisplayTimeUtil;
import com.szrjk.util.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FriendRequestAdapter extends BaseAdapter {
	private Context mContext;
	private List<RequestList> list;
	private LayoutInflater mInflater;
//	private static HashMap<String, Boolean> isSelected;
	private HashMap<String, Integer> state;
	private static final int TYPE_AGREE = 0;
	private static final int TYPE_INGORE = 1;
	private static final int TYPE_AGREED = 2;
	private static final int TYPE_INGORED = 3;
	
	private static final int TYPE_MAX_COUNT = TYPE_INGORED + 1; 

	public FriendRequestAdapter(Context mContext, List<RequestList> list,
			HashMap<String,Integer> state) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mInflater = LayoutInflater.from(mContext);
		this.state = state;
//		isSelected = new HashMap<String, Boolean>();
//		initDate();
//		Log.i("TAG", isSelected.toString());
	}
//	private void initDate() {
//		for (int i = 0; i < list.size(); i++) {  
//			isSelected.put(list.get(i).getUserCard().getUserSeqId(), false);  
//		}  
//	}
//	public  HashMap<String, Boolean> getIsSelected() {  
//		return isSelected;  
//	}  

//	public  void setIsSelected(HashMap<String, Boolean> isSelected) {  
//		FriendRequestAdapter.isSelected = isSelected;  
//	}  

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position,View convertview, ViewGroup parent) {
		ViewHolder viewHolder = null;
		int type = getItemViewType(position);
		if (convertview ==null) {
			viewHolder = new ViewHolder();
			convertview = mInflater.inflate(R.layout.item_list_friendrequest, null);
			viewHolder.usercard = (com.szrjk.widget.UserCardLayout) convertview.findViewById(R.id.request_usercard);
			viewHolder.tv_date = (TextView) convertview.findViewById(R.id.tv_request_date);
			viewHolder.btn_agree = (Button) convertview.findViewById(R.id.bt_request);
			viewHolder.btn_ignore = (Button) convertview.findViewById(R.id.bt_ignore);
			convertview.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertview.getTag();
		}
		viewHolder.usercard.setContext(mContext);
		UserCard userCard = list.get(position).getUserCard();
		viewHolder.usercard.setUser(userCard);
		viewHolder.usercard.changeline(userCard);
		if (userCard.getCompanyName().length()+userCard.getDeptName().length()>18) {
			RelativeLayout.LayoutParams layoutParams = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 
							RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.BELOW, R.id.request_usercard);
			layoutParams.setMargins(118, 0, 0, 0);
			viewHolder.tv_date.setLayoutParams(layoutParams);
		}
		switch (type) {
		case TYPE_AGREE:
			viewHolder.btn_agree.setOnClickListener(new requestBtnListener(viewHolder,position));
			viewHolder.btn_agree.setOnLongClickListener(new requestBtnLongListener(viewHolder, position));
			break;
		case TYPE_INGORE:
			viewHolder.btn_agree.setVisibility(View.GONE);
			viewHolder.btn_ignore.setVisibility(View.VISIBLE);
			viewHolder.btn_ignore.setOnClickListener(new requestBtnListener(viewHolder,position));
			break;
		case TYPE_AGREED:
			viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
			viewHolder.btn_agree.setText("已同意");
			viewHolder.btn_agree.setClickable(false);
			viewHolder.btn_ignore.setClickable(false);
			break;
		case TYPE_INGORED:
			viewHolder.btn_agree.setVisibility(View.GONE);
			viewHolder.btn_ignore.setVisibility(View.VISIBLE);
			viewHolder.btn_ignore.setBackgroundResource(R.drawable.icon_request2);
			viewHolder.btn_ignore.setText("已忽略");
			viewHolder.btn_agree.setClickable(false);
			viewHolder.btn_ignore.setClickable(false);
			break;
		}
		try {
			viewHolder.tv_date.setText(DisplayTimeUtil.displayTimeString(list.get(position).getRequestDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertview;
	}
	private class ViewHolder{
		private com.szrjk.widget.UserCardLayout usercard;
		private TextView tv_date;
		private Button btn_agree;
		private Button btn_ignore;

	}
	class requestBtnLongListener implements OnLongClickListener{
		private int position;
		private ViewHolder viewHolder;

		requestBtnLongListener(ViewHolder viewHolder,int position) {
			super();
			this.position = position;
			this.viewHolder = viewHolder;
		}
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.bt_request:
				state.put(list.get(position).getUserCard().getUserSeqId(), 1);
				notifyDataSetChanged();
//				viewHolder.btn_agree.setVisibility(View.GONE);
//				viewHolder.btn_ignore.setVisibility(View.VISIBLE);
				break;
			}
			return true;
		}

	}
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_MAX_COUNT;
	}
	@Override
	public int getItemViewType(int position) {
		int type = 0;
		switch (state.get(list.get(position).getUserCard().getUserSeqId())) {
		case 0:type = TYPE_AGREE;break;
		case 1:type = TYPE_INGORE;break;
		case 2:type = TYPE_AGREED;break;
		case 3:type = TYPE_INGORED;break;
		}
		return type;
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
					sendAgree();
//					viewHolder.btn_agree.setBackgroundResource(R.drawable.icon_request2);
//					viewHolder.btn_agree.setText("已同意");
//					isSelected.put(list.get(position).getUserCard().getUserSeqId(), true);
//					checkRequset();
//					isAgree =1;
//				}
				break;
			case R.id.bt_ignore:
//				if (isIgnore==0) {
					sendIgnore();
//					viewHolder.btn_ignore.setBackgroundResource(R.drawable.icon_request2);
//					viewHolder.btn_ignore.setText("已忽略");
//					isSelected.put(list.get(position).getUserCard().getUserSeqId(), true);
//					checkRequset();
//					isIgnore =1;
//				}
				break;
			}

		}
//		private void checkRequset() {
//			int num = 0;
//			for (int i = 0; i < list.size(); i++) {
//				if (isSelected.get(list.get(i).getUserCard().getUserSeqId())==true) {
//					num+=1;
//				}
//			}
//			if (num==list.size()) {
//				FriendActivity.changeremind(2);
//			}
//		}

		private void sendIgnore() {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "handleUserFriendRequest");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
			busiParams.put("srcUserSeqId",list.get(position).getUserCard().getUserSeqId());
			busiParams.put("operateType", String.valueOf(3));
			paramMap.put("BusiParams", busiParams);
			Log.i("TAG", paramMap.toString());
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				public void success(JSONObject jsonObject) {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						Toast.makeText(mContext, "已忽略", Toast.LENGTH_SHORT).show();
						state.put(list.get(position).getUserCard().getUserSeqId(), 3);
						notifyDataSetChanged();
					}
				}

				public void start() {

				}
				public void loading(long total, long current, boolean isUploading) {

				}
				public void failure(HttpException exception, JSONObject jobj) {
					ToastUtils.showMessage(mContext, "请求失败");
				}
			});

		}

		private void sendAgree() {
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("ServiceName", "handleUserFriendRequest");
			Map<String, Object> busiParams = new HashMap<String, Object>();
			busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
			busiParams.put("srcUserSeqId",list.get(position).getUserCard().getUserSeqId());
			busiParams.put("operateType", String.valueOf(1));
			paramMap.put("BusiParams", busiParams);
			Log.i("TAG", paramMap.toString());
			DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
				public void success(JSONObject jsonObject) {
					ErrorInfo errorObj = JSON.parseObject(
							jsonObject.getString("ErrorInfo"), ErrorInfo.class);
					if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
					{
						Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
						state.put(list.get(position).getUserCard().getUserSeqId(), 2);
						notifyDataSetChanged();
					}else{
						Toast.makeText(mContext, "已经是好友了", Toast.LENGTH_SHORT).show();
					}
				}

				public void start() {

				}
				public void loading(long total, long current, boolean isUploading) {

				}
				public void failure(HttpException exception, JSONObject jobj) {
					ToastUtils.showMessage(mContext, "请求失败");
				}
			});
		}


	}

}
