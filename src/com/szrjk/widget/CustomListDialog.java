package com.szrjk.widget;

import java.util.List;

import com.szrjk.dhome.R;
import com.szrjk.entity.DialogItem;
import com.szrjk.entity.PopupItem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListDialog extends Dialog{
	private List<DialogItem> list;
	private Context mContext;
//	private listItemListener itemListener;
//	public interface listItemListener{
//		//在此方法写list的Itemonclick方法
//		public  void ItemClick(int position);
//	}
	public CustomListDialog(Context context, List<DialogItem> list) {
		super(context,R.style.Theme_Transparent);
		this.list = list;
		this.mContext = context;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listdialog_layout);
		ListView lv_dialog = (ListView) findViewById(R.id.lv_dialog);
		listdialogAdapter adpater = new listdialogAdapter(mContext, list);
		lv_dialog.setAdapter(adpater);
		lv_dialog.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				list.get(arg2).getDialogItemCallback().DialogitemClick();
				CustomListDialog.this.dismiss();
			}
		});
		
	}
	public class listdialogAdapter extends BaseAdapter{
		private Context mContext;
		private List<DialogItem> list;
		private LayoutInflater mInflater;
		
		
		public listdialogAdapter(Context mContext, List<DialogItem> list) {
			super();
			this.mContext = mContext;
			this.list = list;
			this.mInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position,View convertview, ViewGroup parent) {
			viewHolder viewHolder = null;
			if (convertview ==null) {
				viewHolder = new viewHolder();
				convertview = mInflater.inflate(R.layout.item_listdialog,null);
				viewHolder.tv = (TextView) convertview.findViewById(R.id.tv_listdialog);
				convertview.setTag(viewHolder);
			}else{
				viewHolder = (viewHolder) convertview.getTag();
			}
			viewHolder.tv.setText(list.get(position).getItemText());
			viewHolder.tv.setTextColor(list.get(position).getColor());
			
			return convertview;
		}
		private class viewHolder{
			private TextView tv;
		}
		
	}
	
	
}

