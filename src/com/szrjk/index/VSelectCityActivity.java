package com.szrjk.index;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
@ContentView(R.layout.activity_vselectcity)
public class VSelectCityActivity extends BaseActivity {

	final static String TAG = "VSelectCityActivity";

	final static int REUSLT_CITY = 20;


	@ViewInject(R.id.dh_scroll_list)
	private ListView dh_scroll_list;

	private UIHandler mUiHandler;

	public String[] cityList;

	public String[] cityCodeList;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		Bundle paramBundle =  getIntent().getExtras();
		//测试
		try {
			//把内容加到activity
			final String pid = paramBundle.getString("pid");
			final String pname = paramBundle.getString("pname");
			loadBigData(pid, pname);

		} catch (Exception e) {
			Log.e(TAG,"",e);
		}
	}
	private void loadBigData(String pid,String pname){
		mUiHandler = new UIHandler(pid,pname); //创建uihandler
		new LoadDataThread(pid).start();  //创建线程并启动
	}   

	private class UIHandler  extends Handler {
		static final int CITYNAME = 0;    //定义消息类型
		public static final int CITYCODE = 1;
		private String pid;
		private String pname;
		public UIHandler(String pid, String pname) {
			this.pid=pid;
			this.pname=pname;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CITYNAME:
				//这里对数据进行显示或做相应的处理
				cityList=(String[]) msg.obj;
			case CITYCODE:
				cityCodeList=(String[]) msg.obj;
				try {
					dh_scroll_list.setAdapter(new VSelectAdapter(VSelectCityActivity.this, cityList,cityCodeList));
					dh_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							TextView iteminfo = (TextView) view.findViewById(R.id.itemTitle);
							String cid = (String) iteminfo.getTag();
							String cname = (String) iteminfo.getText();
							//跳转到新界面，把val传过去
							//                             ToastUtils.showMessage(VSelectCityActivity.this, "" + cid + "," + iteminfo.getText());

							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putString("pcode", pid);
							bundle.putString("pname",pname);
							bundle.putString("ccode", cid);
							bundle.putString("cname", cname);

							intent.putExtras(bundle);
							setResult(REUSLT_CITY, intent);
							VSelectCityActivity.this.finish();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}


			}

		}
	}

	private class LoadDataThread extends Thread{
		private String pid;

		public LoadDataThread(String pid) {
			this.pid=pid;
		}
		@Override
		public void  run(){
			try {
				TCity tCity=new TCity();
				String[] cityNames=tCity.fetchAllCityNames(VSelectCityActivity.this,pid);
				String[] cityCodes = tCity.fetchAllCityCodes(VSelectCityActivity.this, pid);
				Message message = new Message();
				message.what = UIHandler.CITYNAME;
				message.obj=cityNames;
				mUiHandler.sendMessage(message); //发送消息给Handler
				Message message2 = new Message();
				message2.what = UIHandler.CITYCODE;
				message2.obj=cityCodes;
				mUiHandler.sendMessage(message2);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}  
	}
}