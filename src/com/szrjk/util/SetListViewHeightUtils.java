package com.szrjk.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SetListViewHeightUtils {

	public static void setListViewHeight(ListView listView) {
		// 获取ListView对应的Adapter

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			try {
				listItem.measure(0, 0); // 计算子项View 的宽高
			} catch (Exception e) {
				Log.e("error",e.getMessage(),e);

			}
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+150;
		listView.setLayoutParams(params);
	}
}
