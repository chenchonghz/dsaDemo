package com.szrjk.util;

import java.util.List;

import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPullPostListCallback;
import com.szrjk.entity.InitSrcPostInterface;
import com.szrjk.entity.PostAbstractList;
import com.szrjk.entity.PostInfo;
import com.szrjk.entity.UserCard;
import com.szrjk.self.SystemUserActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

public class InitTransmitPostUtil {


	/**
	 * 业务util
	 * @param context
	 * @param ssBuilder new 一个就行,等于一个空字符串
	 * @param postAbstractLists 列表
	 * @param initSrcPostInterface
	 * @param iPullPostListCallback
	 * @return
	 */
	public static SpannableStringBuilder initTransmitPost(Context context,SpannableStringBuilder ssBuilder,
			List<PostAbstractList> postAbstractLists,InitSrcPostInterface initSrcPostInterface,IPullPostListCallback iPullPostListCallback){
		boolean isTopTransmit = false;
		if(postAbstractLists != null && !postAbstractLists.isEmpty()){
			for (int i = 0; i < postAbstractLists.size(); i++) {
				if(postAbstractLists.get(i).getPostLevel().equals("0")){
					UserCard userCard = postAbstractLists.get(i).getUserCard();
					PostInfo postInfo = postAbstractLists.get(i).getPostAbstract();
					String isDelete = postAbstractLists.get(i).getIsDelete();
					if(userCard != null && postInfo != null&&initSrcPostInterface!=null){
						initSrcPostInterface.initSrcPost(context, userCard, postInfo,isDelete);
					}
				}else if(postAbstractLists.get(i).getPostLevel().equals(String.valueOf(postAbstractLists.size()-1))){
					isTopTransmit = true;
					String content = postAbstractLists.get(i).getPostAbstract().getContent();
					if(content != null){						
							SpannableString t_content = getTrasmitContent(context,postAbstractLists.get(i).getUserCard(),postAbstractLists.get(i).getPostAbstract(),isTopTransmit,iPullPostListCallback);
							if(t_content != null){		
								ssBuilder.append(t_content);
							}else{
								ssBuilder.append("");
							}
					}else{
						content = "";
						ssBuilder.append("");
					}
				}else{
					isTopTransmit = false;
					SpannableString t_content = getTrasmitContent(context,postAbstractLists.get(i).getUserCard(),postAbstractLists.get(i).getPostAbstract(),isTopTransmit,iPullPostListCallback);
					if(t_content != null){	
						ssBuilder.append(t_content);
					}else{
						ssBuilder.append("");
					}
				}
			}
		}
		return ssBuilder;		
	}
	
	public static SpannableStringBuilder showTransmitPost(Context context,SpannableStringBuilder ssBuilder,
			List<PostAbstractList> postAbstractLists){
		if(postAbstractLists != null && !postAbstractLists.isEmpty()){
			for (int i = 0; i < postAbstractLists.size(); i++) {
				if(!postAbstractLists.get(i).getPostLevel().equals("0")){
					UserCard userCard = postAbstractLists.get(i).getUserCard();
					PostInfo postInfo = postAbstractLists.get(i).getPostAbstract();
					SpannableString t_content = getTransmitContent2(context, userCard, postInfo);
					if(t_content != null){		
						ssBuilder.append(t_content);
					}else{
						ssBuilder.append("");
					}
				}
			}
		}
		return ssBuilder;
	}

	private static SpannableString getTransmitContent2(final Context context, final UserCard userCard,
			PostInfo postAbstract){
		StringBuffer sb_content = new StringBuffer("");
		int start = 0;
		int end = 0;
		int icon_start = 0;
		int icon_end = 0;
		SpannableString spanStr = null;
		String name = userCard.getUserName();
		sb_content.append("//"+name);
		start = 2;
		end = sb_content.length();		
		Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24);
		ImageSpan imgSpan = new ImageSpan(context, b, DynamicDrawableSpan.ALIGN_BASELINE);
		icon_start = sb_content.length();
		if(userCard.getUserLevel().equals("11")){
			sb_content.append(" icon");
			icon_start += 1;
			icon_end = sb_content.length();
		}
		if(postAbstract.getContent() != null){
				sb_content.append(":"+postAbstract.getContent());
		}
		spanStr = new SpannableString(sb_content);
		spanStr.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
				ds.setColor(context.getResources().getColor(R.color.link_text_color));
			}
		}, start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
//		spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.link_text_color)), start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(userCard.getUserLevel().equals("11")){	
			spanStr.setSpan(imgSpan, icon_start, icon_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spanStr;
	}
	private static SpannableString getTrasmitContent(final Context context, final UserCard userCard,
			PostInfo postAbstract, boolean isTopTransmit, final IPullPostListCallback iPullPostListCallback) {
		// TODO Auto-generated method stub
		StringBuffer sb_content = new StringBuffer("");
		int start = 0;
		int end = 0;
		int icon_start = 0;
		int icon_end = 0;
		SpannableString spanStr = null;
		if(isTopTransmit == false){
			String name = userCard.getUserName();
			sb_content.append("//"+name);
			start = 2;
			end = sb_content.length();		
			Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_yellow_v_24);
			ImageSpan imgSpan = new ImageSpan(context, b, DynamicDrawableSpan.ALIGN_BASELINE);
			icon_start = sb_content.length();
			if(userCard.getUserLevel().equals("11")){
				sb_content.append(" icon");
				icon_start += 1;
				icon_end = sb_content.length();
			}
			if(postAbstract.getContent() != null){
					sb_content.append(":"+postAbstract.getContent());
			}
			spanStr = new SpannableString(sb_content);
			spanStr.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if(userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSelfFragment(iPullPostListCallback);
					}else if(userCard.getUserType().equals("1")&&!userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){
						skipToSystemUserActivity(context,userCard.getUserSeqId());
					}else{
						if(!userCard.getUserSeqId().equals(Constant.userInfo.getUserSeqId())){		
							skipToOtherPeopleActivity(context,userCard.getUserSeqId());
						}
					}
				}
				@Override
				public void updateDrawState(TextPaint ds) {
					super.updateDrawState(ds);
					ds.setUnderlineText(false);
					ds.setColor(context.getResources().getColor(R.color.link_text_color));
				}
			}, start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);		
//			spanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_titleanduname)), icon_end,sb_content.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);	
//			spanStr.setSpan(new BackgroundColorSpan(context.getResources().get), start, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			if(userCard.getUserLevel().equals("11")){	
				spanStr.setSpan(imgSpan, icon_start, icon_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return spanStr;
		}else{
			if(postAbstract.getContent() != null){		
					sb_content.append(postAbstract.getContent());
					spanStr = new SpannableString(sb_content);	
			}	
			return spanStr;
		}
	}

	protected static void skipToOtherPeopleActivity(Context context, String userSeqId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, OtherPeopleActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}

	protected static void skipToSystemUserActivity(Context context, String userSeqId) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, SystemUserActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}

	protected static void skipToSelfFragment(IPullPostListCallback iPullPostListCallback) {
		// TODO Auto-generated method stub
		iPullPostListCallback.skipToSelfFragment();
	}

}

