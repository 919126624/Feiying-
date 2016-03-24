package com.wuyizhiye.base.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;

public class PicZipUtilCallUtil implements Callable<String>  {

	private BufferedInputStream Bin;
	FileOutputStream out;
	public PicZipUtilCallUtil(BufferedInputStream Bin,FileOutputStream out){
		this.Bin=Bin;
		this.out=out;
	}
	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub


        zipPic(Bin, out);
        
		return "yes";
	}
	public synchronized void zipPic(BufferedInputStream Bin,FileOutputStream out) throws IOException{
		BufferedOutputStream Bout=new BufferedOutputStream(out); 
		byte[] buff = new byte[4096];//缓存4k
        int b;  
        while((b=Bin.read(buff))!=-1){  
            Bout.write(buff,0,b);  
        }
        Bout.close();  
        out.close(); 
	}
	
	

}
