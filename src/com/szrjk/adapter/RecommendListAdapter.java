package com.szrjk.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.ToastUtils;

public class RecommendListAdapter extends BaseAdapter{
	private Context context;
	private List<UserCard> recommendUserList;
	private ImageLoaderUtil imageloader;
	private int index_position;
	private IndexListViewAdapter indexListViewAdapter;
	private boolean isTourist;
	
	public RecommendListAdapter(Context context,List<UserCard> recommendUserList) {
		this.context = context;
		this.recommendUserList = recommendUserList;
		isTourist = BusiUtils.isguest(context);
	}


	@Override
	public int getCount() {
		return recommendUserList.size();
	}

	@Override
	public Object getItem(int position) {
		return recommendUserList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.view_recommend_doctorinfo, null);
		final UserCard recommendUser = recommendUserList.get(position);
		ImageView iv_smallPhoto = (ImageView) convertView.findViewById(R.id.iv_smallphoto);
		TextView tv_userName = (TextView)convertView.findViewById(R.id.tv_name);
		TextView tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
		TextView tv_department = (TextView)convertView.findViewById(R.id.tv_department);
		TextView tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
		ImageView iv_head_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
		final Button bt_attention = (Button)convertView.findViewById(R.id.bt_add_attention);
		RelativeLayout rl_userInfo = (RelativeLayout)convertView.findViewById(R.id.rl_userinfo);
		Log.e("Recommend", iv_smallPhoto+"");
		try {
			imageloader = new ImageLoaderUtil(context, recommendUser.getUserFaceUrl(),iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
			imageloader.showImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ImageLoader", e.toString());
		}
		tv_userName.setText(recommendUser.getUserName());
		tv_jobTitle.setText(recommendUser.getProfessionalTitle());
		tv_hospital.setText(recommendUser.getCompanyName());
		tv_department.setText(recommendUser.getDeptName());
		if(recommendUser.getUserLevel().equals("11")){
			iv_head_icon.setVisibility(View.VISIBLE);
		}else{
			iv_head_icon.setVisibility(View.GONE);
		}
		boolean isFocus = recommendUser.isFocus();
		if(isFocus){
			Drawable drawableTop = context.getResources().getDrawable(R.drawable.icon_adfocus_gray);
			bt_attention.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
			bt_attention.setText("已关注");
			bt_attention.setTextColor(context.getResources().getColor(R.color.font_cell));
		}else{
			Drawable drawableTop = context.getResources().getDrawable(R.drawable.icon_adfocus);
			bt_attention.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
			bt_attention.setText("未关注");
			bt_attention.setTextColor(context.getResources().getColor(R.color.btn_text_red));
		}
		rl_userInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isTourist){
					DialogUtil.showGuestDialog(context, null);
				}else{					
					Intent intent = new Intent(context, OtherPeopleActivity.class);
					Log.e("RecommendList", "推荐用户id:"+recommendUser.getUserSeqId());
					intent.putExtra(Constant.USER_SEQ_ID, recommendUser.getUserSeqId());
					context.startActivity(intent);
				}
			}
		});
//		bt_attention.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addAttention(recommendUser,bt_attention);
//			}
//		});
		return convertView;
	}

	protected void addAttention(final UserCard recommendUser, final Button bt_attention) {
		// TODO Auto-generated method stub

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "dealUserFocus");
		Map<String,Object> busiParam = new HashMap<String, Object>();
		busiParam.put("userSeqId", Constant.userInfo.getUserSeqId());//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
		busiParam.put("objUserSeqId", recommendUser.getUserSeqId());//这个是目标用户ID
		busiParam.put("operateType", "A");//是不是加关注
		paramMap.put("BusiParams", busiParam);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				ToastUtils.showMessage(context, "关注失败，请检查网络");
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
					recommendUser.setFocus(true);
					notifyDataSetChanged();
				}
			}
		});

	
	}


}
