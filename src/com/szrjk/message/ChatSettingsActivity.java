package com.szrjk.message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.entity.UserCard;
import com.szrjk.util.ImageLoaderUtil;
import com.szrjk.util.PictureLoader;
import com.szrjk.widget.ListPopup;
/**
 * @author ldr
 * 2015-12-25 13:44:52
 * 聊天消息设置
 */
@ContentView(R.layout.activity_chat_settings)
public class ChatSettingsActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.rl_chat)
	private LinearLayout rl_chat;
	
	@ViewInject(R.id.bt_chatclean)
	private Button bt_chatclean;
	
	@ViewInject(R.id.iv_smallphoto)
	private ImageView iv_smallphoto;
	
	@ViewInject(R.id.iv_yellow_icon)
	private ImageView iv_yellow_icon;
	
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	
	@ViewInject(R.id.tv_jobtitle)
	private TextView tv_jobtitle;
	
	@ViewInject(R.id.tv_hospital)
	private TextView tv_hospital;
	
	@ViewInject(R.id.tv_department)
	private TextView tv_department;

	private ChatSettingsActivity instance;
	
	@ViewInject(R.id.rl_setbackground)
	private RelativeLayout rl_setbackground;
	
	private File imgFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		instance = this;
		Intent data = this.getIntent();
		Bundle u = data.getExtras();
		UserCard obj = (UserCard) u.getSerializable(Constant.USER_INFO);
//		Log.i("", obj.toString());
		setListener();
		initLayout(obj);
	}

	private void setListener() {
		rl_setbackground.setOnClickListener(this);
		bt_chatclean.setOnClickListener(this);
	}

	private void initLayout(UserCard obj) {
		ImageLoaderUtil util = new ImageLoaderUtil(this, obj.getUserFaceUrl(), iv_smallphoto, R.drawable.pic_downloadfailed_230, R.drawable.pic_downloadfailed_230);
		util.showImage();
		if ("11".equals(obj.getUserLevel())) {
			iv_yellow_icon.setVisibility(View.VISIBLE);
		}
		tv_name.setText(obj.getUserName());
		tv_jobtitle.setText(obj.getProfessionalTitle());
		tv_hospital.setText(obj.getCompanyName());
		tv_department.setText(obj.getDeptName());
	}

	/**显示sendWindow**/
	private void showPop(){
		//		sendWindow = new PostSendPopup(instance, sendPostClick);

		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("拍照");//设置名称
		pi1.setColor(this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow popupWindow) {
				imgFile = PictureLoader.getCamera(instance);
				popupWindow.dismiss();
			}
		});
		PopupItem pi2 = new PopupItem();
		pi2.setItemname("从手机相册选择");//设置名称
		pi2.setColor(this.getResources().getColor(R.color.search_bg));
		pi2.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow popupWindow) {
				PictureLoader.getAlbum(instance, 1);
				popupWindow.dismiss();
			}
		});
		
		pilist.add(pi1);
		pilist.add(pi2);
		new ListPopup(instance,pilist,rl_chat);
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.rl_setbackground:
				showPop();
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != RESULT_OK) {
				Log.i("", "么有返回数据");
				return;
			}
			
			switch (requestCode) {
			case PictureLoader.CAMERA:
				//拍照完之后，处理图片
				showToast(instance, imgFile.getAbsolutePath(), 0);
				
				break;
			case PictureLoader.Album:
				String[] arr = PictureLoader.getIntentData(data);
				showToast(instance, arr[0], 0);
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
