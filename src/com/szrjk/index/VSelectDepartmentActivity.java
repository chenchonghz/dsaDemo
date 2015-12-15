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
import com.szrjk.entity.TDept;
import com.szrjk.util.DCollectionUtils;

/**
 * denggm on 2015/10/20.
 * DHome
 * 科室列表
 */
@ContentView(R.layout.activity_vselect_department)
public class VSelectDepartmentActivity extends BaseActivity {

	final static String TAG = "VSelectDepartmentActivity";

	final static int RESULT_DEPARTMENT = 2;


	@ViewInject(R.id.dh_scroll_list)
	private ListView dh_scroll_list;

	private UIHandler mUiHandler;

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
		    //定义消息类型
		public static final int DEPARTMENT = 1;

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case DEPARTMENT:
				String[] subDeptName = (String[]) msg.obj;
				//测试
				try {
					//把内容加到activity
					dh_scroll_list.setAdapter(new VSelectDepartmentAdapter(VSelectDepartmentActivity.this, subDeptName));

					dh_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
							TextView iteminfo = (TextView) view.findViewById(R.id.itemTitle);
							String deptName = (String) iteminfo.getText();
							//跳转到新界面，把val传过去

							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putString("deptName", deptName);

							intent.putExtras(bundle);
							setResult(RESULT_DEPARTMENT, intent);
							VSelectDepartmentActivity.this.finish();
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
				List<TDept> tDepts = (new TDept()).fetchAllList(VSelectDepartmentActivity.this);
				if(tDepts!=null){
					String[] deptNames = DCollectionUtils.converFromList2(tDepts,
							"subDeptName");
				Message message = new Message();
				message.what = UIHandler.DEPARTMENT;
				message.obj=deptNames;
				mUiHandler.sendMessage(message); //发送消息给Handler
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
	}
	
}