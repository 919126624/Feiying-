package com.wuyizhiye.base.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @ClassName Encrypt
 * @Description 加密解密
 * @author li.biao
 * @date 2015-4-1
 */
public class Encrypt {
	
	byte[] encryptKey;
	DESedeKeySpec spec;
	SecretKeyFactory keyFactory;
	SecretKey theKey;
	Cipher cipher;
	IvParameterSpec IvParameters;

	public Encrypt() {
		try {
			encryptKey = "hello.world.www.51zhiye.com".getBytes();
			spec = new DESedeKeySpec(encryptKey);
			keyFactory = SecretKeyFactory.getInstance("DESede");
			theKey = keyFactory.generateSecret(spec);
			cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			IvParameters = new IvParameterSpec(new byte[] { 12, 34, 56, 78, 90,
					87, 65, 43 });
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public byte[] encrypt(String password) {
		byte[] encrypted_pwd = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, theKey, IvParameters);
			byte[] plainttext = password.getBytes("UTF-8");
			encrypted_pwd = cipher.doFinal(plainttext);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return encrypted_pwd;
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
