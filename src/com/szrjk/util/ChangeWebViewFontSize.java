package com.szrjk.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebSettings;

public class ChangeWebViewFontSize {
	private  Context context;
	private  WebSettings settings;

	public  void setFontSize(Context context,WebSettings settings){
		this.context = context;
		this.settings = settings;
		//改字体用
		settings.setSupportZoom(true);
		getFontSize();
	}
	private void getFontSize() {
		SharedPreferences mySharedPreferences= context.getSharedPreferences("text_size",Context.MODE_PRIVATE);
		//用putString的方法保存数据
		String size = mySharedPreferences.getString("size","");
		if(size.equals("small")){
		}else if(size.equals("middle")){
			refreshUI(4);
		}else if(size.equals("large")){
			refreshUI(5);
		}
	}
	/** 
	 * 根据字体大小刷新UI 
	 */  
	private void refreshUI(int fontSize1) {  
		if (fontSize1 > 5) {  
			fontSize1 = 5;  
		}  
		if (fontSize1 < 1) {  
			fontSize1 = 1;  
		}  
		switch (fontSize1) {  
		case 1:  
			settings.setTextSize(WebSettings.TextSize.SMALLEST);  
			break;  
		case 2:  
			settings.setTextSize(WebSettings.TextSize.SMALLER);  
			break;  
		case 3:  
			settings.setTextSize(WebSettings.TextSize.NORMAL);  
			break;  
		case 4:  
			settings.setTextSize(WebSettings.TextSize.LARGER);  
			break;  
		case 5:  
			settings.setTextSize(WebSettings.TextSize.LARGEST);  
			break;  
		}  
	}
}