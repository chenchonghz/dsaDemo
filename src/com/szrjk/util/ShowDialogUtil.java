package com.szrjk.util;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.szrjk.dhome.R;

public class ShowDialogUtil {

	public static Dialog createDialog(Context context, String msg)
	{
		Dialog dialog = new Dialog(context, R.style.loading_dialog);
		dialog.setContentView(R.layout.loading_layout);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView textView = (TextView) dialog.findViewById(R.id.tv_msg);
		textView.setText(msg);
		return dialog;
	}
}
