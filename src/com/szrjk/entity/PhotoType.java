package com.szrjk.entity;

public enum PhotoType
{
	Face(1),Feed(2);

	private int value = 0;

	private PhotoType(int value)
	{
		this.value = value;
	}

	public static PhotoType valueOf(int value)
	{
		switch (value)
		{
			case 1:
				return Face;
			case 2:
				return Feed;
			default:
				return null;
		}
	}

	public int value()
	{
		return this.value;
	}
}
