package com.szrjk.self.more;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity {

	@ViewInject(R.id.tv_version)
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		initLayout();
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
