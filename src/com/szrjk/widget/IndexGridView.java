package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class IndexGridView extends GridView{

	private OnTouchInvalidPositionListener mTouchInvalidPosListener;

	public IndexGridView(Context context) {
		super(context);
	}

	public IndexGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public IndexGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		  int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	                MeasureSpec.AT_MOST);
		  //makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
		  //MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	public void setOnTouchInvalidPositionListener(OnTouchInvalidPositionListener listener) {
		mTouchInvalidPosListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(mTouchInvalidPosListener == null) {
		      return super.onTouchEvent(ev);
		    }
		    if (!isEnabled()) {
		      // A disabled view that is clickable still consumes the touch
		      // events, it just doesn't respond to them.
		      return isClickable() || isLongClickable();
		    }
		    final int motionPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
		    if( motionPosition == INVALID_POSITION ) {
		      super.onTouchEvent(ev);
		      return mTouchInvalidPosListener.onTouchInvalidPosition(ev.getActionMasked());
		    }
		    return super.onTouchEvent(ev);
		  }
	
	
	public interface OnTouchInvalidPositionListener {
		/**
		 * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者 MotionEvent.ACTION_UP等来按需要进行判断
		 * @return 是否要终止事件的路由
		 */
		boolean onTouchInvalidPosition(int motionEvent);
	}

}
	
	

