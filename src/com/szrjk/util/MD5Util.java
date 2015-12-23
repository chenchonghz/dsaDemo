package com.szrjk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{
	private static final String[] hexDigits =
	{
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
			"d", "e", "f"
	};

	public static String byteArrayToHexString(byte[] b)
	{
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	public static String byteArrayToOneHexString(byte[] b)
	{
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			resultSb.append(byteToOneHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToOneHexString(byte b)
	{
		int n = b;
		if (n < 0) n = 256 + n;
		int d2 = n % 16;
		return hexDigits[d2];
	}

	private static String byteToHexString(byte b)
	{
		int n = b;
		if (n < 0) n = 256 + n;

		int d1 = n / 16;

		int d2 = n % 16;

		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin)
			throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToHexString(md.digest(origin.getBytes()));
	}

	public static String MD5Encode16bit(String origin)
			throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToOneHexString(md.digest(origin.getBytes()));
	}

	public static String MD5Encode(byte[] origin)
			throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToHexString(md.digest(origin));
	}

	public static String MD5Encode16bit(byte[] origin)
			throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToOneHexString(md.digest(origin));
	}


	//byte字节转换成16进制的字符串MD5Utils.hexString
	public static String hexString(byte[] bytes){
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			int val = ((int) bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static byte[] eccrypt(String info) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] srcBytes = info.getBytes();
		//使用srcBytes更新摘要
		md5.update(srcBytes);
		//完成哈希计算，得到result
		byte[] resultBytes = md5.digest();
		return resultBytes;
	}

//	public static void main(String arg[]) throws NoSuchAlgorithmException {
//		byte[] resultBytes = MD5Util.eccrypt("123456789");
//		String htextecrypt = MD5Util.hexString(resultBytes);
//		System.out.print(htextecrypt);
//	}

//	public static void main(String args[]) throws NoSuchAlgorithmException{
//		String msg = "欢迎光临JerryVon的博客";
//		EncrypMD5 md5 = new EncrypMD5();
//		byte[] resultBytes = md5.eccrypt(msg);
//		System.out.println("密文是：" + MD5Utils.hexString(resultBytes));
//		System.out.println("明文是：" + msg);
//	}

}
