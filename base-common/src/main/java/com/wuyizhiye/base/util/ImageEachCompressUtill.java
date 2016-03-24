package com.wuyizhiye.base.util;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.wuyizhiye.base.common.ImageAttribute;

/**
 * @ClassName ImageEachCompressUtill
 * @Description 遍历文件夹 压缩指定规格图片
 * @author li.biao
 * @date 2015-4-1
 */
public class ImageEachCompressUtill {
	private static Logger logger=Logger.getLogger(ImageEachCompressUtill.class);
	public static void main(String[] args) throws Exception {
	   String filePath = "Z:/images/dataSource_Test/news";
	   //String filePath =  "Z:/images/dataSource_Test/blog/photo/images";
	   //String filePath =  "Z:/images/blog/photo/images";
	    compressFiles(filePath,300,200);// 填充要要压缩的图片的文件夹，且填充压缩图片的宽度及高度
	 } 
	 /*
	  * 通过递归得到某一路径下所有的目录及其文件
	  */
	 public static String compressFiles(String filePath,int width,int height){
	  File root = new File(filePath);
	  if(root.exists()){
	    File[] files = root.listFiles();
	    for(File file:files){     
	     if(file.isDirectory()){
	      /*
	       * 递归调用
	       */
	      return compressFiles(file.getAbsolutePath(),width,height);
	     }else{
	      try {
	    	if(file.getName().indexOf("_origin")!=-1)
			compressImage(file,width,height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("", e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("", e);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.error("", e);
		}}}}else{
	    	return "文件夹不存在";
	    }
	  return null;
	 }
	    /** 
	     * 获得指定文件的byte数组 
	     */  
	   static   byte[] getBytes(String filePath){  
	        byte[] buffer = null;  
	        try {  
	            File file = new File(filePath);  
	            FileInputStream fis = new FileInputStream(file);  
	            ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);  
	            byte[] b = new byte[4096];  
	            int n;  
	            while ((n = fis.read(b)) != -1) {  
	                bos.write(b, 0, n);  
	            }  
	            fis.close();  
	            bos.close();  
	            buffer = bos.toByteArray();  
	        } catch (FileNotFoundException e) {  
//	            e.printStackTrace();
	        	logger.error("", e);
	        } catch (IOException e) {  
//	            e.printStackTrace();
	        	logger.error("", e);
	        }  
	        return buffer;  
	    }  
	   public static  void compressImage(File file,int width,int height) throws IOException, InterruptedException, ExecutionException{
			
			final byte[] bytes = getBytes(file.getPath());
			final String originFileName = file.getName();
			String path = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
			final String suffix = originFileName.substring(originFileName.lastIndexOf("."));
			final String fileName = originFileName.substring(0,originFileName.lastIndexOf("_"));
			String oldFilePath = path+File.separator+fileName+"_"+width+"X"+height+suffix;
			File folder = new File(oldFilePath);
			if(!folder.exists()){
			List<ImageAttribute> list = new ArrayList<ImageAttribute>();	
			//width 或  height 属性为空则原图保存
			ImageAttribute imgattr = new ImageAttribute(width,height,true);
			list.add(imgattr);
			ImageUtil.compressSave(fileName,bytes,path,suffix,list);
			}else{
//			System.out.println("文件已存在");
			}
		}
}
