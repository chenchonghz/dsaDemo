package com.szrjk.index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.ActivityEnum;
import com.szrjk.widget.DeptButton;
import com.szrjk.widget.FlowDeptLayout;

@ContentView(R.layout.activity_dept)
public class DepartmentActivity extends BaseActivity
{
	@ViewInject(R.id.gv_dep)
	private GridView gv_dep;
	
	@ViewInject(R.id.tv_xxgnk)
	private TextView tv_xxgnk;
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	private DepartmentActivity instance;
	private String deptIds, deptValues;
	@ViewInject(R.id.fl_content)
	private FlowDeptLayout fl_content;
	private Resources resources;

	@ViewInject(R.id.tv_nk)
	private TextView tv_nk;
	@ViewInject(R.id.tv_wk)
	private TextView tv_wk;
//	@ViewInject(R.id.tv_fck)
//	private TextView tv_fck;
//	@ViewInject(R.id.tv_rk)
//	private TextView tv_rk;
//	@ViewInject(R.id.tv_ok)
//	private TextView tv_ok;
	private StringBuffer sbKey;
	private StringBuffer sbValue;
	private Intent intent;
	private Bundle bundle;
	private int ch;
	private static int ACTIVITY = 0;
	private int i ;
	private String[] strings;//key
	private String[] strValues;//值
	private Resources res;
	
	
	/**
	 * 发现问题。绑定监听之前。部门的id，value，都是空的、
	 * 
	 * 部门ID来源，就是addDeptment 传入上一个界面过来的部门。new 2 个stringBuffer；分别存放ID和value
	 * 
	 * 当进来的时候是有部门的；然后点击删除了；就要把sb里面的对应删除；
	 * 
	 * 最后在入参的时候：判断sb的长度 	少于0 就不给入参
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		ViewUtils.inject(this);
		intent = getIntent();
		bundle = intent.getExtras();
		ACTIVITY = bundle.getInt(Constant.ACTIVITYENUM);
		deptIds = bundle.getString("DeptIds");
		deptValues = bundle.getString("DeptValues");
		res = this.getResources();
		
		
		initLayout();
	    //----------------这里先测量控件高度---------------方法三     
	    getTextviewHeight();    
		//检查删除部门
//		deleteDepartment();
		
	}
	
	@Override
	protected void onResume() {
		//获得控件高度
				
		super.onResume();
	}
	int px ;
	private void addDeptment(String ids, String values)
	{
		String[] strIds = ids.split(";");
		strValues = values.split(";");
		Log.i("增加科室前strValues", Arrays.toString(strValues));
		int i = 0;
		for (String id : strIds)
		{
			sbKey.append(id);
			sbKey.append(";");
			sbValue.append(strValues[i]);
			sbValue.append(";");
			int txtColor = resources.getColor(R.color.black);
			DeptButton btn = new DeptButton(this);
			btn.setTextColor(txtColor);
			btn.setGravity(Gravity.CENTER);
			
			setStyle(btn);  
			
			btn.setTag(id);
			btn.setText(strValues[i]);
			i++;
			fl_content.addView(btn);
			
			
		}
		
		
		
		strings = sbKey.toString().split(";");
		strValues = sbValue.toString().split(";");
		
		Log.i("增加科室Values", Arrays.toString(strValues));
		deleteDepartment();
	}
	
	private void deleteDepartment(){
		ch = fl_content.getChildCount();
		switch (ch) {
		case 1:
			delButton(ch);
			break;
		case 2:
			delButton(ch);
			break;
		case 3:
			delButton(ch);
			break;
		case 4:
			delButton(ch);
			break;
		case 5:
			delButton(ch);
			break;
		}
		//删除完之后要知道里面还有多少个子控件
		ch = fl_content.getChildCount();
	}
	int j ;
	//绑定控件的点击事件
	private void delButton(int ch2) {
		for (j = 0; i < fl_content.getChildCount(); i++) {
			fl_content.getChildAt(j).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					fl_content.removeView(v);
					deleteElement(j);
				}
			});
		}
	}
	//修改了储存部门的数组
	private void deleteElement(int k){
		System.out.println("删除科室位置：" + k);
		List<String> listKey= new ArrayList<String>(Arrays.asList(strings));
		List<String> listValue= new ArrayList<String>(Arrays.asList(strValues));
		
		
		String ktemp = listKey.remove(k);
		System.out.println(ktemp);
		String vtemp = listValue.remove(k);
		System.out.println(vtemp);
		strings = new String[listKey.size()];
		strValues = new String[listValue.size()];
		listKey.toArray(strings);
		listValue.toArray(strValues);
		
		sbKey = new StringBuffer();
		sbValue = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			sbKey.append(strings[i]);
			sbKey.append(";");
			sbValue.append(strValues[i]);
			sbValue.append(";");
		}
		//当删除了某个控件之后，要重新绑定一次点击事件，因为会错位，index会越界
		System.out.println("剩余科室");
		System.out.println(sbKey.toString());
		System.out.println(sbValue.toString());
		delButton(fl_content.getChildCount());
	}
	@SuppressLint("NewApi")
	private void addButton(TextView view)
	{
		strings = sbKey.toString().split(";");
		strValues = sbValue.toString().split(";");
		if (strings.length >= 5)
		{
			showToast(instance, "最多只能选择5个科室", Toast.LENGTH_SHORT);
		}
		else
		{
			if (!sbKey.toString().contains(view.getTag().toString()))
			{
				sbKey.append(view.getTag());
				sbKey.append(";");
				sbValue.append(view.getText().toString());
				sbValue.append(";");
				int txtColor = resources.getColor(R.color.black);
				Log.i("view.getTag()", view.getTag()+"");
				Log.i("view.getText().toString()", view.getText().toString());
				DeptButton btn = new DeptButton(this);
				btn.setTextColor(txtColor);
				btn.setGravity(Gravity.CENTER);
				
				setStyle(btn);
				
				btn.setText(view.getText().toString());
				btn.setTag(view.getTag());
				fl_content.addView(btn);
			}
		}
		//因为里面的值，删除的时候会修改，所以，增加完的时候要重新赋值数组一次。并且绑定监听遍历一次；
		strings = sbKey.toString().split(";");
		strValues = sbValue.toString().split(";");
		System.out.println("增加按钮-----------------");
		Log.i("sbKey", sbKey.toString());
		Log.i("strValues", Arrays.toString(strValues));
		System.out.println("-----------------");
		deleteDepartment();
	}
	private void setStyle(DeptButton btnStyle) {
		btnStyle.setHeight(px);
		btnStyle.setPadding(12, 10, 12, 10);
		btnStyle.setTextColor(res.getColor(R.color.header_bg));
		btnStyle.setBackground(res.getDrawable(R.drawable.flow_dept_selector));
	}

	private void initLayout()
	{
		resources = getResources();
		sbKey = new StringBuffer();
		sbValue = new StringBuffer();
	}

	@OnClick(R.id.tv_nk)
	public void nkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

	@OnClick(R.id.tv_wk)
	public void wkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	@OnClick(R.id.tv_fck)
//	public void fckClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//	儿科
//	@OnClick(R.id.tv_rk)
//	public void rkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//	五官科
//	@OnClick(R.id.tv_wgk)
//	public void wgkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
////	皮肤科
//	@OnClick(R.id.tv_pfk)
//	public void pfkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
////	中医科
//	@OnClick(R.id.tv_zyk)
//	public void zykClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
////	急诊科
//	@OnClick(R.id.tv_jzk)
//	public void jzkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
////	麻醉科
//	@OnClick(R.id.tv_mzk)
//	public void mzkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
////	其他
//	@OnClick(R.id.tv_ok)
//	public void okClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//	心血管内科
	@OnClick(R.id.tv_xxgnk)
	public void xxgnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	呼吸内科
	@OnClick(R.id.tv_hxnk)
	public void hxnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	消化

	@OnClick(R.id.tv_xhnk)
	public void xhnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//肿瘤内科
	@OnClick(R.id.tv_zlnk)
	public void zlnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	内分泌科
	@OnClick(R.id.tv_ndnk)
	public void ndnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	神经内科
	@OnClick(R.id.tv_sjnk)
	public void sjnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	肾脏内科
	@OnClick(R.id.tv_sznk)
	public void sznkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	血液内科
	@OnClick(R.id.tv_xynk)
	public void xynkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//感染内科
	@OnClick(R.id.tv_grnk)
	public void grnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//风湿免疫内科
	@OnClick(R.id.tv_fsmynk)
	public void fsmykClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	普通外科
	@OnClick(R.id.tv_pk)
	public void pkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	骨外科
	@OnClick(R.id.tv_gk)
	public void gkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	泌尿科
	@OnClick(R.id.tv_mnk)
	public void mnkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	神经外科
	@OnClick(R.id.tv_sjk)
	public void sjkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	胸心外科
	@OnClick(R.id.tv_xxk)
	public void xxkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	烧伤整形
	@OnClick(R.id.tv_sxk)
	public void sxkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//器官移植
	@OnClick(R.id.tv_wkk)
	public void wkkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
//	妇科
	@OnClick(R.id.tv_fk)
	public void fkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	产科
	@OnClick(R.id.tv_ck)
	public void ckClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	生殖医学
	@OnClick(R.id.tv_szk)
	public void szkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	小儿内科
	@OnClick(R.id.tv_xenk)
	public void xenkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	小儿外科
	@OnClick(R.id.tv_xrwk)
	public void xrwkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
////	耳鼻咽喉头颈外科
//	@OnClick(R.id.tv_ebyhtxwk)
//	public void onkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//	
////	口腔
//	@OnClick(R.id.tv_kqk)
//	public void onkqClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//
////	眼科
//	@OnClick(R.id.tv_eyek)
//	public void oneyeClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//	
////	皮肤性病科
//	@OnClick(R.id.tv_pfxbk)
//	public void onpfxbClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//
////	针灸
//	@OnClick(R.id.tv_zjk)
//	public void onpzjkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//
////	推拿按摩科
//	@OnClick(R.id.tv_tnnmk)
//	public void onptnnmClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//
////	料理
//	@OnClick(R.id.tv_llk)
//	public void onpllkClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}
//
////	重症医学科
//	@OnClick(R.id.tv_zzyxk)
//	public void onzzyxClick(View v)
//	{
//		TextView view = (TextView) v;
//		addButton(view);
//	}

	
//	病理科
	@OnClick(R.id.tv_blk)
	public void onblkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	心电图
	@OnClick(R.id.tv_xdts)
	public void onxdtsClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	超声诊断
	@OnClick(R.id.tv_cszdk)
	public void oncszdClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	医学影像
	@OnClick(R.id.tv_yxyxk)
	public void onyxyxClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	检验科
	@OnClick(R.id.tv_jyk)
	public void onjykClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	内窥
	@OnClick(R.id.tv_nkjk)
	public void onnkjClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	放射治疗
	@OnClick(R.id.tv_fszlk)
	public void onfszlClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	介入医学
	@OnClick(R.id.tv_jryxk)
	public void onjryxClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}
	
//	精神心里
	@OnClick(R.id.tv_jsxlk)
	public void onjsxlClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//康复
	@OnClick(R.id.tv_kfk)
	public void crkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

//	营养
	@OnClick(R.id.tv_yyk)
	public void jsxkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

	@OnClick(R.id.tv_other)
	public void phkClick(View v)
	{
		TextView view = (TextView) v;
		addButton(view);
	}

	




	

	@OnClick(R.id.tv_submit)
	public void submitClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tv_submit:
				jumpSendPuzzleActivity();
				break;
		}
	}
	
	@OnClick(R.id.btn_back)
	public void onBack(View v){
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putString("DeptValues", "null");
		b.putString("DeptIds", "null");
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK,i);
		finish();
	}

	private void jumpSendPuzzleActivity()
	{
		intent = new Intent();
		if (ACTIVITY == ActivityEnum.SeedCaseActivity1.value())
		{
			intent.setClass(instance, SendPostActivity.class);
		}
		else if (ACTIVITY == ActivityEnum.SeedPuzzleActivity1.value())
		{
			intent.setClass(instance, SendPuzzleActivity.class);
		}
		bundle = new Bundle();
		
		
		if (sbKey.length() > 0)
		{
			deptIds = sbKey.deleteCharAt(sbKey.length() - 1).toString();
			Log.i("返回发帖/写入bundle：deptIds", deptIds);
			
			bundle.putString("DeptIds", deptIds);
		}
		
		
		if (sbValue.length() > 0)
		{
			deptValues = sbValue.deleteCharAt(sbValue.length() - 1).toString();
			Log.i("返回发帖/写入bundle：DeptValues", deptValues);
			bundle.putString("DeptValues", deptValues);
		}
		bundle.putInt("height", px);
		
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	private void getTextviewHeight() {
		ViewTreeObserver vto2 = tv_xxgnk.getViewTreeObserver();    
	    vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
	        @Override    
	        public void onGlobalLayout() {  
	        	tv_xxgnk.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
//	        	tv_rk.append("\n\n"+tv_fck.getHeight()+","+tv_fck.getWidth()); 
	        	px = tv_xxgnk.getHeight();
	        	Log.i("h", px+"");
	        	//顺序不能乱、这里必须有高度、才可以增加部门、
	    		if (null != deptIds)
	    		{
	    			 Log.i("h", px+"");
	    			addDeptment(deptIds, deptValues);
	    		}
	        }    
	    });
	}
}
