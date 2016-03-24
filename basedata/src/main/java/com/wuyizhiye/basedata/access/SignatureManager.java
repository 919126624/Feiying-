package com.wuyizhiye.basedata.access;

import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @ClassName SignatureManager
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class SignatureManager {
	public static String decode(String mackey,Date time){
		String[] strs = mackey.split(",");
		byte[] bytes = new byte[strs.length];
		for(int i = 0; i < strs.length; i++){
			bytes[i] = Byte.parseByte(strs[i]);
		}
		String t = String.valueOf(time.getTime());
		Encrypt enc = new Encrypt(t+t+t);
		byte[] sig = enc.decrypt(bytes);
		return new String(sig);
	}
	
	public static void main(String[] args) {
//		System.out.println(decode("-48,-29,62,46,-102,76,23,-122,9,-12,-60,-126,-7,84,90,59,60,-70,30,-37,-39,47,28,-74",new Date(1368758973129L)));
	}
}
class Encrypt {
	byte[] encryptKey;
	DESedeKeySpec spec;
	SecretKeyFactory keyFactory;
	SecretKey theKey;
	Cipher cipher;
	IvParameterSpec IvParameters;

	public Encrypt(String time) {
		try {
			encryptKey = time.getBytes();
			spec = new DESedeKeySpec(encryptKey);
			keyFactory = SecretKeyFactory.getInstance("DESede");
			theKey = keyFactory.generateSecret(spec);
			cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			IvParameters = new IvParameterSpec(new byte[] { 12, 34, 56, 78, 90,87, 65, 43 });
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public byte[] decrypt(byte[] password) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, theKey, IvParameters);
			byte[] decryptedPassword = password;
			return cipher.doFinal(decryptedPassword);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
