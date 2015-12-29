package com.szrjk.self.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BackgroundSettingActivity;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.DoctorActivity;
import com.szrjk.dhome.IndexGalleryActivity;
import com.szrjk.dhome.OtherActivity;
import com.szrjk.dhome.R;
import com.szrjk.dhome.StudentActivity;
import com.szrjk.entity.AuthencitionEntity;
import com.szrjk.entity.CeriItemEntity;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PicItemEntity;
import com.szrjk.entity.PopupItem;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.message.ChatSettingsActivity;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.widget.ListPopup;

@ContentView(R.layout.activity_authentications)
public class MainAuthenticationActivity extends BaseActivity implements OnClickListener{

	private MainAuthenticationActivity instance;
	@ViewInject(R.id.lly_cancel)//返回 
	private LinearLayout lly_cancle;
	@ViewInject(R.id.lv_auth)
	private ListView lv_auth;
	@ViewInject(R.id.bt_new)
	private Button bt_new;
	private ArrayList<PicItemEntity> picls;
	private ArrayList<CeriItemEntity> cerls;
	private String[] imgageArr ;
	@ViewInject(R.id.textView1)
	private TextView textView1; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		getUserAuthenticationInfo();
		bt_new.setOnClickListener(this);
		lly_cancle.setOnClickListener(this);
	}

	private void getUserAuthenticationInfo(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "queryCertInfo");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		paramMap.put("BusiParams", busiParams);
		httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			@Override
			public void start() {
			}
			@Override
			public void loading(long total, long current, boolean isUploading) {
			}
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				Log.i("HttpException", jobj.toString());
			}
			@Override
			public void success(JSONObject jsonObject) {
				ErrorInfo errorObj = JSON.parseObject(jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode())) {
					JSONObject returnObj = jsonObject.getJSONObject("ReturnInfo");
					//					showToast(instance, "!"+returnObj, Toast.LENGTH_SHORT);
//					Log.i("getUserAuthenticationInfo", ""+returnObj);
					try {
						AuthencitionEntity lau = JSON.parseObject(returnObj.toString(), AuthencitionEntity.class);
						
						List<CeriItemEntity> cerData = lau.getCertListOut();
						
						Log.i("cerData",cerData.toString());
						if (cerData.size() == 0) {
							//0；一个证件都没有，提示目前无证件
							textView1.setVisibility(View.VISIBLE);
						}else{
							updateUI(cerData);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		});

	}

	// 更新UI
	//	private void updateUI(AuthencitionEntity lau) {
	private void updateUI(List<CeriItemEntity> cerData) {
		try {
			//			picls = (ArrayList<PicItemEntity>) lau.getPicListOut();
			//			cerls = (ArrayList<CeriItemEntity>) lau.getCertListOut();
//			if (picls.size() ==  0) {
//				textView1.setVisibility(View.VISIBLE);
//				//				textView1.setGravity(Gravity.CENTER);
//			}
			imgageArr = new String[cerData.size()];
			//取得图片URL数组，预览用的
			for (int i = 0; i < cerData.size(); i++) {
//				imgageArr[i] = ((PicItemEntity) picls.get(i)).getPicUrl();
				imgageArr[i] = cerData.get(i).getCertPicUrl();
			}
			lv_auth.setAdapter(new MyCerAdapter(cerData));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyCerAdapter extends BaseAdapter{

		List<CeriItemEntity> cerData;
//		public MyCerAdapter(ArrayList<PicItemEntity> picls,ArrayList<CeriItemEntity> cerls) {
		public MyCerAdapter(List<CeriItemEntity> cerData) {
//			this.picls = picls;
//			this.cerls = cerls;
			this.cerData = cerData;
		}
		@Override
		public int getCount() {
			return cerData.size();
		}

		@Override
		public Object getItem(int index) {
			return cerData.get(index);
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(final int index, View arg1, ViewGroup arg2) {
			View v = View.inflate(instance, R.layout.authentications_item, null);
			TextView textView = (TextView) v.findViewById(R.id.tv_type);
			TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
			ImageView imageView = (ImageView) v.findViewById(R.id.iv_authemtications);
			Button bt_new = (Button) v.findViewById(R.id.bt_new);
//			textView.setText("证件"+String.valueOf(index+1));
			CeriItemEntity data = cerData.get(index);
			textView.setText(data.getCertName());
			tv_time.setText(data.getCreateDate());
			ImageLoaderUtil util = new ImageLoaderUtil(instance, data.getCertPicUrl(), imageView, R.drawable.pic_downloadfailed_bg, R.drawable.pic_downloadfailed_bg);
			util.showImage();
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(instance,IndexGalleryActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", index);
					intent.putExtras(bundle);
					intent.putExtra("imgs", imgageArr);
					startActivity(intent);
				}
			});
			bt_new.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(instance,BackgroundSettingActivity.class));
				}
			});
			
			return v;
		}
	}

	@Override
	public void onClick(View v) {
		//这里new一个用户类型判断？
		try {
			switch (v.getId()) {
			case R.id.bt_new:
				showPopNew(bt_new);
				break;
			case R.id.lly_cancel:
				finish();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@OnItemClick(R.id.lv_auth)
//	public void caseItemClick(AdapterView<?> adapterView, View view, int num,long position) {
//		Intent intent = new Intent(instance,IndexGalleryActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("position", (int)position);
//		intent.putExtras(bundle);
//		intent.putExtra("imgs", imgageArr);
//		startActivity(intent);
//	}

	/**显示sendWindow**/
	private void showPopNew(View v){
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("当前操作会覆盖原来证件");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.red));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
			}
		});
		PopupItem pi2 = new PopupItem();
		pi2.setItemname("确定");//设置名称
		pi2.setColor(this.getResources().getColor(R.color.search_bg));
		pi2.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				int type = Integer.parseInt(Constant.userInfo.getUserType());
				switch (type) {
				case 2:
				case 8:
				case 9://authem
					Intent intentd = new Intent(instance,DoctorActivity.class);
					intentd.putExtra("authem", true);
					startActivity(intentd);
					break;
				case 3:
					Intent intents = new Intent(instance,StudentActivity.class);
					intents.putExtra("authem", true);
					startActivity(intents);
					break;
				case 1:
				case 4:
				case 5:
				case 6:
				case 7:
					Intent intento = new Intent(instance,OtherActivity.class);
					intento.putExtra("authem", true);
					startActivity(intento);
					break;
				}
				sendWindow.dismiss();
			}
		});
		pilist.add(pi1);
		pilist.add(pi2);
		new ListPopup(this,pilist,v);
	}

	/**
	 * 这里的3个重写方法：由于打开了拍照，低ram的手机会回收发帖这个Activity。
	 * 当回收之后，会执行onCreate方法里面的检查草稿方法。导致把草稿恢复覆盖当前编辑的内容
	 * @param newConfig
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		Log.i("onConfigurationChanged", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("onRestoreInstanceState", "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("onSaveInstanceState", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
}