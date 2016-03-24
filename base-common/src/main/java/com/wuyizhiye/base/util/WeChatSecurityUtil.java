package com.wuyizhiye.base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import Decoder.BASE64Encoder;

/**
 * @ClassName WeChatSecurityUtil
 * @Description 微信工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class WeChatSecurityUtil {
	private final static Logger logger = Logger.getLogger(WeChatSecurityUtil.class);
	private static final Random random= new Random(); 
	
	private static String getRandomSixteenNum(){
		String number="";
		for (int i = 0; i < 16; i++) {
			number+=random.nextInt(10);
		}
		return number;
	}
	
	
	public static String geMsgSignature(String[] strary){
		
		Arrays.sort(strary);
		String strs = "";
		for(int i=0;i<strary.length;i++){
			strs += strary[i];
		}
		StringBuffer hexstr = new StringBuffer();
		try {
		// SHA1签名生成
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(strs.getBytes());
		byte[] digest = md.digest();

		
		String shaHex = "";
		for (int i = 0; i < digest.length; i++) {
			shaHex = Integer.toHexString(digest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		}catch (Exception e) {
//				e.printStackTrace();
			logger.error(e.toString(), e);
		}
		return hexstr.toString();
	}
	
	public static String msgEncode(String msg,String cropId, String password){
		String enstr = getRandomSixteenNum()+gene4String(msg.getBytes().length)+msg+cropId;
		byte[] enary = encrypt(enstr,password);	
		return base64Encode(enary);
	}
	
	public static String msgDecode(String content,String password) throws IOException{
		byte[] by = decrypt(base64Decode(content),password);
		String decodestr = parseByte2HexStr(by);
		return decodestr;
	}
	
	public static String base64Encode(byte[] content){
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(content);
	}
	
	public static byte[] base64Decode(String content) throws IOException{		
		return Base64.decodeBase64(content.getBytes());
	}
	
	/**
	　　* 加密
	　　*
	　　* @param content 需要加密的内容
	　　* @param password 加密密码
	　　* @return
	　　*/
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES/CBC/NoPadding");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
				logger.error("", e);
			} catch (NoSuchPaddingException e) {
//				e.printStackTrace();
				logger.error("", e);
			} catch (InvalidKeyException e) {
//				e.printStackTrace();
				logger.error("", e);
			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
				logger.error("", e);
			} catch (IllegalBlockSizeException e) {
//				e.printStackTrace();
				logger.error("", e);
			} catch (BadPaddingException e) {
//				e.printStackTrace();
				logger.error("", e);
			}
			return null;
		}
	
	/**解密
	　　* @param content 待解密内容
	　　* @param password 解密密钥
	　　* @return
	　　*/
	public static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (InvalidKeyException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (BadPaddingException e) {
//			e.printStackTrace();
			logger.error("", e);
		}
		return null;
	}
	
	public static String gene4String(int t){
		String s = String.valueOf(t);
		while(s.length()<4){
			s = "0"+s;
		}
		return s;
	}
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	} 
	
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  
	
	public static void main(String args[]) throws IOException{
		//System.out.println(WeChatSecurityUtil.msgEncode("123456", "sdfsdfwefdfd", "dingjian"));
		String s = WeChatSecurityUtil.msgDecode("P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==", "dingjiandingjiandingjiandingjian");
		//System.out.println(new String(base64Decode("FA4/Cbq/8qOZIGWBAIh9Zu8Hf1lhLfEfH81IWhlKKpkkzS6Wr4ygDQW2d3TcGiyLY+if/+KabsG7BDJQwwEJ/A")));
//		System.out.println(s+"==="+s.substring(20,s.lastIndexOf("sdfsdfwefdfd")));
		//System.out.println(new String(decrypt(encrypt("skdkfjkdjfdf","dingjian"),"dingjian")));
		//String str = "ddingksdjsdfkdf周里";
		//System.out.println(str.getBytes(Charset.defaultCharset()).length);
		//System.out.println(base64Encode("dingjiandingjiandingjiandingjian".getBytes()));
		/*String[] ary = new String[4];
		ary[0] = "dingjian";
		ary[1]= "1965314650";
		ary[2] = "1411528189";
		ary[3]="zVbc0MO383OdN6u8OYMbkCDROK+2DiMLU6siiZDxGXr9FbgBvQQRfGZUcN+Qc9ND9d6VrSUcgwciFSYMdEYnGw";
//		System.out.println(WeChatSecurityUtil.geMsgSignature(ary));*/
	}
}
