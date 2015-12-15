package com.szrjk.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

public class KeyWordUtils {
	public static void pullKeywordTop(final Activity activity,final int lyRootID,final int vID,final int svID){
		ViewGroup ly = (ViewGroup) activity.findViewById(lyRootID);
		final int defaultHeight = ((WindowManager)activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		final int mKeyHeight = defaultHeight/4;
		ly.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				int height = oldBottom-bottom;
				ScrollView sv = (ScrollView)activity.findViewById(svID);
				if(height>mKeyHeight) {
					final int lybottom = bottom;
					sv.post(new Runnable() {
						@Override
						public void run() {
							ScrollView runSv = (ScrollView)activity.findViewById(svID);
							View v = (View)activity.findViewById(vID);
							int[] loca = new int[2];
							v.getLocationOnScreen(loca);
							Rect frame = new Rect();
							activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
							int statusBarHeight = frame.top;
							int scrollHeight = loca[1] +  v.getHeight() - lybottom - statusBarHeight;
							if(scrollHeight>0){
								runSv.scrollBy(0, scrollHeight);
							}

						}
					});
				}else if(-height>mKeyHeight){
					sv.scrollTo(0,0);
				}
			}
		});



	}
}
