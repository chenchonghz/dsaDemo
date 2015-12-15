package com.szrjk.entity;

public class UpdataInfo
{
	private String version;
	private String downloadPath;
	private String description;
	private int versionCode;
	private int minVersionCode;

	public int getMinVersionCode()
	{
		return minVersionCode;
	}

	public void setMinVersionCode(int minVersionCode)
	{
		this.minVersionCode = minVersionCode;
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}

	public String getVersion()
	{
		return version;
	}

	public String getDownloadPath()
	{
		if (downloadPath == null) downloadPath = "";
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath)
	{
		this.downloadPath = downloadPath;
	}

	public String getDescription()
	{
		if (description == null || description.isEmpty()) description = "发现最新版本，请升级";
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}
}