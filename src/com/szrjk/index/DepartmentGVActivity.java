package com.szrjk.index;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.szrjk.adapter.DeptGridViewAdapter;
import com.szrjk.dhome.BaseActivity;
import com.szrjk.dhome.R;
import com.szrjk.widget.DeptButton;
import com.szrjk.widget.FlowDeptLayout;
import com.szrjk.widget.HeaderView;

@ContentView(R.layout.activity_dept_gv)
public class DepartmentGVActivity extends BaseActivity
{
	//头部控件
	@ViewInject(R.id.hv_dept)
	private HeaderView hv_dept;

	@ViewInject(R.id.gv_dep)
	private com.szrjk.widget.IndexGridView  gv_dep;

	@ViewInject(R.id.tv_xxgnk)
	private TextView tv_xxgnk;
	@ViewInject(R.id.btn_back)
	private ImageView btn_back;
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;
	private DepartmentGVActivity instance;
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
	private Resources res;

	private DeptGridViewAdapter gridViewAdapter;

	private int px;

	private ArrayList<String> listKey;

	private ArrayList<String> listValue;
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
		//		ACTIVITY = bundle.getInt(Constant.ACTIVITYENUM);
		deptIds = bundle.getString("DeptIds");
		deptValues = bundle.getString("DeptValues");
		res = this.getResources();


		initLayout();
		//----------------这里先测量控件高度---------------方法三     
		//getTextviewHeight();    
		//检查删除部门
		//		deleteDepartment();
		if (deptIds != null) {

			String[] strId = deptIds.split(";");
			String[] strValue = deptValues.split(";");
			listKey = new ArrayList<String>(Arrays.asList(strId));
			listValue = new ArrayList<String>(Arrays.asList(strValue));
			gridViewAdapter = new DeptGridViewAdapter(this, listKey,listValue,gv_dep);
			gv_dep.setAdapter(gridViewAdapter);
			System.out.println("传入");
			Log.i("listKey", listKey.toString());
			Log.i("listValue", listValue.toString());
		}else{
			listKey = new ArrayList<String>();
			listValue = new ArrayList<String>();
			gridViewAdapter = new DeptGridViewAdapter(this, listKey,listValue,gv_dep);
			gv_dep.setAdapter(gridViewAdapter);
		}
		setHeader();
	}

	private void setHeader() {
		//返回按钮
		hv_dept.setBtnBackOnClick(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent();
				Bundle b = new Bundle();
				b.putString("DeptValues", "null");
				b.putString("DeptIds", "null");
				intent.putExtras(bundle);
				setResult(Activity.RESULT_OK,i);
				finish();
			}
		});
		//确定按钮
		hv_dept.showTextBtn("确定", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try{
					returnData();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	//设置点击事件、包含删除操作
	@OnItemClick(R.id.gv_dep)
	public void checkItemClick(AdapterView<?> adapterView, View view, int position,long id) {
		//		typeCount = type_adapter.getCount();
		//		Log.e("PopupWindow", "可删除标题数："+typeCount);
		//		if(typeCount>1){
		//			String title = type_adapter.getTitle(position);
		//			String typeId = type_adapter.getTypeId(position);
		//			type_adapter.removeItem(position);
		//			deleteElement(title,typeId);
		//			}
		//		}
		String k = gridViewAdapter.getDeptId(position);
		String v = gridViewAdapter.getDept(position);
		//移除数据，更新适配器
		gridViewAdapter.removeItem(position);
		listKey.remove(k);
		listValue.remove(v);
		System.out.println("移除------");
		Log.i("k", k);
		Log.i("v", v);
		System.out.println("剩余======");
		Log.i("listKey", listKey.toString());
		Log.i("listValue", listValue.toString());

	}

	private void addButton(TextView v){
		if (listKey.size() >= 5) {
			showToast(this, "最多可选5个科室", 0);
		}else{

			if (! listKey.contains(v.getTag().toString())) {
				String key = v.getTag().toString();
				String val = v.getText().toString();
				System.out.println("增加部门 ++++");
				Log.i("key", key);
				Log.i("val", val);
				System.out.println("--------");
				System.out.println();
				listKey.add(key);
				listValue.add(val);
				System.out.println(listKey.toString());
				System.out.println(listValue.toString());
				gridViewAdapter.notifyDataSetChanged();
				//			gridViewAdapter = new DeptGridViewAdapter(this, listKey,listValue);
			}
		}
	}

	private void setStyle(DeptButton btnStyle) {
		//		btnStyle.setHeight(px);
		btnStyle.setPadding(12, 10, 12, 10);
		btnStyle.setTextColor(res.getColor(R.color.header_bg));
		btnStyle.setBackground(res.getDrawable(R.drawable.flow_dept_selector));
	}

	private void initLayout()
	{
		resources = getResources();
		sbKey = new StringBuffer();
		sbValue = new StringBuffer();
		getTextviewHeight();
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

	private void returnData() {
		if (listKey.isEmpty()) {
			finish();
		}else{
			//			deptIds = bundle.getString("DeptIds");
			//			deptValues = bundle.getString("DeptValues");
			//			px = bundle.getInt("height", 0);
			StringBuffer sbk = new StringBuffer();
			StringBuffer sbv = new StringBuffer();
			for (int i = 0; i < listKey.size(); i++) {
				sbk.append(listKey.get(i));
				sbk.append(";");
			}
			System.out.println("返回：" + listKey.toString());

			for (int i = 0; i < listValue.size(); i++) {
				sbv.append(listValue.get(i));
				sbv.append(";");
			}
			System.out.println("返回：" + sbv.toString());
			Bundle b = new Bundle();
			b.putString("DeptIds", sbk.toString());
			b.putString("DeptValues", sbv.toString());
			b.putInt("height", px);
			Intent intent = new Intent();
			intent.putExtras(b);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
	private void getTextviewHeight() {
		ViewTreeObserver vto2 = tv_xxgnk.getViewTreeObserver();    
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
			@SuppressWarnings("deprecation")
			@Override    
			public void onGlobalLayout() {  
				tv_xxgnk.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
				//	        	tv_rk.append("\n\n"+tv_fck.getHeight()+","+tv_fck.getWidth()); 
				px = tv_xxgnk.getHeight();
				Log.i("h", px+"");
			}    
		});
	}
}
