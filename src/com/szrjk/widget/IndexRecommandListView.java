package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class IndexRecommandListView extends ListView{

	public IndexRecommandListView(Context context) {
		super(context);
	}

	public IndexRecommandListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public IndexRecommandListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	                MeasureSpec.AT_MOST);
		  //makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
		  //MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
