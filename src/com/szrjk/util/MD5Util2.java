package com.szrjk.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
* ���ܣ�֧����MD5ǩ����������ļ�������Ҫ�޸�
* �汾��3.3
* �޸����ڣ�2012-08-17
* ˵����
* ���´���ֻ��Ϊ�˷����̻����Զ��ṩ���������룬�̻����Ը����Լ���վ����Ҫ�����ռ����ĵ���д,����һ��Ҫʹ�øô��롣
* �ô������ѧϰ���о�֧�����ӿ�ʹ�ã�ֻ���ṩһ��
* */

public class MD5Util2 {

	private final static String  key = "abt80[Auj~2%6JV!jY~^c,_81qg>AWSh]$1jtpWwilK4L9OwDxPJn~Be.sLN8c3G";
	
	
	//byte�ֽ�ת����16���Ƶ��ַ���MD5Utils.hexString
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
        //ʹ��srcBytes����ժҪ
        md5.update(srcBytes);
        //��ɹ�ϣ���㣬�õ�result
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
		String msg = "12345�����»ƽ�����6789"+key;  
		byte[] resultBytes = eccrypt(msg);  
		System.out.println("�����ǣ�" + hexString(resultBytes));  
		System.out.println("�����ǣ�" + msg);  
		String ss = MD5Encode(msg);
		System.out.println("mi�ǣ�" + ss);
		
	}
	
//    /**
//     * ǩ���ַ���
//     * @param text ��Ҫǩ�����ַ���
//     * @param key ��Կ
//     * @param input_charset �����ʽ
//     * @return ǩ�����
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
//            throw new RuntimeException("MD5ǩ�������г��ִ���,ָ���ı��뼯����,��Ŀǰָ���ı��뼯��:" + charset);
//        }
//    }
	
//    /**
//     * ǩ���ַ���
//     * @param text ��Ҫǩ�����ַ���
//     * @param key ��Կ
//     * @param input_charset �����ʽ
//     * @return ǩ�����
//     */
//    public static String sign(String text, String key, String input_charset) {
//    	text = text + key;
//        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
//    }
    
    /**
     * ǩ���ַ���
     * @param text ��Ҫǩ�����ַ���
     * @param sign ǩ�����
     * @param key ��Կ
     * @param input_charset �����ʽ
     * @return ǩ�����
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