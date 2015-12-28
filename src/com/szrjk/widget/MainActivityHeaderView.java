package com.szrjk.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.szrjk.dhome.AddressBookActivity;
import com.szrjk.dhome.R;
import com.szrjk.search.SearchMainActivity;
import com.szrjk.self.CreateCircleActivity;
import com.szrjk.self.more.CaseSharePostActivity;
import com.szrjk.self.more.ProblemHelpActivity;

public class MainActivityHeaderView extends RelativeLayout implements
		OnClickListener {

	private Context context;
	private TextView tv_digital_doctor;
	private ImageView iv_linklist;
	private ImageView iv_search;
	private ImageView iv_add;
	private RelativeLayout rl_headerview_mainactivity;

	public MainActivityHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.headerview_mainactivity, this);
		rl_headerview_mainactivity=(RelativeLayout)findViewById(R.id.rl_headerview_mainactivity);
		tv_digital_doctor = (TextView) findViewById(R.id.tv_digital_doctor);
		iv_linklist = (ImageView) findViewById(R.id.iv_linklist);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_add = (ImageView) findViewById(R.id.iv_add);

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
				R.styleable.MainActivityHeaderView);
		String mtext = typedArray
				.getString(R.styleable.MainActivityHeaderView_mtext);
		typedArray.recycle();
		tv_digital_doctor.setText(mtext);
		iv_linklist.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		iv_add.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_linklist:
			Intent intent1=new Intent(context, AddressBookActivity.class);
			context.startActivity(intent1);
			break;
		case R.id.iv_search:
			Intent intent2=new Intent(context, SearchMainActivity.class);
			context.startActivity(intent2);
			break;
		case R.id.iv_add:
			AddPopup addPopup=new AddPopup((Activity)context, new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent=null;
					switch (view.getId()) {
					case R.id.ll_creatcoterie:
						intent=new Intent(context, CreateCircleActivity.class);
						context.startActivity(intent);
						break;
					case R.id.ll_caseshare:
						intent=new Intent(context, CaseSharePostActivity.class);
						intent.putExtra("postType", "ALL");
						context.startActivity(intent);
						break;
					case R.id.ll_puzzlehelp:
						intent=new Intent(context, ProblemHelpActivity.class);
						intent.putExtra("postType", "ALL");
						context.startActivity(intent);
						break;
					}
				}
			});
//			addPopup.showAsDropDown(iv_add, -50, 5);
//			Log.i("dp", px2dip(15)+"");//10
//			Log.i("dp", px2dip(95)+"");//63
			addPopup.showAtLocation(rl_headerview_mainactivity, Gravity.RIGHT|Gravity.TOP, dip2px(8), dip2px(63));
			break;
		}
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public int dip2px(float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public int px2dip(float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
