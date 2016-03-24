package com.wuyizhiye.base.datasource;

import org.apache.commons.codec.binary.Base64;

/**
 * @ClassName EncryptionTools
 * @Description 字节数组加密
 * @author li.biao
 * @date 2015-4-1
 */
public class EncryptionTools {
	private static final byte[] V = new byte[]{32,28,59,11,25,19,28,77,64,98,23,41,37,13,57,69,1,85};
	public static String encry(String sourceStr){
		if(sourceStr != null){
			int vlen = 0;
			byte[] source = sourceStr.getBytes();
			for(int i = 0; i < source.length; i++){
				source[i] = (byte) (source[i]+V[vlen]);
				vlen++;
				if(vlen>=V.length){
					vlen = 0;
				}
			}
			return Base64.encodeBase64URLSafeString(source);
		}
		return null;
	}
	
	public static String decry(String cryStr){
		if(cryStr != null){
			byte[] cryBytes = Base64.decodeBase64(cryStr);
			int vlen = 0;
			for(int i = 0; i < cryBytes.length; i++){
				cryBytes[i] = (byte) (cryBytes[i]-V[vlen]);
				vlen++;
				if(vlen>=V.length){
					vlen = 0;
				}
			}
			return new String(cryBytes);
		}
		return null;
	}
}
