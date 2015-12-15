package com.szrjk.entity;

public class ErrorInfo
{
	// 返回代号
	private String returnCode;
	// 描述信息
	private String errorMessage;

	public String getReturnCode()
	{
		return returnCode;
	}

	public void setReturnCode(String returnCode)
	{
		this.returnCode = returnCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
}
