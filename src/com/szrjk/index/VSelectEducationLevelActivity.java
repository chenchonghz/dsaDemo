package com.szrjk.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;

@ContentView(R.layout.activity_vselect_education_level)
public class VSelectEducationLevelActivity extends BaseActivity{

	final static String TAG = "VSelectEducationLevelActivity";

	final static int RESULT_VSEDUCATIONLEVEL = 0;


	@ViewInject(R.id.dh_scroll_list)
	private ListView dh_scroll_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		final String[] levels=new String[]{"无","小学","初中","中专","高中","大专","本科","硕士研究生","博士研究生","博士后"};
		VSelectEducationLevelAdapter adapter=new VSelectEducationLevelAdapter(this, levels);
		dh_scroll_list.setAdapter(adapter);
		dh_scroll_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				TextView iteminfo = (TextView) view.findViewById(R.id.itemTitle);
				String level = (String) iteminfo.getText();
				//跳转到新界面，把val传过去

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("level", level);
				bundle.putInt("index", i);
				intent.putExtras(bundle);
				setResult(RESULT_VSEDUCATIONLEVEL, intent);
				VSelectEducationLevelActivity.this.finish();
			}
		});
	}
}
