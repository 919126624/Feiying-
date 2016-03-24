package com.wuyizhiye.base.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.wuyizhiye.base.common.ImageAttribute;

/**
 * @ClassName ImageUtil
 * @Description 图片工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class ImageUtil {
	private final static Logger logger = Logger.getLogger(ImageUtil.class);

	static {
		System.setProperty("java.awt.headless", "true");
	}
	/**
	 * 压缩图片
	 * @param sourceImage 原图片
	 * @param width 压缩后的宽
	 * @param height 压缩后的高
	 * @param isEqualRatio 是否等比压缩
	 * @return
	 */
	public static byte[] resize(byte[] sourceImage, Integer width, Integer height, boolean isEqualRatio) {
		try {
			if(width == null || height == null) return sourceImage;
			
			String tempDir = System.getProperty("java.io.tmpdir");
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			resize(sourcePath, targetPath, width, height, isEqualRatio);
			File targetFile = new File(targetPath);
			byte[] result = FileUtils.readFileToByteArray(targetFile);
			FileUtils.deleteQuietly(sourceFile);
			FileUtils.deleteQuietly(targetFile);
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	/**
	 * 图片加水印
	 * @param sourceImage 原图
	 * @param logoPath 水印图片
	 * @return
	 */
	public static byte[] watermark(byte[] sourceImage, String logoPath) {
		try {
			String tempDir = System.getProperty("java.io.tmpdir");
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);  
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			//watermark(sourcePath, targetPath, logoPath);
			File targetFile = new File(targetPath);
			byte[] result = FileUtils.readFileToByteArray(targetFile);
			FileUtils.deleteDirectory(sourceFile);
			FileUtils.deleteDirectory(targetFile);
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	
	/**
	 * 压缩图片
	 * 
	 * @param sourcePath 源文件路径
	 * @param targetPath 缩略图路径
	 * @param width 设定宽
	 * @param height 设定长
	 * @param isEqualRatio 是否等比缩放
	 */
	public static void resize(String sourcePath, String targetPath, int width, int height, boolean isEqualRatio) {
		Float quality = 1.0f;
		File srcFile = new File(sourcePath);
		File distFile = new File(targetPath);
		try
		{
			
			Image srcImageFile = ImageIO.read(new File(sourcePath));
			int new_w = width;
			int new_h = height;
			int o_width = srcImageFile.getWidth(null);
			int o_height = srcImageFile.getHeight(null);
			if(quality >= 1.0f && o_width <= width && o_height <= height)
			{
				FileUtils.copyFile(srcFile, distFile);
			}
			else
			{
				if(isEqualRatio)
				{
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) o_width) / (double) width;
					double rate2 = ((double) o_height) / (double) height;
					double rate = rate1 > rate2 ? rate1 : rate2;
					new_w = (int) (((double) o_width) / rate);
					new_h = (int) (((double) o_height) / rate);
				}

				BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(srcImageFile.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0,
						null);

				FileOutputStream out = new FileOutputStream(distFile);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);

				jep.setQuality(quality, true);
				encoder.encode(tag, jep);

				out.close();
			}
		}
		catch(IOException e)
		{
//			e.printStackTrace();
			logger.error("", e);
		}
	}

	/**
	 * 保存图片
	 * @throws IOException 
	 */
	public static void saveImage(byte[] bytes,String path) throws IOException{
		FileUtils.writeByteArrayToFile(new File(path), bytes);
	}
	
	/**
	 * 删除图片
	 * @throws IOException 
	 */
	public static void delImage(String path) throws IOException{
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}	
	}
	
	/**
	 * 批量删除图片
	 */
	public static void delImage(List pathList) throws IOException{
		for(int i=0;i<pathList.size();i++){
		File file = new File(pathList.get(i).toString());
		if(file.exists()){
			file.delete();
		}	
		}
	}
	
	/**
	 * 按图片属性列表参数压缩不同尺寸的图片,返回随机生成的图片名,图片保存名为 XXX_100X75.png
	 * @param bytes
	 * @param path  图片目录
	 * @param suffix 图片后缀
	 * @param list  图片属性列表
	 * @return
	 */
	public static String compressSave(final byte[] bytes,final String path,final String suffix,List<ImageAttribute> list){
		final String fileId = UUID.randomUUID().toString();
		for(ImageAttribute imgattr : list) {
			
			byte[] compressedImage = ImageUtil.resize(bytes, imgattr.getWidth(), imgattr.getHeight(), imgattr.isEqualRatio());
			String nameSuffix = 
				imgattr.isNoShowSuffix()?"":((null==imgattr.getWidth()||null==imgattr.getHeight())?"_origin":("_"+imgattr.getWidth()+"X"+imgattr.getHeight()));
			String tmpname = fileId+nameSuffix+suffix;
			
			try {
				saveImage(compressedImage, path+"//"+tmpname);
			} catch (IOException e) {					
//				e.printStackTrace();
				logger.error("", e);
			}
			
		}
			
		return fileId;
	}
	
	/**
	 * 按指定文件名 图片属性列表参数压缩保存不同尺寸的图片,返回随机生成的图片名,图片保存名为 fileName_100X75.png
	 * @param fileName 指定的图片保存名
	 * @param bytes
	 * @param path		图片目录
	 * @param suffix	图片后缀
	 * @param list		图片属性列表
	 */
	public static void  compressSave(String fileName,final byte[] bytes,final String path,final String suffix,List<ImageAttribute> list){
		final String fileId = fileName;
		for(ImageAttribute imgattr : list) {
			byte[] compressedImage = ImageUtil.resize(bytes, imgattr.getWidth(), imgattr.getHeight(), imgattr.isEqualRatio());
			String nameSuffix = 
				imgattr.isNoShowSuffix()?"":((null==imgattr.getWidth()||null==imgattr.getHeight())?"_origin":("_"+imgattr.getWidth()+"X"+imgattr.getHeight()));
			String tmpname = fileId+nameSuffix+suffix;
			try {
				saveImage(compressedImage, path+"//"+tmpname);
			} catch (IOException e) {					
//				e.printStackTrace();
				logger.error("", e);
			}
		}
	}
	
	/**
	 * 压缩图片
	 * 
	 * @param sourcePath 源文件路径
	 * @param targetPath 缩略图路径
	 * @param width 设定宽
	 * @param height 设定长
	 * @param isEqualRatio 是否等比缩放或放大
	 */
	public static void enlarge(String sourcePath, String targetPath, int width, int height, boolean isEqualRatio) {
		Float quality = 1.0f;
		File distFile = new File(targetPath);
		try
		{
			Image srcImageFile = ImageIO.read(new File(sourcePath));
			int new_w = width;
			int new_h = height;
			int o_width = srcImageFile.getWidth(null);
			int o_height = srcImageFile.getHeight(null);
			
				if(isEqualRatio)
				{
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) o_width) / (double) width;
					double rate2 = ((double) o_height) / (double) height;
					double rate = rate1 > rate2 ? rate1 : rate2;
					new_w = (int) (((double) o_width) / rate);
					new_h = (int) (((double) o_height) / rate);
				}

				BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
				
				tag.getGraphics().drawImage(srcImageFile.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0,
						null);

				FileOutputStream out = new FileOutputStream(distFile);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);

				jep.setQuality(quality, true);
				encoder.encode(tag, jep);

				out.close();
			
		}
		catch(IOException e)
		{
//			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @param width
	 * @param height
	 * @param isEqualRatio
	 */
	public static void fillimg(String sourcePath, String targetPath, int width, int height) {
		Float quality = 1.0f;
		File distFile = new File(targetPath);
		try
		{
			Image srcImageFile = ImageIO.read(new File(sourcePath));
			int new_w = width;
			int new_h = height;
			int o_width = srcImageFile.getWidth(null);
			int o_height = srcImageFile.getHeight(null);
			
				

			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			
			tag.getGraphics().drawImage(srcImageFile, 
					(new_w-o_width)/2, 
					(new_h-o_height)/2,
					null);

			FileOutputStream out = new FileOutputStream(distFile);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);

			jep.setQuality(quality, true);
			encoder.encode(tag, jep);

			out.close();
			
		}
		catch(IOException e)
		{
//			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	
	
	 /**********************************************************************************/
	  /**
	     * 几种常见的图片格式
	     */
	    public static String IMAGE_TYPE_GIF = "gif";// 图形交换格式
	    public static String IMAGE_TYPE_JPG = "jpg";// 联合照片专家组
	    public static String IMAGE_TYPE_JPEG = "jpeg";// 联合照片专家组
	    public static String IMAGE_TYPE_BMP = "bmp";// 英文Bitmap（位图）的简写，它是Windows操作系统中的标准图像文件格式
	    public static String IMAGE_TYPE_PNG = "png";// 可移植网络图形
	    public static String IMAGE_TYPE_PSD = "psd";// Photoshop的专用格式Photoshop



	
	/**
	 *  图片合成
	   *  x,y从左上角开始
	   * @param srcImageFile 合成图片主图 ——路径
	   * @param pressImageFile 合成图片附图  ——路径      
	   * @param destImageFile  合成目标图片——路径
	   * @param x 附图位置x坐标
	   * @param y 附图位置y坐标
	   */
	  public static void composePic(String srcImageFile,String pressImageFile,String destImageFile,int x,int y)
	  {
	      try
	      {	        //读取主图
	        //File fileOne = new File("C:/images/Chrysanthemum.jpg");
	    	 
	    	  File fileOne = new File(srcImageFile);
	        BufferedImage ImageOne = ImageIO.read(fileOne);
	        int width = ImageOne.getWidth();//图片宽度
	        int height = ImageOne.getHeight();//图片高度
	        String fileName = srcImageFile.substring(0, srcImageFile.lastIndexOf("."));
			String suffix = srcImageFile.substring(srcImageFile.lastIndexOf(".")+1);
	        //从图片中读取RGB
	        int[] ImageArrayOne = new int[width*height];
	        ImageArrayOne = ImageOne.getRGB(0,0,width,height,ImageArrayOne,0,width);
	        //对附图做相同的处理
	        File fileTwo = new File(pressImageFile);
	        
	        BufferedImage ImageTwo = ImageIO.read(fileTwo);
	        int widthTwo = ImageTwo.getWidth();//图片宽度
	        int heightTwo = ImageTwo.getHeight();//图片高度
	        int[] ImageArrayTwo = new int[widthTwo*heightTwo];
	        ImageArrayTwo = ImageTwo.getRGB(0,0,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);
	         
	        //生成目标图片
	        BufferedImage ImageNew = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	        ImageNew.setRGB(0,0,width,height,ImageArrayOne,0,width);//设置左半部分的RGB
//	        System.out.println("宽度"+widthTwo+"_高度_"+heightTwo);
	        ImageNew.setRGB(x,y,widthTwo,heightTwo,ImageArrayTwo,0,widthTwo);//设置右半部分的RGB
	        
	        File outFile = new File(destImageFile);
	        ImageIO.write(ImageNew, suffix, outFile);//写图片
	 
	      }
	      catch(Exception e)
	      {
//	        e.printStackTrace();
	    	  logger.error("", e);
	      }
	  }
	  /**
	   *图片旋转
	   * @param srcImageFile 原图路径
	   * @param destImageFile 目标图路径
	   * @param degree 旋转角度
	   * @param bgcolor 背景色
	   * */
	  
	  public static File rotateImg(String srcImageFile,String destImageFile, int degree, Color bgcolor) throws IOException {  
		    BufferedImage image = ImageIO.read(new File(srcImageFile));
		    String fileName = srcImageFile.substring(0, srcImageFile.lastIndexOf("."));
			String suffix = srcImageFile.substring(srcImageFile.lastIndexOf(".")+1);
	        int iw = image.getWidth();//原始图象的宽度   
	        int ih = image.getHeight();//原始图象的高度  
	        int w = 0;  
	        int h = 0;  
	        int x = 0;  
	        int y = 0;  
	        degree = degree % 360;  
	        if (degree < 0)  
	            degree = 360 + degree;//将角度转换到0-360度之间  
	        double ang = Math.toRadians(degree);//将角度转为弧度  
	  
	        /** 
	         *确定旋转后的图象的高度和宽度 
	         */  
	  
	        if (degree == 180 || degree == 0 || degree == 360) {  
	            w = iw;  
	            h = ih;  
	        } else if (degree == 90 || degree == 270) {  
	            w = ih;  
	            h = iw;  
	        } else {  
	            int d = iw + ih;  
	            w = (int) (d * Math.abs(Math.cos(ang)));  
	            h = (int) (d * Math.abs(Math.sin(ang)));  
	        }  
	  
	        x = (w / 2) - (iw / 2);//确定原点坐标  
	        y = (h / 2) - (ih / 2);  
	        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());  
	        Graphics2D gs = (Graphics2D)rotatedImage.getGraphics();  
	        
	        
	     // ---------- 增加下面的代码使得背景透明 -----------------
	     
	        
	       /* rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
	        gs.dispose();
	        gs=rotatedImage.createGraphics();*/
 
	        
	        // ---------- 背景透明代码结束 -----------------
	        if(bgcolor==null){  
	            rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);  
	        }else{  
	            gs.setColor(bgcolor);  
	           
	            gs.fillRect(0, 0, w, h);//以给定颜色绘制旋转后图片的背景  
	        }  
	          
	        AffineTransform at = new AffineTransform();  
	        at.rotate(ang, w / 2, h / 2);//旋转图象  
	        at.translate(x, y);  
	        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);  
	        op.filter(image, rotatedImage);  
	        image = rotatedImage;  
	          
	        ByteArrayOutputStream  byteOut= new ByteArrayOutputStream();  
	        ImageOutputStream imageOut = ImageIO.createImageOutputStream(byteOut);  
	        InputStream  inputStream = new ByteArrayInputStream(byteOut.toByteArray());  
	          
	        
	        
	        File outFile = new File(destImageFile);
	        ImageIO.write(rotatedImage,suffix , outFile);
	        
	        
	        alphaImage(image, 0.0);//透明度
	        
	        
	        ImageIO.write(image, suffix , imageOut); 
	        
	        
	        File fTemp = File.createTempFile(System.currentTimeMillis()+"","."+suffix);
	        return fTemp;  
	    }  
	  

	    /**
	     * 缩放图像（按比例缩放）
	     * @param srcImageFile 源图像文件地址
	     * @param result 缩放后的图像地址
	     * @param scale 缩放比例
	     * @param flag 缩放选择:true 放大; false 缩小;
	     */
	    public final static void scale(String srcImageFile, String result,
	            int scale, boolean flag) {
	        try {
	            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
	            int width = src.getWidth(); // 得到源图宽
	            int height = src.getHeight(); // 得到源图长
	            if (flag) {// 放大
	                width = width * scale;
	                height = height * scale;
	            } else {// 缩小
	                width = width / scale;
	                height = height / scale;
	            }
	            Image image = src.getScaledInstance(width, height,
	                    Image.SCALE_DEFAULT);
	            BufferedImage tag = new BufferedImage(width, height,
	                    BufferedImage.TYPE_INT_RGB);
	            Graphics g = tag.getGraphics();
	            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
	            g.dispose();
	            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
	        } catch (IOException e) {
//	            e.printStackTrace();
	        	logger.error("", e);
	        }
	    }


	    /**
	     * 缩放图像（按高度和宽度缩放）
	     * @param srcImageFile 源图像文件地址
	     * @param result 缩放后的图像地址
	     * @param height 缩放后的高度
	     * @param width 缩放后的宽度
	     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
	     */
	    public final static void _scale2(String srcImageFile, String result, int height, int width, boolean bb) {
	        try {
	            double ratio = 0.0; // 缩放比例
	            File f = new File(srcImageFile);
	            BufferedImage bi = ImageIO.read(f);
	            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
	            // 计算比例
	            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
	                if (bi.getHeight() > bi.getWidth()) {
	                    ratio = ((new Integer(height)).doubleValue()
	                            / bi.getHeight()-0.05);
	                } else {
	                    ratio = ((new Integer(width)).doubleValue() / bi.getWidth())-0.05;
	                }
	                AffineTransformOp op = new AffineTransformOp(AffineTransform
	                        .getScaleInstance(ratio, ratio), null);
	                itemp = op.filter(bi, null);
	            }
	            if (bb) {//补白
	                BufferedImage image = new BufferedImage(width, height,
	                        BufferedImage.TYPE_INT_RGB);
	                Graphics2D g = image.createGraphics();
	                g.setColor(Color.white);
	                g.fillRect(0, 0, width, height);
/*	                if (width == itemp.getWidth(null))
	                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
	                            itemp.getWidth(null), itemp.getHeight(null),
	                            Color.white, null);
	                else
	                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, (height - itemp.getHeight(null)) / 2,
	                            itemp.getWidth(null), itemp.getHeight(null),
	                            Color.white, null);*/
	                
	                g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, (height - itemp.getHeight(null)) / 2,
                          itemp.getWidth(null), itemp.getHeight(null),
                          Color.white, null);
	                
	                g.dispose();
	                itemp = image;
	            }
	            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
	        } catch (IOException e) {
//	            e.printStackTrace();
	        	logger.error("", e);
	        }
	    }
	    
	    
	    public final static void scale2(String srcImageFile, String result, int height, int width, boolean bb) {
	        try {
	            double ratio = 0.0; // 缩放比例
	            File f = new File(srcImageFile);
	            BufferedImage bi = ImageIO.read(f);
	            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
	            // 计算比例
	            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
	                if (bi.getHeight() > bi.getWidth()) {
	                    ratio = (new Integer(height)).doubleValue()
	                            / bi.getHeight();
	                } else {
	                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
	                }
	                AffineTransformOp op = new AffineTransformOp(AffineTransform
	                        .getScaleInstance(ratio, ratio), null);
	                itemp = op.filter(bi, null);
	            }
	            if (bb) {//补白
	                BufferedImage image = new BufferedImage(width, height,
	                        BufferedImage.TYPE_INT_RGB);
	                Graphics2D g = image.createGraphics();
	                g.setColor(Color.white);
	                g.fillRect(0, 0, width, height);
	                if (width == itemp.getWidth(null))
	                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
	                            itemp.getWidth(null), itemp.getHeight(null),
	                            Color.white, null);
	                else
	                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
	                            itemp.getWidth(null), itemp.getHeight(null),
	                            Color.white, null);
	                g.dispose();
	                itemp = image;
	            }
	            ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
	        } catch (IOException e) {
//	            e.printStackTrace();
	        	logger.error("", e);
	        }
	    }
	   

	    /**
	     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	     * @param srcImageFile 源图像地址
	     * @param formatName 包含格式非正式名称的 String：如JPG、JPEG、GIF等
	     * @param destImageFile 目标图像地址
	     */
	    public final static void convert(String srcImageFile, String formatName, String destImageFile) {
	        try {
	            File f = new File(srcImageFile);
	            f.canRead();
	            f.canWrite();
	            BufferedImage src = ImageIO.read(f);
	            ImageIO.write(src, formatName, new File(destImageFile));
	        } catch (Exception e) {
//	            e.printStackTrace();
	        	logger.error("", e);
	        }
	    }


	 

	    /**
	     * 计算text的长度（一个中文算两个字符）
	     * @param text
	     * @return
	     */
	    public final static int getLength(String text) {
	        int length = 0;
	        for (int i = 0; i < text.length(); i++) {
	            if (new String(text.charAt(i) + "").getBytes().length > 1) {
	                length += 2;
	            } else {
	                length += 1;
	            }
	        }
	        return length / 2;
	    }
	    public static void rota_compose(String srcImageFile,String pressImageFile,String destImageFile,int x,int y,int width,int height,int degree, Color bgcolor) throws IOException{
	    	System.currentTimeMillis();
	    	File folder = new File(SystemConfig.getParameter("image_path") + "_temp");
	    	//File folder = new File("C:/images/" + "_temp");
			if(!folder.exists()){
				folder.mkdirs();
			}
			String fileName = srcImageFile.substring(0, srcImageFile.lastIndexOf("."));
			String suffix = srcImageFile.substring(srcImageFile.lastIndexOf("."));
			String timeMillis ="_"+System.currentTimeMillis();
			String dirString =  folder.getPath();
			rotateImg(pressImageFile, dirString+"/"+timeMillis+suffix, degree, bgcolor);
			File temp1 = new File(dirString+"/"+timeMillis+suffix);
	        BufferedImage ImageTemp1 = ImageIO.read(temp1);
	        int temp1_width = ImageTemp1.getWidth();//图片宽度
	        int temp1_height = ImageTemp1.getHeight();//图片高度
	        if(temp1_width>width||temp1_height>height){
	        	scale2(pressImageFile,dirString+File.separator+timeMillis+suffix, width, height, true);
	        }
	       composePic(srcImageFile,dirString+File.separator+timeMillis+suffix, destImageFile, x, y);
	        //temp1.deleteOnExit();
	        temp1.delete();
	        //folder.deleteOnExit();
	        folder.delete();
	    }
	    
	    public static void scal_compose(String srcImageFile,String pressImageFile,String destImageFile,int x,int y,int width,int height) throws IOException{
	    	File folder = new File(SystemConfig.getParameter("image_path") + "_temp");
	    	//File folder = new File("C:/images/" + "_temp");
			if(!folder.exists()){
				folder.mkdirs();
			}
			String fileName = srcImageFile.substring(0, srcImageFile.lastIndexOf("."));
			String suffix = srcImageFile.substring(srcImageFile.lastIndexOf("."));
			String timeMillis ="_"+System.currentTimeMillis();
			String dirString =  folder.getPath();
			File temp1 = new File(pressImageFile);
	        BufferedImage ImageTemp1 = ImageIO.read(temp1);
	        int temp1_width = ImageTemp1.getWidth();//图片宽度
	        int temp1_height = ImageTemp1.getHeight();//图片高度
	        /*if(temp1_width>width||temp1_height>height){
	        	ImageUtils.scale2(pressImageFile,dirString+"/"+timeMillis+suffix, width, height, true);
	        	ImageUtils.composePic(srcImageFile,dirString+"/"+timeMillis+suffix, destImageFile, x, y);
	        }else{
	        	ImageUtils.composePic(srcImageFile,pressImageFile, destImageFile, x, y);
	        }*/
	        whiteBord(pressImageFile,dirString+"/"+timeMillis+suffix, width, height);
	        composePic(srcImageFile,dirString+"/"+timeMillis+suffix, destImageFile, x, y);
	        //temp1.deleteOnExit();
	        //temp1.delete();
	        new File(dirString+"/"+timeMillis+suffix).delete();
	        //folder.deleteOnExit();
	        folder.delete();
	    }
	    
	    /**
		 * 设置图片透明度（异常则返回源图片）
		 * @param img 源图片
		 * @param alf 透明度(范围0-1)
		 * @return
		 */
		public static Image alphaImage(Image img, double alf) {
			if (img == null) {
				return img;
			}
			alf = alf < 0 ? 0 : alf > 1 ? 1 : alf;
			
			try{
				int imgW = img.getWidth(null);
				int imgH = img.getHeight(null);
				
				BufferedImage bi = new BufferedImage(imgW, imgH, 3);
				bi.getGraphics().drawImage(img, 0, 0, null);
				
				int tmp = (int)(alf * 255.0);
				for(int i = 0; i < imgW; i++){
					for(int j = 0; j < imgH; j++) {
						int rgb = bi.getRGB(i, j);
						Color c = new Color(rgb);
						Color cc =  new Color(c.getRed(), c.getGreen(), c.getBlue(), tmp);
						bi.setRGB(i, j, cc.getRGB());
					}
				}
				return bi;
			}catch(Exception e){
				return img;
			}
		}
		public static void whiteBord(String srcImageFile,String destImageFile,int newwidth,int newheight) throws IOException{
		 File _file = new File(srcImageFile); // 读入文件  
		   Image src = javax.imageio.ImageIO.read(_file); // 构造Image对象  
		   int width = src.getWidth(null); // 得到源图宽  
		   int height = src.getHeight(null); // 得到源图长  
		    
		   //需要长度  
		  //  int newwidth = 22;//width / 2  
		  // int newheight = 22;//height / 2  
		   BufferedImage image = new BufferedImage(newwidth, newheight,  
		     BufferedImage.TYPE_INT_RGB);  
		   Graphics graphics = image.getGraphics();  
		    
		          
		   graphics.drawImage(src, 0, 0, newwidth, newheight, null); // 绘制缩小后的图  
		  // 画边框,在drawImage后面，下面代码给图片加上两个像素的白边     
		   graphics.drawRect(0, 0, newwidth - 1, newheight - 1);  
		   graphics.drawRect(1, 1, newwidth - 1, newheight - 1);  
		   graphics.drawRect(0, 0, newwidth - 2, newheight - 2); 
		    
		   FileOutputStream out = new FileOutputStream(destImageFile); // 输出到文件流  
		   JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
		   encoder.encode(image); // JPEG编码  
		   out.close();
		   }
	  /**********************************************************************************/
	
		
		/** 
		  * 判断文件是否为图片<br> 
		  * <br> 
		  * @param pInput 文件名<br> 
		  * @param pImgeFlag 判断具体文件类型<br> 
		  * @return 检查后的结果<br> 
		  * @throws Exception 
		  */ 
		public static boolean isPicture(String  pInput, 
		                           String pImgeFlag) throws Exception{ 
		  // 文件名称为空的场合 
		  if(!StringUtils.isNotNull(pInput)){ 
		   // 返回不和合法 
		   return false; 
		  } 
		  // 获得文件后缀名 
		  String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, 
		                              pInput.length()); 
		  // 声明图片后缀名数组 
		  String imgeArray [][] = { 
		    {"bmp", "0"}, {"dib", "1"}, {"gif", "2"}, 
		    {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"}, 
		    {"jpg", "6"}, {"png", "7"} ,{"tif", "8"}, 
		    {"tiff", "9"}, {"ico", "10"} 
		  }; 
		  // 遍历名称数组 
		  for(int i = 0; i<imgeArray.length;i++){ 
		   // 判断单个类型文件的场合 
		   if(StringUtils.isNotNull(pImgeFlag) 
		      && imgeArray [i][0].equals(tmpName.toLowerCase()) 
		   && imgeArray [i][1].equals(pImgeFlag)){ 
		    return true; 
		   } 
		   // 判断符合全部类型的场合 
		   if(StringUtils.isNotNull(pImgeFlag) 
		      && imgeArray [i][0].equals(tmpName.toLowerCase())){ 
		    return true; 
		   } 
		  } 
		  return false; 
		} 
	
}
