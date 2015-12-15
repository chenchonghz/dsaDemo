package com.szrjk.fire;

public class Result
{

	public final boolean passed;
	public final String message;
	final String value;

	public Result(boolean passed, String message, String value)
	{
		this.passed = passed;
		this.message = message;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "{ " + "passed: " + passed + ",\t" + "value: "
				+ String.valueOf(value) + ", " + "message: " + message + "}";
	}

	/**
	 * 校验通过
	 * 
	 * @param value
	 *            数值
	 * @return 校验通过的结果对象
	 */
	public static Result passed(String value)
	{
		return new Result(true, null, value);
	}

	/**
	 * 校验失败
	 * 
	 * @param message
	 *            失败提示消息
	 * @param value
	 *            数值
	 * @return 校验失败结果对象
	 */
	public static Result reject(String message, String value)
	{
		return new Result(false, message, value);
	}
}