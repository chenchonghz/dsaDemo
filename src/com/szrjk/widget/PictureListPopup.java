package com.szrjk.widget;

import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szrjk.config.Constant;
import com.szrjk.dhome.R;
import com.szrjk.self.more.album.AlbumGalleryActivity;
import com.szrjk.self.more.album.AlbumsAdapter;
import com.szrjk.self.more.album.PhotoUpImageBucket;

public class PictureListPopup extends PopupWindow implements OnClickListener{
	private static boolean flag;
	private View view;
	private View v;
	private AlbumGalleryActivity context;
	private ListView listView;
	private TextView tv_cancel;
	private AlbumsAdapter adapter;
	private List<PhotoUpImageBucket> list;
	private Handler handler;
	@SuppressLint("NewApi")
	public PictureListPopup(AlbumGalleryActivity context,View v,List<PhotoUpImageBucket> list,Handler handler) {
		super();
		this.v = v;
		this.context = context;
		this.list = list;
		this.handler = handler;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.pop_picture_item, null);//加载布局
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		int h = (int) (Constant.screenHeight * 0.7);
		this.setHeight(h);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(color.white);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//		this.showAsDropDown(v, 0, 0, Gravity.CENTER_HORIZONTAL);
		view.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});

		setDissMiss();

		//配置适配器
		setAdapter(view);
	}
	private void setAdapter(View pop) {
		listView = (ListView) pop.findViewById(R.id.lv_albums);
		tv_cancel=(TextView)pop.findViewById(R.id.tv_cancel);
		adapter = new AlbumsAdapter(context);
		listView.setAdapter(adapter);
		if (!flag) {
			list.add(0, context.getHeadItem());
			flag = true;
		}
		adapter.setArrayList(list);
		adapter.notifyDataSetChanged();//更新视图

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

				if ("最近照片".equals(list.get(position).getBucketName())) {
					handler.sendEmptyMessage(0);
					dismiss();
				}else{

					PhotoUpImageBucket newlist = list.get(position);
					Message message = new Message();
					message.what = 1;
					message.obj = newlist ;
					handler.sendMessage(message);
					dismiss();
				}
			}
		});
	}
	//关闭
	private void setDissMiss() {
		this.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});
	}

	public void dissMissPopup(){
		
		this.dismiss();
	}
	@Override
	public void onClick(View arg0) {

	}
}
