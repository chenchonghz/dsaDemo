package com.szrjk.index;

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
import com.szrjk.entity.TCity;
import com.szrjk.util.ToastUtils;

/**
 * denggm on 2015/10/20.
 * DHome
 * 省份列表
 */
@ContentView(R.layout.activity_vselect)
public class VSelectActivity extends BaseActivity {

	final static String TAG = "VSelectActivity";

	final static int RESULT_VSCITY = 11;


	@ViewInject(R.id.dh_scroll_list)
	private ListView dh_scroll_list;

	private UIHandler mUiHandler;
	String[] provinceList = null;
	String[] provinceCodeList=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		loadBigData();
	}

	private void loadBigData(){
		mUiHandler = new UIHandler(); //创建uihandler
		new LoadDataThread().start();  //创建线程并启动
	}   

	private class UIHandler  extends Handler {
		static final int PROVINCE = 0;    //定义消息类型
		public static final int PROVINCECODE = 1;

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case PROVINCE:
				//这里对数据进行显示或做相应的处理
				provinceList=(String[]) msg.obj;
			case PROVINCECODE:
				provinceCodeList = (String[]) msg.obj;
				//测试
				try {
					//把内容加到activity
					dh_scroll_list.setAdapter(new VSelectAdapter(VSelectActivity.this, provinceList,provinceCodeList));

					dh_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							TextView iteminfo = (TextView) view.findViewById(R.id.itemTitle);
							String pid = (String) iteminfo.getTag();
							String pname = (String) iteminfo.getText();
							//跳转到新界面，把val传过去
							//                                 ToastUtils.showMessage(VSelectActivity.this, "" + pid + "," + iteminfo.getText());

							Intent intent = new Intent();
							intent.setClass(VSelectActivity.this, VSelectCityActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("pid", pid);
							bundle.putString("pname", pname);

							intent.putExtras(bundle);
							startActivityForResult(intent, RESULT_VSCITY);
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
				TCity tCity=new TCity();
				String[] provinces=tCity.fetchAllProvinceNames(VSelectActivity.this);
				String[] provinceCodes = tCity.fetchProvinceCode(VSelectActivity.this);
				Message message = new Message();
				message.what = UIHandler.PROVINCE;
				message.obj=provinces;
				mUiHandler.sendMessage(message); //发送消息给Handler
				Message message2 = new Message();
				message2.what = UIHandler.PROVINCECODE;
				message2.obj=provinceCodes;
				mUiHandler.sendMessage(message2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode)
		{
		case RESULT_VSCITY:
			if(data==null)break;
			Bundle bundle = data.getExtras();
			//返回给界面
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_VSCITY, intent);
			VSelectActivity.this.finish();
			break;
		}

	}
}