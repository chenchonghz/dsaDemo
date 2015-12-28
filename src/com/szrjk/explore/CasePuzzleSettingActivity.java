package com.szrjk.explore;

import android.os.Bundle;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
@ContentView(R.layout.activity_casepuzzle_setting)
public class CasePuzzleSettingActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}

	

}
