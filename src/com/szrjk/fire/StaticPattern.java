package com.szrjk.fire;

/**
 * 静态匹配模式
 * 
 * @author Yoojia.Chen (yoojia.chen@gmail.com)
 * @version version 2015-05-20
 * @since 2.0
 */
public enum StaticPattern
{

	Required("此为必填项"), NotBlank("不能是任何空值"), Digits("只能输入数字"), DigitsLetters("不能是纯数字或者纯字母"),Email("邮箱地址错误"), Host(
			"域名地址错误"), URL("网络地址错误"), IPv4("IP地址错误"), Numeric("只能输入数值"), BankCard(
			"银行卡/信用卡号码错误"), IDCard("身份证号错误"), Mobile("手机号码错误"), VehicleNumber(
			"车牌号错误"), VIN("车架号错误"),MIX("只能是ABC"),ISCHINESE("只能是中文!"),ONLYCHINESEORMIX("只能是中文或字母!   ");

	private final String mDefMessage;

	private String mMessage;
	private int mMessageId = -1;

	private StaticPattern(String message)
	{
		mDefMessage = message;
	}

	/**
	 * 设置提示消息内容
	 * 
	 * @param message
	 *            消息内容
	 */
	public StaticPattern setMessage(String message)
	{
		mMessage = message;
		return this;
	}

	/**
	 * 设置提示消息内容的资源ID
	 * 
	 * @param msgId
	 *            资源ID
	 */
	public StaticPattern setMessage(int msgId)
	{
		mMessageId = msgId;
		return this;
	}

	String getMessage()
	{
		final String msg = mMessage == null ? mDefMessage : mMessage;
		mMessage = null;
		return msg;
	}

	int getMessageId()
	{
		final int msg = mMessageId <= 0 ? -1 : mMessageId;
		mMessageId = -1;
		return msg;
	}

	@Override
	public String toString()
	{
		return " {" + "name='" + name() + '\'' + ", message='" + mMessage
				+ '\'' + ", messageId=" + mMessageId + '}';
	}
}
