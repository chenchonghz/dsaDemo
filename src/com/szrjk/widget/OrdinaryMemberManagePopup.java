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

public class OrdinaryMemberManagePopup extends PopupWindow
{
	private TextView tv_invite, tv_quit, tv_cancel;
	private View mMenuView;

	public OrdinaryMemberManagePopup(Activity context, OnClickListener itemsOnClick)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_ordinary_member_manage, null);
		tv_invite = (TextView) mMenuView.findViewById(R.id.tv_invite);
		//tv_quit = (TextView) mMenuView.findViewById(R.id.tv_quit);
		tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
		// 取消按钮
		tv_cancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		tv_invite.setOnClickListener(itemsOnClick);
		//tv_quit.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(android.view.ViewGroup.LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
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
			@Override
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
