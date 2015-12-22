package com.szrjk.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
* 功能：支付宝MD5签名处理核心文件，不需要修改
* 版本：3.3
* 修改日期：2012-08-17
* 说明：
* 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
* 该代码仅供学习和研究支付宝接口使用，只是提供一个
* */

public class MD5Util2 {

	private final static String  key = "abt80[Auj~2%6JV!jY~^c,_81qg>AWSh]$1jtpWwilK4L9OwDxPJn~Be.sLN8c3G";
	
	
	//byte字节转换成16进制的字符串MD5Utils.hexString
	private static String hexString(byte[] bytes){
        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            int val = ((int) bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
    private static byte[] eccrypt(String info) throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] srcBytes = info.getBytes();
        //使用srcBytes更新摘要
        md5.update(srcBytes);
        //完成哈希计算，得到result
        byte[] resultBytes = md5.digest();
        return resultBytes;
    }
    
    public static String MD5Encode(String info)throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	String text = info + key;
		byte[] resultBytes = eccrypt(text);
		String sRtnValue = hexString(resultBytes);
		return  sRtnValue;
    }
	
    //http://blog.csdn.net/jerryvon/article/details/22602811
	public static void main(String args[]) throws Exception{
		String msg = "12345阿萨德黄金阿萨德6789"+key;  
		byte[] resultBytes = eccrypt(msg);  
		System.out.println("密文是：" + hexString(resultBytes));  
		System.out.println("明文是：" + msg);  
		String ss = MD5Encode(msg);
		System.out.println("mi是：" + ss);
		
	}
	
//    /**
//     * 签名字符串
//     * @param text 需要签名的字符串
//     * @param key 密钥
//     * @param input_charset 编码格式
//     * @return 签名结果
//     */
//    public static String sign(String text) {
//    	text = text + key;
//        return DigestUtils.md5Hex(getContentBytes(text, "UTF-8"));
//    }
//    
//    /**
//     * @param content
//     * @param charset
//     * @return
//     * @throws SignatureException
//     * @throws UnsupportedEncodingException 
//     */
//    private static byte[] getContentBytes(String content, String charset) {
//        if (charset == null || "".equals(charset)) {
//            return content.getBytes();
//        }
//        try {
//            return content.getBytes(charset);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
//        }
//    }
	
//    /**
//     * 签名字符串
//     * @param text 需要签名的字符串
//     * @param key 密钥
//     * @param input_charset 编码格式
//     * @return 签名结果
//     */
//    public static String sign(String text, String key, String input_charset) {
//    	text = text + key;
//        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
//    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
//    public static boolean verify(String text, String sign, String key, String input_charset) {
//    	String mysign  = sign(text, key, input_charset);
//    	if(mysign.equals(sign)) {
//    		return true;
//    	}
//    	else {
//    		return false;
//    	}
//    }
    



}