package com.szrjk.widget;

import com.szrjk.dhome.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private String DialogText;
	private String ConfrimText;
	private String CancelText;
	private ConfrimButtonListener confrimButtonListener;
	private Context mContext;
	
	//dialogText：dialog正文文字 ；confirmText：确认按钮文字，可null；cancelText:取消按钮文字，可null
	public CustomDialog(Context context, String dialogText, String confrimText,
			String cancelText, ConfrimButtonListener confrimButtonListener) {
		super(context,R.style.Theme_Transparent);
		this.DialogText = dialogText;
		this.ConfrimText = confrimText;
		this.CancelText = cancelText;
		this.mContext = context;
		this.confrimButtonListener = confrimButtonListener;
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
		Button btn_confrim = (Button) findViewById(R.id.btn_enter);
		TextView tv_dialog = (TextView) findViewById(R.id.tv_dialog);
		tv_dialog.setText(DialogText);
		if (ConfrimText!=null) {
			btn_confrim.setText(ConfrimText);
		}else{
			btn_confrim.setText("确认");
		}
		if (CancelText!=null) {
			btn_cancel.setText(CancelText);
		}else{
			btn_cancel.setText("取消");
		}
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				CustomDialog.this.dismiss();
			}
		});
		btn_confrim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				confrimButtonListener.confrim();
				CustomDialog.this.dismiss();
			}
		});
	}

	public interface ConfrimButtonListener{
		//在此方法写确认按钮的onclick方法
		public void confrim();
	}


}
