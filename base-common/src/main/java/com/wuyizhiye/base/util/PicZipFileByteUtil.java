package com.wuyizhiye.base.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

import org.apache.log4j.Logger;

/**
 * @ClassName ObjectCopyUtils
 * @Description 图片解压，文件转byte工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class PicZipFileByteUtil {
	private final static Logger logger = Logger.getLogger(PicZipFileByteUtil.class);

	/**
	 * 图片的压缩后解压
	 * @param source 待压缩的资源目录
	 * @param targer 解缩的资源目录
	 * @param tiaoxingmasList 条形码集合
	 * @param tiaoxingmaDisplay 条形码展示图的集合
	 * @param tiaoxingmaDisplay 条形码详情图图的集合
	 * @param size 图片内存大小，0为无限制
	 * @return 
	 * @throws Exception 
	 */
	public static void zipjie(String source,String targer ,List<String> tiaoxingmasList,List<StringBuilder> tiaoxingmaDisplay,List<StringBuilder> tiaoxingmaDetails, long size) throws Exception{

		ZipFile zipFile = new ZipFile(source);   
		StringBuilder stringBuilder=null;//记录条形码和展示图
		StringBuilder DetailsBuilder=null;//记录条形码和详情图
		List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
		for (FileHeader fileHeader : fileHeaderList) {
		    if (fileHeader != null) {
		    	if(size != 0 && fileHeader.getUncompressedSize() > size * 1024) {
		    		tiaoxingmasList.add("ERROR");
		    		return;
		    	}
		    	String entityname=fileHeader.getFileName();
		        String outFilePath = targer + System.getProperty("file.separator") + fileHeader.getFileName();
		        File outFile = new File(outFilePath);
		        if (fileHeader.isDirectory()) {

            		if(entityname.indexOf("/")==entityname.lastIndexOf("/")){//是否一级条形目录
            			if(stringBuilder!=null){//展示图
            				tiaoxingmaDisplay.add(stringBuilder);
            			}
            			if(DetailsBuilder!=null){//详情图
            				tiaoxingmaDetails.add(DetailsBuilder);
            			}
            			stringBuilder=new StringBuilder();
            			DetailsBuilder=new StringBuilder();
            			String barCode=entityname.replace("/", "");//条形码
            			tiaoxingmasList.add(barCode);
            			stringBuilder.append(barCode+",");
            			DetailsBuilder.append(barCode+",");
            		}
		            outFile.mkdirs();
		            continue;
		        }
		        File parentDir = outFile.getParentFile();
				String tmpName =entityname.substring(
						entityname.lastIndexOf(".") + 1,
						entityname.length());
		        if (!ImageUtil.isPicture(entityname, tmpName)) {
					continue;
				}
		        if (!parentDir.exists()) {
		            parentDir.mkdirs();
		        }
            	if(parentDir.getName().indexOf("PIC4GOODS")>=0){//如果是展示图
            		stringBuilder.append(entityname+",");
            	}
            	if(parentDir.getName().indexOf("PIC4ACTIVITY")>=0){//如果是展示图
            		DetailsBuilder.append(entityname+",");
            	}
		     // 获得文件后缀名 
  			  	//String extend = entityname.substring(entityname.lastIndexOf(".") + 1, entityname.length()); 
//            	try {
//					if(entityname.indexOf("PIC4GOODS")>=0 && ImageUtil.isPicture(entityname, extend)){//如果是展示图
//						stringBuilder.append(entityname+",");
//					}
//					if(entityname.indexOf("PIC4ACTIVITY")>=0 && ImageUtil.isPicture(entityname, extend)){//如果是展示图
//						DetailsBuilder.append(entityname+",");
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		        ZipInputStream is = zipFile.getInputStream(fileHeader);
		        OutputStream os = new FileOutputStream(outFile);

		        int readLen = -1;
		        byte[] buff = new byte[4096];
		        while ((readLen = is.read(buff)) != -1) {
		            os.write(buff, 0, readLen);
		        }
		        os.close();
		        os = null;
		        is.close();
		        is = null;
		        UnzipUtil.applyFileAttributes(fileHeader, outFile);
		    }else {
		        System.err.println("fileheader不能为null");
		    }
		}
		if(stringBuilder!=null){//展示图
			tiaoxingmaDisplay.add(stringBuilder);
		}
		if(DetailsBuilder!=null){//详情图
			tiaoxingmaDetails.add(DetailsBuilder);
		}
	}
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
//            e.printStackTrace();
        	logger.error("", e);
        } catch (IOException e) {  
//            e.printStackTrace();  
        	logger.error("", e);
        }  
        return buffer;  
    }

}
