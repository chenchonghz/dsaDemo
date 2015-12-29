package com.szrjk.config;

import com.szrjk.entity.UserInfo;

public class ConstantUser
{

	public static UserInfo getUserInfo(){
		return Constant.userInfo;
	}
	public static final int MyNormalPost = 7;
	public static final int MyCaseShare = 8;
	public static final int MyProblemHelp = 9;

	/**用户等级*/
	public static final String USERLEVEL="USERLEVEL";
	
	//消息类型
	public static final int TYPE_NEWS = 1;
	public static final int TYPE_CASESHARE = 2;
	public static final int TYPE_PROBLEMHELP = 3;
	public static final int TYPE_FRI_REQUEST = 4;
	public static final int TYPE_SYS_REMIND = 5;
	public static final int TYPE_CIRCLE_REQUEST = 6;
	public static final int TYPE_CIRCLE_INVITE = 7;
	public static final int TYPE_CIRCLE_POST = 8;
	public static final int TYPE_POST_COMMENT = 9;
	public static final int TYPE_POST_TRANSMIT = 10;
	public static final int TYPE_POST_LIKE = 11;
	public static final int TYPE_CHAT = 12;

}
