package com.szrjk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPopupItemCallback;
import com.szrjk.entity.PopupItem;

public class ItemPopupLayout extends RelativeLayout
{

	private LinearLayout item_pop_ll;

	private View pView;

	private TextView tv_pop_content ;

	private IPopupItemCallback iPopupItemCallback;

	private PopupWindow popupWindow;
	public ItemPopupLayout(Context context,PopupWindow popupWindow)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pView = inflater.inflate(R.layout.item_popup_list, this);
		tv_pop_content = (TextView) pView.findViewById(R.id.tv_pop_content);
		item_pop_ll = (LinearLayout) pView.findViewById(R.id.item_pop_ll);
		this.popupWindow = popupWindow;
	}

	public ItemPopupLayout(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
//		LayoutInflater inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		pView = inflater.inflate(R.layout.item_popup_list, this);
//		tv_content = (TextView) pView.findViewById(R.id.tv_pop_content);
//		item_pop_ll = (LinearLayout) pView.findViewById(R.id.item_pop_ll);

	}
	//这里保存之前的item高度
	static int  h ;
	public void setPopupItem(PopupItem popupItem){
//		if ("取消".equals(popupItem.getItemname())) {
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//			lp.setMargins(0, 8, 20, 20);
//			tv_pop_content.setLayoutParams(lp);
//		}
		//如果不是取消，设置字体颜色、获得高度
		//如果是取消，设置高度，margintop = 8 
		if (!"取消".equals(popupItem.getItemname())) {
			
			android.view.ViewGroup.LayoutParams p = tv_pop_content.getLayoutParams();
			System.out.println("h:"+p.height);
			h = p.height;
			tv_pop_content.setTextColor(popupItem.getColor());
		}else{
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, h);
			lp.setMargins(0, 8, 0, 0);
			tv_pop_content.setLayoutParams(lp);
		}
		tv_pop_content.setText(popupItem.getItemname());
		iPopupItemCallback = popupItem.getiPopupItemCallback();
		tv_pop_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				iPopupItemCallback.itemClickFunc(popupWindow);
			}
		});
	}
}
