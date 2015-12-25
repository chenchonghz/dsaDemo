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

}
