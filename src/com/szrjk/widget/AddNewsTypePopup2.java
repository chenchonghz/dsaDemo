package com.szrjk.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.color;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.szrjk.adapter.ExploreMyTypeGridViewAdapter;
import com.szrjk.adapter.TabPageIndicatorAdapter;
import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.entity.ErrorInfo;
import com.szrjk.explore.MoreNewsActivity;
import com.szrjk.http.AbstractDhomeRequestCallBack;
import com.szrjk.http.DHttpService;
import com.szrjk.util.ToastUtils;

public class AddNewsTypePopup2 extends PopupWindow implements OnClickListener{

	private View mChooseTypeView;
	private ArrayList<String> title_id;
	private ArrayList<String> indicatorTitle;
	private ArrayList<String> title_id_copy;
	private ArrayList<String> indicatorTitle_copy;
	private MoreNewsActivity context;
	private LinearLayout ll_indicator;
	private Button bt_more_type;
	private Button bt_more_type_close;
	private LinearLayout ll_my_news;
	private TabPageIndicatorAdapter adapter;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private ViewPager viewPager;
	private Handler handler;
	private boolean canRemove = true;
	private int scrollViewWidth;
	private int typeCount;
	private int h;
	private int w;
	private IndexGridView gv_my_type;
	private ExploreMyTypeGridViewAdapter type_adapter;
	private Resources res;
	private TextView tv_ek,tv_nk,tv_wk,tv_zy,tv_fc,tv_wg,tv_pf,tv_jz,tv_mz,tv_qt,
	                 tv_xy,tv_yx,tv_za,tv_bl,tv_yj,tv_qy,tv_zn,tv_mj,tv_rd,tv_zt,
	                 tv_ht,tv_ft,tv_hd,tv_zc,tv_dj;
	
	
//	@SuppressLint("NewApi")
	public AddNewsTypePopup2(MoreNewsActivity context,ArrayList<String> titles_id,
			ArrayList<String> indicatorTitle,LinearLayout ll_indicator, Button bt_more_type, Button bt_more_type_close, LinearLayout ll_my_news, ColumnHorizontalScrollView mColumnHorizontalScrollView, TabPageIndicatorAdapter mAdapter, ViewPager viewPager, Handler handler, int scrollViewHeight) {
		super();
		try {
			this.context = context;
			if(titles_id != null){	
				this.title_id = titles_id;
			}else{
				title_id = new ArrayList<String>();
			}
			if(indicatorTitle != null){		
				this.indicatorTitle = indicatorTitle;
			}else{
				indicatorTitle = new ArrayList<String>();
			}
			this.ll_indicator = ll_indicator;
			this.bt_more_type = bt_more_type;
			this.bt_more_type_close = bt_more_type_close;
			this.ll_my_news = ll_my_news;
			this.mColumnHorizontalScrollView = mColumnHorizontalScrollView;
			this.adapter = mAdapter;
			this.viewPager = viewPager;
			this.scrollViewWidth = scrollViewHeight;
			title_id_copy = title_id;
			indicatorTitle_copy = indicatorTitle;
			this.handler = handler;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mChooseTypeView = inflater.inflate(R.layout.popup_explore, null);
			tv_ek = (TextView)mChooseTypeView.findViewById(R.id.tv_ek);
			tv_nk = (TextView)mChooseTypeView.findViewById(R.id.tv_nk);
			tv_wk = (TextView)mChooseTypeView.findViewById(R.id.tv_wk);
			tv_zy = (TextView)mChooseTypeView.findViewById(R.id.tv_zy);
			tv_fc = (TextView)mChooseTypeView.findViewById(R.id.tv_fc);
			tv_wg = (TextView)mChooseTypeView.findViewById(R.id.tv_wg);
			tv_pf = (TextView)mChooseTypeView.findViewById(R.id.tv_pf);
			tv_jz = (TextView)mChooseTypeView.findViewById(R.id.tv_jz);
			tv_mz = (TextView)mChooseTypeView.findViewById(R.id.tv_mz);
			tv_qt = (TextView)mChooseTypeView.findViewById(R.id.tv_qt);
			tv_xy = (TextView)mChooseTypeView.findViewById(R.id.tv_xy);
			tv_yx = (TextView)mChooseTypeView.findViewById(R.id.tv_yx);
			tv_za = (TextView)mChooseTypeView.findViewById(R.id.tv_za);
			tv_bl = (TextView)mChooseTypeView.findViewById(R.id.tv_bl);
			tv_yj = (TextView)mChooseTypeView.findViewById(R.id.tv_yj);
			tv_qy = (TextView)mChooseTypeView.findViewById(R.id.tv_qy);
			tv_zn = (TextView)mChooseTypeView.findViewById(R.id.tv_zn);
			tv_zc = (TextView)mChooseTypeView.findViewById(R.id.tv_zc);
			tv_mj = (TextView)mChooseTypeView.findViewById(R.id.tv_mj);
			tv_rd = (TextView)mChooseTypeView.findViewById(R.id.tv_rd);
			tv_zt = (TextView)mChooseTypeView.findViewById(R.id.tv_zt);
			tv_ht = (TextView)mChooseTypeView.findViewById(R.id.tv_ht);
			tv_dj = (TextView)mChooseTypeView.findViewById(R.id.tv_dj);
			tv_ft = (TextView)mChooseTypeView.findViewById(R.id.tv_ft);
			tv_hd = (TextView)mChooseTypeView.findViewById(R.id.tv_hd);
			tv_ek.setOnClickListener(this);
			tv_nk.setOnClickListener(this);
			tv_wk.setOnClickListener(this);
			tv_zy.setOnClickListener(this);
			tv_fc.setOnClickListener(this);
			tv_wg.setOnClickListener(this);
			tv_pf.setOnClickListener(this);
			tv_jz.setOnClickListener(this);
			tv_mz.setOnClickListener(this);
			tv_qt.setOnClickListener(this);
			tv_xy.setOnClickListener(this);
			tv_yx.setOnClickListener(this);
			tv_za.setOnClickListener(this);
			tv_bl.setOnClickListener(this);
			tv_yj.setOnClickListener(this);
			tv_qy.setOnClickListener(this);
			tv_zn.setOnClickListener(this);
			tv_zc.setOnClickListener(this);
			tv_mj.setOnClickListener(this);
			tv_rd.setOnClickListener(this);
			tv_zt.setOnClickListener(this);
			tv_ht.setOnClickListener(this);
			tv_dj.setOnClickListener(this);
			tv_ft.setOnClickListener(this);
			tv_hd.setOnClickListener(this);
			gv_my_type = (IndexGridView)mChooseTypeView.findViewById(R.id.gv_my_type);
			res = context.getResources();
			getTextviewHeight();
			this.setContentView(mChooseTypeView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(android.view.ViewGroup.LayoutParams.FILL_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			ColorDrawable dw = new ColorDrawable(color.white);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
//			this.showAsDropDown(ll_indicator, 0, 0, Gravity.CENTER_HORIZONTAL);
//			this.showAtLocation(context.findViewById(R.id.ll_more_news_activity),Gravity.BOTTOM, 200, 220);
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("MoreNewsActivity", e.toString());
		}

		
		Log.e("PopupWindow", title_id.toString());
		Log.e("PopupWindow", indicatorTitle.toString());
		setDissMiss();
		
	}
	
	private void setDissMiss() {
		// TODO Auto-generated method stub
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Log.e("PopupWindow", title_id.toString());
				Log.e("PopupWindow", indicatorTitle.toString());
				String[] infCols = title_id_copy.toArray(new String[title_id_copy.size()]);
				updateType(infCols);
				ObjectAnimator animator = ObjectAnimator.ofFloat(bt_more_type, "rotation", 180f, 360f);
				animator.setDuration(300);  
				animator.start();
				ObjectAnimator show = ObjectAnimator.ofFloat(mColumnHorizontalScrollView, "alpha", 0f, 1f);
				show.setDuration(300);  
				show.start();
				Log.e("PopWindow", "长度："+scrollViewWidth);
				ObjectAnimator out = ObjectAnimator.ofFloat(ll_my_news, "translationX", 0, scrollViewWidth);  
				ObjectAnimator gone = ObjectAnimator.ofFloat(ll_my_news, "alpha", 1f, 0f);
				AnimatorSet animSet = new AnimatorSet();
				animSet.play(out).with(gone);
				animSet.setDuration(300);  
				animSet.start();
//				bt_more_type.setVisibility(View.VISIBLE);
//				ll_my_news.setVisibility(View.GONE);
				mColumnHorizontalScrollView.setVisibility(View.VISIBLE);
//				bt_more_type_close.setVisibility(View.GONE);

			}
		});
	}

	private void getTextviewHeight() {
		// TODO Auto-generated method stub
		ViewTreeObserver vto2 = tv_bl.getViewTreeObserver();    
	    vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
	        @Override    
	        public void onGlobalLayout() {  
	        	tv_bl.getViewTreeObserver().removeGlobalOnLayoutListener(this);    
//	        	tv_rk.append("\n\n"+tv_fck.getHeight()+","+tv_fck.getWidth()); 
	        	h = tv_bl.getHeight();
	        	w = tv_bl.getWidth();
	        	Log.i("h", h+"");
	        	Log.i("w", w+"");
	        	//顺序不能乱、这里必须有高度、才可以增加部门、
	    			addType();
	        }    
	    });
	}

	protected void addType() {
		// TODO Auto-generated method stub
		if(title_id_copy.isEmpty()){
			title_id_copy.add("RD");
		}
		if(indicatorTitle.isEmpty()){
			indicatorTitle_copy.add("热点");
		}
		type_adapter = new ExploreMyTypeGridViewAdapter(context, indicatorTitle_copy, title_id_copy, w, h);
		gv_my_type.setAdapter(type_adapter);
		setTextViewListner();
		
	}

	private void setTextViewListner() {
		// TODO Auto-generated method stub
		typeCount = type_adapter.getCount();
		Log.e("PopupWindow", "可删除标题数："+typeCount);
		gv_my_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				typeCount = type_adapter.getCount();
				Log.e("PopupWindow", "可删除标题数："+typeCount);
				if(typeCount>1){
					String title = type_adapter.getTitle(position);
					String typeId = type_adapter.getTypeId(position);
					type_adapter.removeItem(position);
					deleteElement(title,typeId);
					}
				}
		});
		

