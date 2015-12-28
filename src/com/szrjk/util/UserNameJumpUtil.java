package com.szrjk.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.szrjk.config.Constant;
import com.szrjk.dhome.OtherPeopleActivity;
import com.szrjk.dhome.R;
import com.szrjk.entity.IPullPostListCallback;
import com.szrjk.entity.PostInfo;
import com.szrjk.self.SystemUserActivity;
import com.szrjk.simplifyspan.SimplifySpanBuild;
import com.szrjk.simplifyspan.other.OnClickableSpanListener;
import com.szrjk.simplifyspan.other.SpecialGravity;
import com.szrjk.simplifyspan.unit.SpecialClickableUnit;
import com.szrjk.simplifyspan.unit.SpecialLabelUnit;
import com.szrjk.simplifyspan.unit.SpecialTextUnit;

public class UserNameJumpUtil {

	public static SimplifySpanBuild getContentText(final Context context,
			TextView tv_post_text, String content, String userName,
			final String userSeqId, String userLevel, final String userType,
			final PostInfo postInfo, final int position,
			final IPullPostListCallback iPullPostListCallback) {
		SimplifySpanBuild simplifySpanBuild = new SimplifySpanBuild(context,
				tv_post_text);
		simplifySpanBuild.appendSpecialUnit(new SpecialTextUnit(userName,
				context.getResources().getColor(R.color.link_text_color))
				.setSpecialTextBackgroundColor(context.getResources().getColor(R.color.base_bg))
				.setSpecialClickableUnit(new SpecialClickableUnit(
						new OnClickableSpanListener() {
							@Override
							public void onClick(TextView tv, String clickText) {
								if (userSeqId.equals(Constant.userInfo
										.getUserSeqId())) {
									iPullPostListCallback.skipToSelfFragment();
								} else if (!userSeqId.equals(Constant.userInfo
										.getUserSeqId())
										&& !(context instanceof OtherPeopleActivity)) {
									skipToOtherPeopleActivity(context,
											userSeqId);
								} else if (context instanceof SystemUserActivity) {
									String objId=((SystemUserActivity)context).getObjId();
									if (objId!=null) {
										if (userType.equals("1")&&!userSeqId.equals(objId)) {
											skipToSystemUserActivity(context, userSeqId);
										}
									}
								} else if (context instanceof OtherPeopleActivity) {
									String objId = ((OtherPeopleActivity) context)
											.getObjId();
									if (objId != null) {
										if (!userSeqId.equals(Constant.userInfo
												.getUserSeqId())
												&& !userSeqId.equals(objId)) {
											skipToOtherPeopleActivity(context,
													userSeqId);
										}
									}
								}
							}
						})));
		if (userLevel.equals("11")) {
			int px = DisplaySizeUtil.dip2px(context, 13);
			simplifySpanBuild
					.appendSpecialUnit(new SpecialTextUnit(" "))
					.appendSpecialUnit(
							new SpecialLabelUnit("火", context.getResources()
									.getColor(R.color.transparent), 13,
									BitmapFactory.decodeResource(
											context.getResources(),
											R.drawable.icon_yellow_v_24), px,
									px).setGravity(SpecialGravity.CENTER))
					.appendSpecialUnit(new SpecialTextUnit(" "));
		}
		if (content != null) {
			simplifySpanBuild.appendSpecialUnit(new SpecialTextUnit(":"
					+ content, context.getResources().getColor(
					R.color.font_titleanduname))
					.setSpecialClickableUnit(new SpecialClickableUnit(
							new OnClickableSpanListener() {

								@Override
								public void onClick(TextView tv,
										String clickText) {
								}
							})));
		}
		return simplifySpanBuild;
	}

	/**
	 * 跳转到系统主页
	 * 
	 * @param userSeqId
	 */
	protected static void skipToSystemUserActivity(Context context,
			String userSeqId) {
		Intent intent = new Intent(context, SystemUserActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}

	protected static void skipToOtherPeopleActivity(Context context,
			String userSeqId) {
		Intent intent = new Intent(context, OtherPeopleActivity.class);
		intent.putExtra(Constant.USER_SEQ_ID, userSeqId);
		context.startActivity(intent);
	}
}
