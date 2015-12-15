package com.szrjk.widget;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class TextSizePopup extends PopupWindow
{
	private TextView tv_large, tv_medium, tv_small, tv_cancel;
	private View mMenuView;

	public TextSizePopup(Activity context, OnClickListener itemsOnClick)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_text_size, null);
		tv_large = (TextView) mMenuView.findViewById(R.id.tv_large);
		tv_medium = (TextView) mMenuView.findViewById(R.id.tv_medium);
		tv_small = (TextView) mMenuView.findViewById(R.id.tv_small);
		tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
		// 取消按钮
		tv_cancel.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		tv_large.setOnClickListener(itemsOnClick);
		tv_medium.setOnClickListener(itemsOnClick);
		tv_small.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (y < height)
					{
						dismiss();
					}
				}
				return true;
			}
		});
	}
}
