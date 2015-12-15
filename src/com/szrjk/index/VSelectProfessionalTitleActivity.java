package com.szrjk.index;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.TProfessionalTitle;
import com.szrjk.util.DCollectionUtils;

/**
 * denggm on 2015/10/20.
 * DHome
 * 职称列表
 */
@ContentView(R.layout.activity_vselect_professionaltitle)
public class VSelectProfessionalTitleActivity extends BaseActivity {

	final static String TAG = "VSelectProfessionalTitleActivity";

	final static int RESULT_PROFESSIONALTITLE = 3;


	@ViewInject(R.id.dh_scroll_list)
	private ListView dh_scroll_list;

	private UIHandler mUiHandler;

	private String userType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		userType=getIntent().getStringExtra("USERTYPE");
		loadBigData();
	}

	private void loadBigData(){
		mUiHandler = new UIHandler(); //创建uihandler
		new LoadDataThread().start();  //创建线程并启动
	}   

	private class UIHandler  extends Handler {
		    //定义消息类型
		public static final int TPROFESSIONALTITLE = 4;

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case TPROFESSIONALTITLE:
				String[] titleNames=(String[]) msg.obj;
				//测试
				try {
					//把内容加到activity
					dh_scroll_list.setAdapter(new VSelectDepartmentAdapter(VSelectProfessionalTitleActivity.this, titleNames));

					dh_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							TextView iteminfo = (TextView) view.findViewById(R.id.itemTitle);
							String titleName = (String) iteminfo.getText();
							//跳转到新界面，把val传过去

							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putString("titleName", titleName);

							intent.putExtras(bundle);
							setResult(RESULT_PROFESSIONALTITLE, intent);
							VSelectProfessionalTitleActivity.this.finish();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}

		}
	}

	private class LoadDataThread extends Thread{
		@Override
		public void  run(){

			try {
				// dept 的 下拉
				List<TProfessionalTitle> tProfessions = (new TProfessionalTitle()).getListFromType(VSelectProfessionalTitleActivity.this, userType);
				if(tProfessions!=null){
					String[] titleNameArr = DCollectionUtils.converFromList2(tProfessions,
							"titleName");
				Message message= new Message();
				message.what = UIHandler.TPROFESSIONALTITLE;
				message.obj=titleNameArr;
				mUiHandler.sendMessage(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
	}
	
}