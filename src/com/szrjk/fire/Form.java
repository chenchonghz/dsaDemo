package com.szrjk.fire;

import android.view.View;
import android.widget.TextView;

/**
 * 表格查找器
 */
public class Form
{

	private final View form;

	public Form(View form)
	{
		this.form = form;
	}

	/**
	 * 查找表格From的子View
	 * 
	 * @param viewId
	 *            View ID
	 * @param <T>
	 *            TextView的子类型
	 * @return TextView的子类型
	 */
	public <T extends TextView> T byId(int viewId)
	{
		return (T) form.findViewById(viewId);
	}
}
