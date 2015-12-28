package com.szrjk.adapter;

import com.szrjk.entity.RecommendContent;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.dhome.R;
import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.entity.RecommendInfo;
import com.szrjk.self.CircleHomepageActivity;
import com.szrjk.util.BusiUtils;
import com.szrjk.util.DialogUtil;
import com.szrjk.util.ImageLoaderUtil;

public class RecommendListAdapter2 extends BaseAdapter{
	
	private Context context;
	private List<RecommendInfo> recommendInfoList;
	private ImageLoaderUtil imageloader;
	private int index_position;
	private IndexListViewAdapter indexListViewAdapter;
	private boolean isTourist;
	private ViewHolder2 circleHolder;
	private ViewHolder userHolder;
	private ViewHolder3 unknowHolder;
	private static final String CIRCLE_TYPE_PERSONAL = "个人";
	private static final String CIRCLE_TYPE_COMPANY = "组织/机构";
	
	public RecommendListAdapter2(Context context,List<RecommendInfo> recommendInfoList) {
		this.context = context;
		if(recommendInfoList != null){		
			this.recommendInfoList = recommendInfoList;
		}else{
			this.recommendInfoList = new ArrayList<RecommendInfo>();
		}
		isTourist = BusiUtils.isguest(context);
	}


	@Override
	public int getCount() {
		return recommendInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return recommendInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		RecommendInfo recommendInfo = recommendInfoList.get(position);
		if(recommendInfo.getRecommendType().equals("1")){
			return 1;
		}else if(recommendInfo.getRecommendType().equals("2")){
			return 0;
		}else{
			return 2;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecommendInfo recommentInfo = recommendInfoList.get(position);
		if(recommentInfo == null){
			return convertView;
		}
		int type = getItemViewType(position);
		Log.e("RecommendInfo", "类型："+type);
		if(convertView == null){
			switch (type) {
			case 0:
				convertView = View.inflate(context, R.layout.view_recommend_circle, null);
				circleHolder = new ViewHolder2();
				circleHolder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
				circleHolder.tv_circleName = (TextView)convertView.findViewById(R.id.tv_circleName);
				circleHolder.tv_circleType = (TextView)convertView.findViewById(R.id.tv_circle_type);
				circleHolder.tv_peopleNum = (TextView)convertView.findViewById(R.id.tv_people_num);
				circleHolder.rl_recommendCircle = (RelativeLayout)convertView.findViewById(R.id.rl_recommend_circle);
				convertView.setTag(circleHolder);
				break;
			case 1:
				convertView = View.inflate(context, R.layout.view_recommend_doctorinfo, null);
				userHolder = new ViewHolder();
				userHolder.iv_smallPhoto = (ImageView)convertView.findViewById(R.id.iv_smallphoto);
				userHolder.iv_head_icon = (ImageView)convertView.findViewById(R.id.iv_yellow_icon);
				userHolder.tv_userName = (TextView)convertView.findViewById(R.id.tv_name);
				userHolder.tv_jobTitle = (TextView)convertView.findViewById(R.id.tv_jobtitle);
				userHolder.tv_hospital = (TextView)convertView.findViewById(R.id.tv_hospital);
				userHolder.tv_department = (TextView)convertView.findViewById(R.id.tv_department);
				userHolder.rl_userInfo = (RelativeLayout)convertView.findViewById(R.id.rl_userinfo);
				convertView.setTag(userHolder);
				break;
			case 2:
				convertView = View.inflate(context, R.layout.item_unknow_type, null);
				unknowHolder = new ViewHolder3();
				Log.e("IndexViewAdapter", "其他帖子加载布局");
				convertView.setTag(unknowHolder);
				break;
			}
		}else{
			switch (type) {
			case 0:
				circleHolder = (ViewHolder2) convertView.getTag();
				break;
			case 1:
				userHolder = (ViewHolder) convertView.getTag();
				break;
			case 2:
				unknowHolder = (ViewHolder3) convertView.getTag();
				break;
			}
		}
			final RecommendContent recommendContent = recommentInfo.getRecommendContent();
			switch (type) {
			case 0:
				if(recommendContent == null){
					return convertView;
				}else{
					imageloader = new ImageLoaderUtil(context, recommendContent.getCoterieFaceUrl(),circleHolder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
					imageloader.showImage();
					if(recommendContent.getCoterieName()!=null && !recommendContent.getCoterieName().isEmpty()){
						circleHolder.tv_circleName.setText(recommendContent.getCoterieName());
					}else{
						circleHolder.tv_circleName.setText("");
					}
					if(recommendContent.getMemberNum()!=null && !recommendContent.getMemberNum().isEmpty()){
						circleHolder.tv_peopleNum.setText(recommendContent.getMemberNum());
					}else{
						circleHolder.tv_peopleNum.setText("0");
					}
					if(recommendContent.getCoterieType()!=null && !recommendContent.getCoterieType().isEmpty()){
						if(recommendContent.getCoterieType().equals("1")){
							circleHolder.tv_circleType.setText(CIRCLE_TYPE_PERSONAL);
						}else{
							circleHolder.tv_circleType.setText(CIRCLE_TYPE_COMPANY);
						}
					}else{
						circleHolder.tv_circleType.setText("未知");
					}
					circleHolder.rl_recommendCircle.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(recommendContent.getCoterieId()!=null && !recommendContent.getCoterieId().isEmpty()){
								skipToCircleHomePage(recommendContent.getCoterieId());
							}
						}
					});
				}
				break;
			case 1:
				if(recommendContent == null){
					return convertView;
				}else{
					imageloader = new ImageLoaderUtil(context, recommendContent.getUserFaceUrl(),userHolder.iv_smallPhoto, R.drawable.icon_headfailed, R.drawable.icon_headfailed);
					imageloader.showImage();
					if(recommendContent.getUserLevel()!=null && !recommendContent.getUserLevel().isEmpty()){
						if(recommendContent.getUserLevel().equals("11")){
							userHolder.iv_head_icon.setVisibility(View.VISIBLE);
						}else{
							userHolder.iv_head_icon.setVisibility(View.GONE);
						}
					}else{
						userHolder.iv_head_icon.setVisibility(View.GONE);
					}
					if(recommendContent.getUserName()!=null && !recommendContent.getUserName().isEmpty()){
						userHolder.tv_userName.setText(recommendContent.getUserName());
					}else{
						userHolder.tv_userName.setText("");
					}
					if(recommendContent.getProfessionalTitle()!=null && !recommendContent.getProfessionalTitle().isEmpty()){
						userHolder.tv_jobTitle.setText(recommendContent.getProfessionalTitle());
					}else{
						userHolder.tv_jobTitle.setText("");
					}
					if(recommendContent.getCompanyName()!=null && !recommendContent.getCompanyName().isEmpty()){
						userHolder.tv_hospital.setText(recommendContent.getCompanyName());
					}else{
						userHolder.tv_hospital.setText("");
					}
					if(recommendContent.getDeptName()!=null && !recommendContent.getDeptName().isEmpty()){
						userHolder.tv_department.setText(recommendContent.getDeptName());
					}else{
						userHolder.tv_department.setText("");
					}
					userHolder.rl_userInfo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(isTourist){
								DialogUtil.showGuestDialog(context, null);
							}else{
								if(recommendContent.getUserSeqId()!=null && !recommendContent.getUserSeqId().isEmpty()){			
									Intent intent = new Intent(context, OtherPeopleActivity.class);
									intent.putExtra(Constant.USER_SEQ_ID, recommendContent.getUserSeqId());
									context.startActivity(intent);
								}
							}
						}
					});
				}
				break;
			case 2:
				break;		
			}
		return convertView;
	}
	
	protected void skipToCircleHomePage(String coterieId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, CircleHomepageActivity.class);
		intent.putExtra("intent_param_circle_id", coterieId);
		context.startActivity(intent);
	}



	class ViewHolder{
		ImageView iv_smallPhoto,iv_head_icon;
		TextView tv_userName,tv_jobTitle,tv_hospital,tv_department;
		RelativeLayout rl_userInfo;
	}
	class ViewHolder2{
		ImageView iv_smallPhoto;
		TextView tv_circleName,tv_peopleNum,tv_circleType;
		RelativeLayout rl_recommendCircle;
	}
	class ViewHolder3{
		
	}

