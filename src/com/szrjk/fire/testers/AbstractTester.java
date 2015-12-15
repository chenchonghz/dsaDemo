package com.szrjk.fire.testers;

import java.util.regex.Pattern;

/**
 * 校验器
 */
public abstract class AbstractTester
{
	/**
	 * 发生异常时产生的消息
	 */
	private String mExceptionMessage;

	public String getExceptionMessage()
	{
		return mExceptionMessage;
	}

	/**
	 * 校验输入内容。如果校验通过返回True，否则返回False。
	 * 
	 * @param content
	 *            校验内容
	 * @return 如果校验通过返回True，否则返回False。
	 */
	public final boolean performTest(String content)
	{
		try
		{
			mExceptionMessage = null;
			return test(content);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			mExceptionMessage = e.getMessage();
			return false;
		}
	}

	/**
	 * 获取Tester的名称
	 * 
	 * @return Tester名称
	 */
	public final String getName()
	{
		return this.getClass().getSimpleName();
	}

	/**
	 * 校验器实现类实现真正的校验方法
	 * 
	 * @param content
	 *            内容
	 * @return 是否校验通过
	 */
	protected abstract boolean test(String content);

	/**
	 * 校验内容是否匹配指定正则表达式
	 * 
	 * @param regex
	 *            正则表达式
	 * @param inputValue
	 *            内容
	 * @return 是否匹配
	 */
	protected static boolean testRegex(String regex, CharSequence inputValue)
	{
		return Pattern.compile(regex).matcher(inputValue).matches();
	}
}
