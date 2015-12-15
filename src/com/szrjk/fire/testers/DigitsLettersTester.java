package com.szrjk.fire.testers;

import android.text.TextUtils;


/**
 * 密码
 * 纯数字或者纯字母*/
public class DigitsLettersTester extends AbstractTester{

	static final String P_REGEX = "^([a-zA-Z])*$";
	@Override
	public boolean test(String content)
	{
		return !testRegex(P_REGEX, content)&&!TextUtils.isDigitsOnly(content);
	}
}
