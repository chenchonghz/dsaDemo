package com.szrjk.widget;

import com.szrjk.dhome.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class AddPopup extends PopupWindow {

	private LinearLayout ll_creatcoterie, ll_caseshare, ll_puzzlehelp;

	private View mMenuView;

	public AddPopup(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_add, null);
		ll_creatcoterie = (LinearLayout) mMenuView
				.findViewById(R.id.ll_creatcoterie);
		ll_caseshare = (LinearLayout) mMenuView.findViewById(R.id.ll_caseshare);
		ll_puzzlehelp = (LinearLayout) mMenuView
				.findViewById(R.id.ll_puzzlehelp);

		// 设置按钮监听
		ll_creatcoterie.setOnClickListener(itemsOnClick);
		ll_caseshare.setOnClickListener(itemsOnClick);
		ll_puzzlehelp.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.setWidth(dm.widthPixels*2/5);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// // 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int top = mMenuView.getTop();
				int bottom = mMenuView.getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < top || y > bottom) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
}
