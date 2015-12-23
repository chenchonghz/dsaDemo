package com.szrjk.self.more.album;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public abstract class AlbumPopListView<T> extends PopupWindow{

	/**
	 * 布局文件的最外层View
	 */
	protected View mContentView;
	protected Context context;
	/**
	 * ListView的数据集
	 */
	protected List<PhotoUpImageBucket> mDatas;

	public AlbumPopListView(View contentView, int width, int height,
			boolean focusable)
	{
		this(contentView, width, height, focusable, null);
	}

	public AlbumPopListView(View contentView, int width, int height,
			boolean focusable, List<PhotoUpImageBucket> datas)
	{
		this(contentView, width, height, focusable, datas, new Object[0]);

	}

	public AlbumPopListView(View contentView, int width, int height,
			boolean focusable, List<PhotoUpImageBucket> datas, Object... params)
	{
		super(contentView, width, height, focusable);
		this.mContentView = contentView;
		context = contentView.getContext();
		if (datas != null)
//			Collections.reverse(mDatas);
			this.mDatas = datas;

		if (params != null && params.length > 0)
		{
			beforeInitWeNeedSomeParams(params);
		}

		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
				{
					dismiss();
					return true;
				}
				return false;
			}
		});
		initViews();
		initEvents();
		init();
	}

	protected abstract void beforeInitWeNeedSomeParams(Object... params);

	public abstract void initViews();

	public abstract void initEvents();

	public abstract void init();

	public View findViewById(int id)
	{
		return mContentView.findViewById(id);
	}

	protected static int dpToPx(Context context, int dp)
	{
		return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
	}

}
