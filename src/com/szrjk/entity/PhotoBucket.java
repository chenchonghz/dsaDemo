package com.szrjk.entity;

public class PhotoBucket
{
	//图片bucket标签
	private String bucket;
	//图片key
	private String key;
	//尺寸
	private String size;

	public String getBucket()
	{
		return bucket;
	}

	public void setBucket(String bucket)
	{
		this.bucket = bucket;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}
}
