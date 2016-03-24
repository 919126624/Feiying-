package com.wuyizhiye.hr.utils;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.wuyizhiye.base.util.ImageUtil;
  
public class ImageCutUtils {  
  
 //裁剪图片
    public static void cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{   
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
        ImageReader reader = (ImageReader)iterator.next();   
        InputStream in=new FileInputStream(src);  
        ImageInputStream iis = ImageIO.createImageInputStream(in);   
        reader.setInput(iis, true);   
        ImageReadParam param = reader.getDefaultReadParam();   
        Rectangle rect = new Rectangle(x, y, w,h);    
        param.setSourceRegion(rect);   
        BufferedImage bi = reader.read(0,param);     
        ImageIO.write(bi, "jpg", new File(dest));             

 }
    
	
	  public final static void convert(String srcImageFile, String formatName, String destImageFile) {
	        try {
	        	BufferedImage bufferedImage;
	        	bufferedImage=ImageIO.read(new File(srcImageFile));
	    		BufferedImage newBufferedImage =new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	    		newBufferedImage .createGraphics().drawImage(bufferedImage,0,0,Color.white,null);
	    		
	    		ImageIO.write(newBufferedImage,formatName,new File(destImageFile)  );
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	  
   public static  void main(String[] args){
	   ImageUtil.convert("C:/Users/Administrator/Pictures/cut/b077b38c6a92ac3b5bc05d7ed57486a7.png",ImageUtil.IMAGE_TYPE_JPG ,"C:/Users/Administrator/Pictures/cut/b077b38c6a92ac3b5bc05d7ed57486a7.jpg");
//       System.out.println("完成");
   } 

}  
