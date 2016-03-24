package com.wuyizhiye.cmct.phone.caas.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @ClassName CaasMD5
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public class CaasMD5 {

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };   
    protected static MessageDigest messagedigest = null;  
    
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");    
        } catch (Exception e) {
            e.printStackTrace();    
        }    
    }    
    
    public static String getMD5(String file_path) throws Exception {  
    	File file = new File(file_path);
        InputStream fis;  
        fis = new FileInputStream(file);   
        byte[] buffer = new byte[1024]; 
        int numRead = 0;    
        
        while ((numRead = fis.read(buffer)) > 0) {	
            messagedigest.update(buffer, 0, numRead);    
        }  
        fis.close();
        
        return bufferToHex(messagedigest.digest());    
    }    
      
    public static String getStringMD5(String str){  
    	byte[] buffer = str.getBytes();
        messagedigest.update(buffer);
        return bufferToHex(messagedigest.digest());
    }  
    
    public static String bufferToHex(byte bytes[]) {  
        return bufferToHex(bytes, 0, bytes.length);    
    }    
    
    private static String bufferToHex(byte bytes[], int m, int n) { 
        StringBuffer stringbuffer = new StringBuffer(2 * n); 
        int k = m + n;   
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);    
        }    
        return stringbuffer.toString();    
    }    
    
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];//
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);    
    }

} 
