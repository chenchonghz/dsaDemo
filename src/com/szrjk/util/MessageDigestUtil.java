package com.szrjk.util;

import com.szrjk.util.base64.Base64Util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5生成代码处理 
 * http://blog.csdn.net/jerryvon/article/details/22602811
 **/

public class MessageDigestUtil {

	private final static String  MD5key = "abt80[Auj~2%6JV!jY~^c,_81qg>AWSh]$1jtpWwilK4L9OwDxPJn~Be.sLN8c3G"; //32位
	
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
    
    private static byte[] MD5eccrypt(String info) throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] srcBytes = info.getBytes();
        //使用srcBytes更新摘要
        md5.update(srcBytes);
        //完成哈希计算，得到result
        byte[] resultBytes = md5.digest();
        return resultBytes;
    }
    
    /**
     * -- 前台
     * 1. 报文加密
     * 2. 报文获取摘要(前16个字符+key) MD5
     * -- 后台
     * 2. 报文摘要校验(前16个字符+key) MD5
     * 3. 报文解密
    **/
	public static String MD5Encode(String plainText) throws Exception {
		String subString = plainText;
		subString = subString + MD5key;
		String info = Base64Util.encode(subString);
		byte[] resultBytes = MD5eccrypt(info);
		String sRtnValue = hexString(resultBytes);
		return sRtnValue;
	}
	
    //----------------------------------------------------------------------
    //          报文 加/解密
    //----------------------------------------------------------------------
	// 报文加密密钥
 	private final static String msgKey = "ze_@w.5{RqY2kM$Z7UG%X2kn!LRuX~O7";  //32位Key
 	// 报文加密向量
 	private final static String iv = "R2h~c#hi";   //8位向量
 	// 加解密统一使用的编码方式
 	private final static String encoding = "utf-8";

 	/**
 	 * 3DES加密
 	 */
 	public static String doEncode(String plainText) throws Exception {
 		Key deskey = null;
 		DESedeKeySpec spec = new DESedeKeySpec(msgKey.getBytes());
 		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
 		deskey = keyfactory.generateSecret(spec);

 		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
 		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
 		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
 		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
 		return Base64Util.encode(new String(encryptData));
 	}

 	/**
 	 * 3DES解密
 	 */
 	public static String doDecode(String encryptText) throws Exception {
 		Key deskey = null;
 		DESedeKeySpec spec = new DESedeKeySpec(msgKey.getBytes());
 		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
 		deskey = keyfactory.generateSecret(spec);
 		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
 		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
 		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
 		String sText = Base64Util.decode(encryptText);
 		byte[] decryptData = cipher.doFinal(sText.getBytes());
 		return new String(decryptData, encoding);
 	}
    
    
    /**
     * 先BASE64转码-->再MD5转码
     * @param info
     * @return
     * @throws Exception
     */
//    public static String Encode(String info)throws Exception{
////    	String text = new String(info.toString().getBytes("UTF-8")); 
////    	System.out.println("text1="+text);
//    	String text = info;
//    	System.out.println("text2="+text);
////		String text = Base64Util.encode(text);
////		System.out.println("input="+text);
//		byte[] resultBytes = eccrypt(text);
//		String sRtnValue = hexString(resultBytes);
//		System.out.println("sRtnValue="+sRtnValue);
//		return  sRtnValue;
//    }
//    public static String Encode(String info)throws Exception{
////      info = new String(info.toString().getBytes("UTF-8"));
////  	String text = info + key;
//    	byte[] byte1 = info.getBytes();
//		System.out.println("1.before byte1.length =" + byte1.length);
//		System.out.println("2.before base64=" + info);
//		info = Base64Util.encode(info);
//		System.out.println("3.afte base64=" + info);
//		byte[] resultBytes = eccrypt(info);
//		String sRtnValue = hexString(resultBytes);
//		System.out.println("4.afte MD5=" + sRtnValue);
//		return sRtnValue;
//  }
 	//-----------------------------------------------------------------------------
 	//   报文压缩/解压
 	// http://www.cnblogs.com/gengaixue/archive/2013/09/04/3300658.html
 	//-----------------------------------------------------------------------------

// 	
//	/**
//	 * 使用gzip进行压缩
//	 */
//	public static String gzip(String primStr) {
//		if (primStr == null || primStr.length() == 0) {
//			return primStr;
//		}
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		GZIPOutputStream gzip = null;
//		try {
//			gzip = new GZIPOutputStream(out);
//			gzip.write(primStr.getBytes());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (gzip != null) {
//				try {
//					gzip.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
//	}
//	
//	/**
//	 * 使用gzip进行解压缩
//	 * @throws Exception 
//	 */
//	public static String gunzip(String compressedStr) throws Exception {
//		if (compressedStr == null) {
//			return null;
//		}
//
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		ByteArrayInputStream in = null;
//		GZIPInputStream ginzip = null;
//		byte[] compressed = null;
//		String decompressed = null;
//		try {
//			compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
////			compressed = Base64Util.decode(compressedStr).getBytes();
//			in = new ByteArrayInputStream(compressed);
//			ginzip = new GZIPInputStream(in);
//
//			byte[] buffer = new byte[1024];
//			int offset = -1;
//			while ((offset = ginzip.read(buffer)) != -1) {
//				out.write(buffer, 0, offset);
//			}
//			decompressed = out.toString();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (ginzip != null) {
//				try {
//					ginzip.close();
//				} catch (IOException e) {
//				}
//			}
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//				}
//			}
//			if (out != null) {
//				try {
//					out.close();
//				} catch (IOException e) {
//				}
//			}
//		}
//
//		return decompressed;
//	}
    
    
//	public static void main(String args[]) throws Exception{
//		String msg = "{\"BusiParams\":{\"content\":\"的\",\"deviceType\":\"1\",\"userSeqId\":\"1000000206\"},\"ServiceName\":\"sendNormalPost\"}";  
////		byte[] resultBytes = eccrypt(msg);  
////		System.out.println("密文是：" + hexString(resultBytes));  
//		System.out.println("--明文是：" + msg);  
//		String ss = MD5Encode(msg);
//		System.out.println("--密文是：" + ss);
//	}
	
//    public static String Encode(String info)throws Exception{
////      info = new String(info.toString().getBytes("UTF-8"));
////  	String text = info + key;
//		String text = Base64Util.encode(info);
////      byte[] resultBytes = eccrypt(info);
////      String sRtnValue = hexString(resultBytes);
//      return  text;
//  }
	
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