//	protected void addAttention(final UserCard recommendUser, final Button bt_attention) {
//		// TODO Auto-generated method stub
//
//		HashMap<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("ServiceName", "dealUserFocus");
//		Map<String,Object> busiParam = new HashMap<String, Object>();
//		busiParam.put("userSeqId", Constant.userInfo.getUserSeqId());//当前用户ID  可以用userInfo = Constant.userInfo;userId=userInfo.getUserSeqId();
//		busiParam.put("objUserSeqId", recommendUser.getUserSeqId());//这个是目标用户ID
//		busiParam.put("operateType", "A");//是不是加关注
//		paramMap.put("BusiParams", busiParam);
//		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
//			@Override
//			public void start() {
//			}
//			@Override
//			public void loading(long total, long current, boolean isUploading) {
//			}
//			@Override
//			public void failure(HttpException exception, JSONObject jobj) {
//				ToastUtils.showMessage(context, "关注失败，请检查网络");
//			}
//			@Override
//			public void success(JSONObject jsonObject) {
//				ErrorInfo errorObj  =JSON.parseObject(jsonObject.getString("ErrorInfo"),ErrorInfo.class);
//				if(Constant.REQUESTCODE.equals(errorObj.getReturnCode())){
//					recommendUser.setFocus(true);
//					notifyDataSetChanged();
//				}
//			}
//		});

	

}
