package com.szrjk.util.base64;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encode(String str) throws  Exception{
		String sOut = "";
		try {

//			byte[] ss = str.getBytes("utf8");
//			String fuck = new String(ss,"utf8");
//			String context1 = new String(ss, "UTF-8");
//			String context2 = new String(ss, "GBK");
//			fuck = fuck.trim();
//
//			Log.e("context1",context1.getBytes().length+"");
//			Log.e("context2",context2.getBytes().length+"");
			Base64 bs = new Base64();
			sOut = new String(bs.encode(str.getBytes("utf8")));
		} catch (Exception e) {
			throw e;
		}
		return sOut;
	}

	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static String decode(String str) throws  Exception{
		String sOut = "";
		try {
			Base64 bs = new Base64();
			sOut = new String(bs.decode(str));
		} catch (Exception e) {
			throw e;
		}
		return sOut;
	}

	/*
	 * 测试程序
	 */
	public static void main(String[] args) throws Exception {
		String sMsg = "今天吃的阿斯顿节哈斯看见的哈萨克";
		String msg1 = encode(sMsg);
		String msg2 = decode(msg1);
		System.out.println(msg1);
		System.out.println(msg2);
	}
}