//			fl_type.getChildAt(i).setOnLongClickListener(new OnLongClickListener() {
//				
//				@Override
//				public boolean onLongClick(View v) {
//					// TODO Auto-generated method stub
//					changeButtonStyle();
//					return true;
//				}
//			});
		}

//	@SuppressLint("NewApi")
//	protected void changeButtonStyle() {
//		// TODO Auto-generated method stub
//		for(int j = 0;j<textViewCount;j++){
//			DeptButton btn = (DeptButton) fl_type.getChildAt(j);
////			LayoutParams lp = new LayoutParams(w, h+50);
////			lp.setMargins(15, 15, 15, 15);
////			btn.setLayoutParams(lp);
//////			btn.setPadding(12, 10, 12, 10);
////			btn.setTextColor(res.getColor(R.color.font_titleanduname));
////			btn.setTextSize(12);
//			btn.setBackground(res.getDrawable(R.drawable.icon_column3));
//			btn.setGravity(Gravity.CENTER);
//		}
//		canRemove = true;
//	}

	protected void deleteElement(String title,String typeId) {
		// TODO Auto-generated method stub
		title_id_copy.remove(typeId);
		indicatorTitle_copy.remove(title);
//		type_adapter.notifyDataSetChanged();
//		setTextViewListner();
	}

	@SuppressLint("NewApi")
	private void setStyle(DeptButton btn) {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(w, h);
		lp.setMargins(20, 20, 20, 20);
		btn.setLayoutParams(lp);
		btn.setPadding(12, 10, 12, 10);
		btn.setTextColor(res.getColor(R.color.font_titleanduname));
		btn.setTextSize(12);
		btn.setBackground(res.getDrawable(R.drawable.icon_column2));
	}

