package com.szrjk.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;

import java.util.List;

public class ListPopup extends LinearLayout
{
	//	private TextView tv_save, tv_nsave, tv_cancel;
	private LinearLayout pop_layout;
	private View myview;

	private PopupWindow sendWindow;

	/**
	 *
	 * @param context
	 * @param pilist 文字 和 click动作
	 * @param lly_post 外层的 view,，用于 showAtLocation
	 */
	public ListPopup(Activity context, List<PopupItem> pilist,View lly_post)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		myview = inflater.inflate(R.layout.popup_list, null);
		pop_layout = (LinearLayout) myview.findViewById(R.id.pop_list_ll);

		//加入取消
		PopupItem canclePopupItem = new PopupItem();
		canclePopupItem.setItemname("取消");
		canclePopupItem.setiPopupItemCallback(new IPopupItemCallback() {
			@Override
			public void itemClickFunc(PopupWindow sendWindow) {
				sendWindow.dismiss();
			}
		});
		pilist.add(canclePopupItem);


		//初始化window
		sendWindow = new PopupWindow(context);

		//显示数据
		for(int i=0;i<pilist.size();i++){
			PopupItem popupItem = pilist.get(i);
			ItemPopupLayout itemPopupLayout = new ItemPopupLayout(context,sendWindow);
			itemPopupLayout.setPopupItem(popupItem);
			pop_layout.addView(itemPopupLayout);
		}






		//		tv_nsave = (TextView) mMenuView.findViewById(R.id.tv_nsave);
		//		tv_cancel = (TextView) mMenuView.findViewById(R.id.tv_cancel);
		//		// 取消按钮
		//		tv_cancel.setOnClickListener(new OnClickListener()
		//		{
		//
		//			public void onClick(View v)
		//			{
		//				// 销毁弹出框
		//				dismiss();
		//			}
		//		});
		//		// 设置按钮监听
		//		tv_save.setOnClickListener(itemsOnClick);
		//		tv_nsave.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		sendWindow.setContentView(myview);
		// 设置SelectPicPopupWindow弹出窗体的宽
		sendWindow.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		sendWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		sendWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		sendWindow.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		sendWindow.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		myview.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = pop_layout.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						sendWindow.dismiss();
					}
				}
				return true;
			}
		});
		// 显示窗口
		sendWindow.showAtLocation(lly_post, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}
}
