package com.szrjk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5生成代码处理 
 * //http://blog.csdn.net/jerryvon/article/details/22602811
 * 
* */

public class MessafeDigest {

//	private final static String  key = "abt80[Auj~2%6JV!jY~^c,_81qg>AWSh]$1jtpWwilK4L9OwDxPJn~Be.sLN8c3G";
	
	
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
    
    /**
     * 先BASE64转码-->再MD5转码
     * @param info
     * @return
     * @throws Exception
     */
    public static String Encode(String info)throws Exception{
//        info = new String(info.toString().getBytes("UTF-8"));
//    	String text = info + key;
        byte[] resultBytes = eccrypt(info);
        String sRtnValue = hexString(resultBytes);
        return  sRtnValue;
    }
	
    
//	public static void main(String args[]) throws Exception{
//		String msg = "{\"BusiParams\":{\"content\":\"的\",\"deviceType\":\"1\",\"userSeqId\":\"1000000206\"},\"ServiceName\":\"sendNormalPost\"}";
//		byte[] resultBytes = eccrypt(msg);
//		System.out.println("密文是：" + hexString(resultBytes));
//		System.out.println("明文是：" + msg);
//		String ss = Encode(msg);
//		System.out.println("mi是：" + ss);
//
//	}
	
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