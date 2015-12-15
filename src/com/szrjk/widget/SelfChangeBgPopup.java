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
/**
 * 我的界面按背景墙弹出的更换背景墙选项菜单
 * @author 郑斯铭
 * 包括了一个更换按钮和一个取消按钮
 *
 */
public class SelfChangeBgPopup extends PopupWindow 
{
	private TextView tv_change, tv_cancel;
	private View mMenuView;

	public SelfChangeBgPopup(Activity context, OnClickListener itemsOnClick)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.menu_self_bg_popwindow, null);
		tv_change = (TextView) mMenuView.findViewById(R.id.tv_change_bg);
		tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_change_cancel);
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
		tv_change.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
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

				int height = mMenuView.findViewById(R.id.lly_change_pop).getTop();
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
	public void setText(String text){
		tv_change.setText(text);
	}
}
