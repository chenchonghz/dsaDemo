package com.szrjk.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.dhome.AddressBookActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.UserCard;
import com.szrjk.util.ImageLoaderUtil;
/**
 * @author ldr
 * 2015-12-25 13:44:52
 * 聊天消息设置
 */
@ContentView(R.layout.activity_chat_settings)
public class ChatSettingsActivity extends Activity {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		
		Intent data = this.getIntent();
		Bundle u = data.getExtras();
		UserCard obj = (UserCard) u.getSerializable(Constant.USER_INFO);
//		Log.i("", obj.toString());
		bt_chatclean.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				startActivity(new Intent(ChatSettingsActivity.this,AddressBookActivity.class));
				
			}
		});
		
		initLayout(obj);
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


}
