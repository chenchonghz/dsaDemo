package com.szrjk.self.more;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.config.Constant;
import com.szrjk.config.ConstantUser;
import com.szrjk.dhome.AboutYouActivity;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;
import com.szrjk.explore.CaseSharePostMessageActivity;
import com.szrjk.index.RepeatActivity;
import com.szrjk.widget.ListPopup;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

	@ViewInject(R.id.tv_version)
	private TextView tv_version;

	@ViewInject(R.id.ll_about_you)
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initLayout();

		tv_version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopup();
			}
		});


	}

	private  void showPopup(){
		List<PopupItem> pilist = new ArrayList<PopupItem>();
		PopupItem pi1 = new PopupItem();
		pi1.setItemname("病例分享消息版");//设置名称
		pi1.setColor(AboutActivity.this.getResources().getColor(R.color.search_bg));
		pi1.setiPopupItemCallback(new IPopupItemCallback() {  //设置click动作
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
				Intent intent = new Intent(AboutActivity.this, CaseSharePostMessageActivity.class);
				intent.putExtra("postType", "ALL");
				AboutActivity.this.startActivityForResult(intent, 1);
			}
		});
		pilist.add(pi1);
		new ListPopup(this,pilist,ll);
	}

	private void initLayout() {
		String version = getVersion();
		tv_version.setText(version);
	}

	/**
	 * 
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */

	public String getVersion() {

		try {

			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return "version" + version;

		} catch (Exception e) {

			e.printStackTrace();

			return "can not find version name";

		}

	}
}