//	@SuppressLint("NewApi")
//	public void showPopup(int popupHeight){
//
//	}
	
	protected void updateType(String[] infCols) {
		// TODO Auto-generated method stub
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ServiceName", "updateUserInfCol");
		Map<String, Object> busiParams = new HashMap<String, Object>();
		busiParams.put("userSeqId", Constant.userInfo.getUserSeqId());
		busiParams.put("infCols", infCols);
		paramMap.put("BusiParams", busiParams);
		DHttpService.httpPost(paramMap, new AbstractDhomeRequestCallBack() {
			
			@Override
			public void success(JSONObject jsonObject) {
				// TODO Auto-generated method stub
				ErrorInfo errorObj = JSON.parseObject(
						jsonObject.getString("ErrorInfo"), ErrorInfo.class);
				if (Constant.REQUESTCODE.equals(errorObj.getReturnCode()))
				{
					Message msg = new Message();
					msg.what = Constant.HAVE_NEW_TYPE;
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("title_id", title_id_copy);
					bundle.putStringArrayList("title", indicatorTitle_copy);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = Constant.HAVE_NEW_TYPE;
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("title_id", title_id);
					bundle.putStringArrayList("title", indicatorTitle);
					msg.setData(bundle);
					handler.sendMessage(msg);
					ToastUtils.showMessage(context, "更新栏目失败");
				}
			}
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void failure(HttpException exception, JSONObject jobj) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = Constant.HAVE_NEW_TYPE;
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("title_id", title_id);
				bundle.putStringArrayList("title", indicatorTitle);
				msg.setData(bundle);
				handler.sendMessage(msg);
				ToastUtils.showMessage(context, "更新栏目失败，请检查网络");
			}
		});
	}

	private void addNewType(TextView view){
		if(!this.title_id_copy.contains(view.getTag().toString())){
			String type = view.getTag().toString();
			String title = view.getText().toString();
			this.title_id_copy.add(type);
			this.indicatorTitle_copy.add(title);
			type_adapter.notifyDataSetChanged();
//			setTextViewListner();
		}else{
			ToastUtils.showMessage(context, "此栏目已添加");
		}
	}
	
	public void dissMissPopup(){
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView view = (TextView)v;
//		returnButtonStyle();
		addNewType(view);
	}

//	private void returnButtonStyle() {
//		// TODO Auto-generated method stub
//		Log.e("PopWindow", "Button数量："+textViewCount);
//		for(int j = 0;j<textViewCount;j++){
//			DeptButton btn = (DeptButton) fl_type.getChildAt(j);
////			setStyle(btn);
//		}
//	}
	
	
	
	
}
