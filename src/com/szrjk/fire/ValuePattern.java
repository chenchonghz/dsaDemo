package com.szrjk.fire;

/**
 * 数值匹配模式。作为配置项传递，其数据由Config来保存
 */
public enum ValuePattern
{

	Required("此为必填项目"),

	MaxLength("长度不能超过{0}"), MinLength("长度不能小于{0}"), RangeLength(
			"长度必须在[{0},{1}]之间"),

	MaxValue("数值不能超过{0}"), MinValue("数值不能小于{0}"), RangeValue("数值必须在[{0},{1}]之间"),

	EqualsTo("必须输入相同内容"), NotEqualsTo("必须输入不相同内容");

	private final String mDefMessage;
	private String mMessage;
	private int mMessageId = -1;

	private LazyLoader mLazyLoader;
	private ValueType mValueType;
	private String mMinValue;
	private String mMaxValue;
	private String mValue;

	private ValuePattern(String message)
	{
		mDefMessage = message;
	}

	/**
	 * 设置懒加载接口
	 * 
	 * @param lazyLoader
	 *            懒加载接口
	 * @return ValuePattern实例
	 */
	public ValuePattern lazy(LazyLoader lazyLoader)
	{
		mLazyLoader = lazyLoader;
		return this;
	}

	/**
	 * 设置第一个参数值
	 * 
	 * @param first
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setFirstValue(double first)
	{
		enforceFloatValueType();
		syncValue(first);
		return this;
	}

	/**
	 * 设置第一个参数值
	 * 
	 * @param first
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setFirstValue(long first)
	{
		enforceIntValueType();
		syncValue(first);
		return this;
	}

	/**
	 * 设置第二个参数值
	 * 
	 * @param second
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setSecondValue(long second)
	{
		enforceIntValueType();
		mMaxValue = String.valueOf(second);
		return this;
	}

	/**
	 * 设置第二个参数值
	 * 
	 * @param second
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setSecondValue(double second)
	{
		enforceFloatValueType();
		mMaxValue = String.valueOf(second);
		return this;
	}

	/**
	 * 设置第一个参数值
	 * 
	 * @param value
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setValue(String value)
	{
		syncValue(value);
		mValueType = ValueType.String;
		return this;
	}

	/**
	 * 设置第一个参数值
	 * 
	 * @param value
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setValue(long value)
	{
		syncValue(value);
		mValueType = ValueType.Int;
		return this;
	}

	/**
	 * 设置第一个参数值
	 * 
	 * @param value
	 *            数值
	 * @return ValuePattern
	 */
	public ValuePattern setValue(double value)
	{
		syncValue(value);
		mValueType = ValueType.Float;
		return this;
	}

	/**
	 * 设置提示消息内容
	 * 
	 * @param message
	 *            消息内容
	 */
	public ValuePattern setMessage(String message)
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
	public ValuePattern setMessage(int msgId)
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

	LazyLoader getLazyLoader()
	{
		final LazyLoader loader = mLazyLoader;
		mLazyLoader = null;
		return loader;
	}

	ValueType getValueType()
	{
		ValueType type = mValueType;
		mValueType = null;
		return type;
	}

	String getMinValue()
	{
		final String value = mMinValue;
		mMinValue = null;
		return value;
	}

	String getMaxValue()
	{
		final String value = mMaxValue;
		mMaxValue = null;
		return value;
	}

	String getValue()
	{
		final String value = mValue;
		mValue = null;
		return value;
	}

	private void syncValue(Object value)
	{
		mValue = String.valueOf(value);
		mMinValue = mValue;
	}

	private void enforceIntValueType()
	{
		if (mValueType == null)
		{
			mValueType = ValueType.Int;
		}
		else
		{
			if (!ValueType.Int.equals(mValueType)) throw new IllegalArgumentException(
					"设置的数值类型必须同为整数");
		}
	}

	private void enforceFloatValueType()
	{
		if (mValueType == null)
		{
			mValueType = ValueType.Float;
		}
		else
		{
			if (!ValueType.Float.equals(mValueType)) throw new IllegalArgumentException(
					"设置的数值类型必须同为浮点数");
		}
	}

	@Override
	public String toString()
	{
		return "{" + "name='" + name() + '\'' + ", messageId=" + mMessageId
				+ ", message='" + mMessage + '\'' + ", lazyLoader="
				+ mLazyLoader + ", valueType=" + mValueType + ", minValue='"
				+ mMinValue + '\'' + ", maxValue='" + mMaxValue + '\''
				+ ", value='" + mValue + '\'' + '}';
	}
}
