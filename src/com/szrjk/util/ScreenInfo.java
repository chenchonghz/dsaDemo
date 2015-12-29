package com.szrjk.util;

import android.util.DisplayMetrics;
import android.util.Log;

import com.szrjk.config.Constant;
import com.szrjk.dhome.BaseActivity;

public class ScreenInfo {

	public static int getScreenWidth(BaseActivity context){
		DisplayMetrics  dm = new DisplayMetrics();    
//	    //取得窗口属性    
		context. getWindowManager().getDefaultDisplay().getMetrics(dm);    
//	    //窗口的宽度    
	    int screenWidth = dm.widthPixels;
	    int screenHeight = dm.heightPixels;
//	    Log.i("changeportrai", screenWidth+"");
	    Constant.screenWidth = screenWidth;
	    Constant.screenHeight = screenHeight;
	    Log.i("screenWidth", ""+screenWidth);
	    Log.i("ccreenHeight", ""+screenHeight);
	    return screenWidth;
	}
}
