package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ScrollView;


public class InnerListView extends ListView{
//	private ScrollView parentScrollView;
//	private int maxHeight;

	public InnerListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
//	public void setParentScrollView(ScrollView scrollView){
//		this.parentScrollView = scrollView;
//	}
//	
//	
//	
//	public int getMaxHeight() {
//		return maxHeight;
//	}
//
//	public void setMaxHeight(int maxHeight) {
//		this.maxHeight = maxHeight;
//	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			setParentScrollAble(false);
//			break;
//		case MotionEvent.ACTION_CANCEL:
//			setParentScrollAble(true);
//			break;
//		default:
//			break;
//		}
//		return super.onInterceptTouchEvent(ev);
//	}
//
//	private void setParentScrollAble(boolean b) {
//		// TODO Auto-generated method stub
//		if(parentScrollView != null){
//			parentScrollView.requestDisallowInterceptTouchEvent(!b);
//		}
//	}
//	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	                MeasureSpec.AT_MOST);
		  //makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
		  //MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	
	


}
