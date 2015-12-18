package com.szrjk.util;


import com.szrjk.dhome.LoginHelper;
import com.szrjk.dhome.MainActivity;
import com.szrjk.dhome.Register1Activity;
import com.szrjk.widget.CustomDialog;
import com.szrjk.widget.CustomDialog.ConfrimButtonListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DialogUtil {
	//弹框显示 传入context与text  text可为空
//	public static void showGuestDialog(final Context mContext, String text) {
//		LayoutInflater mInflater = LayoutInflater.from(mContext);
//		AlertDialog.Builder builder=new AlertDialog.Builder(mContext,R.style.Theme_Transparent);
//		View CustomView=mInflater.inflate(R.layout.dialog_layout, null);
//		final AlertDialog dialog = builder.setView(CustomView).show();
//		dialog.setCanceledOnTouchOutside(true);
//		Button btn_cancel = (Button) CustomView.findViewById(R.id.btn_cancel);
//		Button btn_enter = (Button) CustomView.findViewById(R.id.btn_enter);
//		TextView tv_dialog = (TextView) CustomView.findViewById(R.id.tv_dialog);
//		if (text==null) {
//			tv_dialog.setText("通过注册填写基本信息后，您就可以使用发帖、搜索、学习医学知识等所有功能，还能结交更多大牛医友。");
//		}else{
//			tv_dialog.setText(text);
//		}
//		btn_enter.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
////				ToastUtils.showMessage(mContext, "确定");
//				LoginHelper.Logout(mContext);
//				Intent intent = new Intent(mContext, Register1Activity.class);
//				intent.putExtra("Visitor", true);
//				mContext.startActivity(intent);
//				Activity activity = (Activity) mContext;
//				activity.finish();
//				MainActivity.finishMain();
//			}
//		});
//		btn_cancel.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
////				ToastUtils.showMessage(mContext, "取消");
//				dialog.dismiss();
//			}
//		});
//	}
	public static void showGuestDialog(final Context mContext
			, String dialogtext){
		dialogtext = "通过注册填写基本信息后，您就可以使用发帖、搜索、学习医学知识等所有功能，还能结交更多大牛医友。";
		String Confrimtext = "立即注册";
		String Canceltext = "取消";
		CustomDialog dialog = new CustomDialog(mContext, dialogtext, Confrimtext, Canceltext,
				new ConfrimButtonListener() {
					@Override
					public void confrim() {
						LoginHelper.Logout(mContext);
						Intent intent = new Intent(mContext, Register1Activity.class);
						intent.putExtra("Visitor", true);
						mContext.startActivity(intent);
						Activity activity = (Activity) mContext;
						activity.finish();
						MainActivity.finishMain();
					}
				});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}
	
}
