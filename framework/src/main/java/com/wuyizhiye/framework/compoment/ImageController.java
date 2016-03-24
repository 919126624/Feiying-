package com.wuyizhiye.framework.compoment;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import Decoder.BASE64Decoder;

import com.wuyizhiye.base.common.ImageAttribute;
import com.wuyizhiye.base.util.ImageEachCompressUtill;
import com.wuyizhiye.base.util.ImageUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.TwoDimensionCode;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName ImageController
 * @Description 图片处理Controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="framework/images/*")
public class ImageController extends BaseController {
	
	/**
	 * 图片上传时name必须为image,必须指定图片存放相对路径direct(如:person/head - > /usr/local/appData/images/person/head)
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public void upload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			dir = SystemUtil.getCurrentDataCenter()+dir;
			File path = new File(SystemConfig.getParameter("image_path") + dir);
			if(!path.exists()){
				path.mkdirs();
			}
			
			String fileName = file.getOriginalFilename();
			
			fileName = (StringUtils.isEmpty(ordingName)?UUID.randomUUID().toString():ordingName) + fileName.substring(fileName.lastIndexOf("."));
			File img = new File(path.getPath() + "/" + fileName);
			if(img.exists()){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "该文件己存在");
			}else{
				img.createNewFile();
				FileOutputStream fos = new FileOutputStream(img);
				InputStream is = file.getInputStream();
				byte[] buff = new byte[4096];//缓存4k
				int len = 0;
				while((len=is.read(buff))>0){
					fos.write(buff, 0, len);
				}
				is.close();
				fos.flush();
				fos.close();
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				getOutputMsg().put("PATH", dir + "/" + fileName);
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 图片上传时name必须为image,必须指定图片存放相对路径direct(如:person/head - > /usr/local/appData/images/person/head)
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@RequestMapping(value="compressUpload",method=RequestMethod.POST)
	public void compressUpload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			HttpServletResponse response) throws IOException, InterruptedException, ExecutionException{
		
		final byte[] bytes = file.getBytes();
		final String fileName = file.getOriginalFilename();
		dir = SystemUtil.getCurrentDataCenter()+dir;
		final String path = SystemConfig.getParameter("image_path") + dir;
		final String suffix = fileName.substring(fileName.lastIndexOf("."));
		File folder = new File(SystemConfig.getParameter("image_path") + dir);
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		List<ImageAttribute> list = new ArrayList<ImageAttribute>();	
		//width 或  height 属性为空则原图保存
		ImageAttribute imgattr = new ImageAttribute(null,null,true);
		list.add(imgattr);
		//项目代理-我的项目图片
		if(dir.indexOf("myProject")>-1){
			imgattr = new ImageAttribute(75,75,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(900,600,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(300,200,true);
			list.add(imgattr);
		}
		//
		else if(dir.indexOf("logPath")>-1){
			//start 
			imgattr = new ImageAttribute(104,104,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(135,135,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(260,275,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(210,215,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(684,243,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(720,326,true);
			list.add(imgattr);
			
			imgattr = new ImageAttribute(104,104,true);
			list.add(imgattr);
			//end
			imgattr = new ImageAttribute(240,220,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(360,330,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(500,285,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(598,220,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(600,300,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}
		else if(dir.indexOf("PIC4GOODS")>-1||dir.indexOf("PIC4ACTIVITY")>-1){
			imgattr = new ImageAttribute(720,262,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(600,400,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}

		//地产物业-我的项目图片
		else if(dir.indexOf("invitemerchant")>-1){
			imgattr = new ImageAttribute(75,75,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(900,600,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(300,200,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}
				
		//项目新闻图片
		else if(dir.indexOf("projectNews")>-1){
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}
		//微博图片
		else if(dir.indexOf("microblog")>-1){
			imgattr = new ImageAttribute(100,100,true);
			list.add(imgattr);
		}
		//微博相册图片
		else if(dir.indexOf("blog/photo")>-1){
			imgattr = new ImageAttribute(100,75,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(500,375,true);
			list.add(imgattr);
		}else if(dir.indexOf("billReport")>-1){
			//开单战报
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}else if(dir.indexOf("marketList")>-1){
			//开单战报
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
		}//车险资料
		else if(dir.indexOf("ebcar/insurance")>-1){
			imgattr = new ImageAttribute(88,58,true);
			list.add(imgattr);
		}else{
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(350,350,true);
			list.add(imgattr);
			imgattr = new ImageAttribute(720,300,true);
			list.add(imgattr);	
			imgattr = new ImageAttribute(270,180,true);
			list.add(imgattr);
		}
		
			
		String url = ImageUtil.compressSave(bytes,path,suffix,list);
		
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "上传文件成功");
		//文件路径跟你设置的图片压缩高宽度有关
		getOutputMsg().put("PATH", dir + "/"+url+"_size"+suffix);
		getOutputMsg().put("TARGET_EL", getString("TARGET_EL")); 
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	//遍历图片文件夹 压缩图片
	@RequestMapping(value="compressImage")
	public void compressImage(HttpServletResponse response){
		String dir = getString("compressDirect");
		int width = Integer.parseInt(getString("compressWidth"));
		int height = Integer.parseInt(getString("compressHeight"));
		//dir = SystemUtil.getCurrentDataCenter()+dir;
		final String path = SystemConfig.getParameter("image_path") + dir;
		String msg = ImageEachCompressUtill.compressFiles(path, width, height);
		if(msg==null){
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "压缩文件成功");}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG",msg );
		}
		
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/*public String doUpload(final byte[] bytes,final String path,final String suffix,List<HashMap<String,Object>> list) throws InterruptedException, ExecutionException{
		
		List<Future<String>> futures = new ArrayList<Future<String>>();
		final String fileId = UUID.randomUUID().toString();
		for(final HashMap<String,Object> hmap : list) {
			Future<String> future = executor.submit(new Runnable() {
				public void run() {
					byte[] compressedImage = ImageUtil.resize(bytes, null==hmap.get("width")?null:Integer.parseInt(hmap.get("width").toString()), null==hmap.get("height")?null:Integer.parseInt(hmap.get("height").toString()), Boolean.parseBoolean(hmap.get("equalRatio").toString()));
					
					String tmpname = fileId+((null==hmap.get("width")||null==hmap.get("height"))?"":("_"+hmap.get("width")+"X"+hmap.get("height")))+suffix;
					
					try {
						ImageUtil.saveImage(compressedImage, path+"//"+tmpname);
					} catch (IOException e) {					
						e.printStackTrace();
					}
					
				}
			}, "done");
			futures.add(future);
		}
		
		for(Future<String> future : futures) {
			if(future.get() != null) {
				continue;
			}
		}
		return fileId;
	}*/
	
	@RequestMapping(value="base64compress",method=RequestMethod.POST)
	public void base64compress(@RequestParam(value="imagedata")String imagedata,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			HttpServletResponse response) throws IOException{
		if(StringUtils.isNotNull(imagedata)){
			dir = SystemUtil.getCurrentDataCenter()+dir;
			
			
			BASE64Decoder decoder = new BASE64Decoder();		
			byte[] bytes = decoder.decodeBuffer(imagedata);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
				bytes[i] += 256;
				}
			}	
			String fileName = ordingName;
			String path = SystemConfig.getParameter("image_path") + dir;
			String suffix = "";
			if(fileName.lastIndexOf(".")>0)
				suffix = fileName.substring(fileName.lastIndexOf("."));
			else suffix = ".jpg";
			fileName = UUID.randomUUID().toString() + suffix;	
			//String image_path = "images/";
			File folder = new File(SystemConfig.getParameter("image_path") + dir);
			if(!folder.exists()){
				folder.mkdirs();
			}
			
			List<ImageAttribute> list = new ArrayList<ImageAttribute>();	
			//width 或  height 属性为空则原图保存
			ImageAttribute imgattr = new ImageAttribute(null,null,true);
			list.add(imgattr);	
			/*imgattr = new ImageAttribute(88,58,true);
			list.add(imgattr);*/	
			if(dir.indexOf("marketList")>-1){
			imgattr = new ImageAttribute(150,100,true);
			list.add(imgattr);
			}else if(dir.indexOf("billReport")>-1){
				//开单战报
				imgattr = new ImageAttribute(150,100,true);
				list.add(imgattr);
			}else if(dir.indexOf("room/images")>-1){
				//盘源详情
				imgattr= new ImageAttribute(400,300,true);
				list.add(imgattr);
			}
			String url = ImageUtil.compressSave(bytes,path,suffix,list);
			String imagePath = dir + "/"+url+"_size"+suffix;												

			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "上传文件成功");
			getOutputMsg().put("PATH", imagePath);
			
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 旋转图片
	 * @param response
	 */
	@RequestMapping(value="imgRotate")
	public synchronized void imgRotate(HttpServletResponse response) {
		try{
				String path=SystemConfig.getParameter("image_path") +getString("path");
				String suffix = path.substring(path.lastIndexOf(".")+1,path.length());  
				path=path.replace("size", "origin");
				int degree=getInt("degree"); 
				 
		        BufferedImage src_origin = ImageIO.read(new File(path));  
		        BufferedImage des_origin  = this.rotateImage(src_origin, degree);  
		   //     ByteArrayOutputStream os=new ByteArrayOutputStream();
		        
		        ImageIO.write(des_origin, suffix, new File(path));
     
		    /*    byte[] min_Bytes = os.toByteArray(); 
		        
				FileOutputStream min_fos = new FileOutputStream(new File(path));
				
				ByteArrayInputStream bi = new ByteArrayInputStream(min_Bytes);		
				byte[] buff = new byte[4096];//缓存4k
				int len = 0;
				while((len=bi.read(buff))>0){
					min_fos.write(buff, 0, len);
				}		
				min_fos.write(min_Bytes);
				min_fos.flush();
				min_fos.close();*/
		        
		        
		         String pageType=getString("pageType"); 
		        if("insurance".equals(pageType)){  
					path=path.replace("origin", "88X58");
				}
				
			    BufferedImage src = ImageIO.read(new File(path));  
			    BufferedImage origin_des = this.rotateImage(src,degree);  
			    /*     ByteArrayOutputStream origin_bos= new ByteArrayOutputStream(); */
		        
		        ImageIO.write(origin_des, suffix, new File(path) );  
/* 		        
		         byte[] origin_Bytes = origin_bos.toByteArray(); 
				
		        FileOutputStream origin_fos = new FileOutputStream(new File(path));
		        
				ByteArrayInputStream origin_bi = new ByteArrayInputStream(origin_Bytes);		
				byte[] origin_buff = new byte[4096];//缓存4k
				int origin_len = 0;
				while((origin_len=origin_bi.read(origin_buff))>0){
					origin_fos.write(origin_buff, 0, origin_len);
				}		
				origin_fos.write(origin_Bytes);
				origin_fos.flush();
				origin_fos.close();  */
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
     * 旋转图片为指定角度
     * 
     * @param bufferedimage
     *            目标图像
     * @param degree
     *            旋转角度
     * @return
     */
    public  BufferedImage rotateImage( BufferedImage bufferedimage,
              int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        degree=degree<0?360+degree:degree;
        (graphics2d = (img = new BufferedImage(w, h, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    @RequestMapping(value="outputimage")
	public void outputimage(@RequestParam(value="url",required=true)String url,HttpServletResponse response) throws IOException{
		File file = new File(SystemConfig.getParameter("image_path") + url);
		 String filename = file.getName();// 获取日志文件名称
		    InputStream fis = new BufferedInputStream(new FileInputStream(file));
		    byte[] buffer = new byte[fis.available()];
		    fis.read(buffer);
		    fis.close();
		    response.reset();
		    // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
		    response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1"));
		    response.addHeader("Content-Length", "" + file.length());
		    OutputStream os = new BufferedOutputStream(response.getOutputStream());
		    response.setContentType("application/octet-stream");
		    os.write(buffer);// 输出文件
		    os.flush();
		    os.close();
	}
    
    @RequestMapping(value="generateQR")
	public void generateQR(@RequestParam(required=true,value="encoderURL")String encoderURL,HttpServletResponse response) throws IOException{			
		String imagePath =null;
		//imgsize设置二维码图片生成
		String size = this.getString("imgsize");
		imagePath = encoderTwoDimensionCode(encoderURL.replaceAll("\\s*", ""),size);
		
		
		//imgwidth设置二维码显示宽度,默认宽度高度一致
		File file = null;
		String imgwidth = this.getString("imgwidth");
		String imgheight = this.getString("imgheight");
		String imgfill = this.getString("imgfill");
		if(StringUtils.isNotNull(imgwidth)){
			int w = Integer.parseInt(imgwidth);
			int h = w;
			if(StringUtils.isNotNull(imgheight)){
				h = Integer.parseInt(imgheight);
			}
			String imgtarget = imagePath.substring(0, imagePath.lastIndexOf("."))+"_target.png";
			if(StringUtils.isEmpty(imgfill)){
				ImageUtil.enlarge(imagePath, imgtarget, w, h, false);
			}else{
				ImageUtil.fillimg(imagePath, imgtarget, w, h);
			}
			file = new File(imgtarget);
		}else{
			file = new File(imagePath);
		}
		 FileInputStream inputStream = new FileInputStream(file);
	        byte[] data = new byte[(int)file.length()];
	        int length = inputStream.read(data);
	        inputStream.close();
	        response.setContentType("image/png");
	        OutputStream stream = response.getOutputStream();
	        stream.write(data);
	        stream.flush();
	        stream.close();
	}
    
  //生成二维码
  	public String encoderTwoDimensionCode(String encoderURL,String imgsize){
  		String encoderContent = encoderURL.replaceAll("\\s*", "");  
  		String dir ="TwoDimensionCode";
  		Properties props=System.getProperties(); //系统属性
  		dir = props.getProperty("java.io.tmpdir")+File.separator+dir;
  		File path = new File(dir);
  		if(!path.exists()){
  		      path.mkdirs();
  		}		
  		String imgName = "qrcode"+System.currentTimeMillis()+".png";
  		String imgPath =path.getPath()+File.separator +imgName;  
  		TwoDimensionCode handler = TwoDimensionCode.getInstatnce(); 
  		if(StringUtils.isEmpty(imgsize)){
  			handler.encoderQRCode(encoderContent, imgPath);
  		}else{
  			handler.encoderQRCode(encoderContent, imgPath,"png",Integer.parseInt(imgsize));
  		}
  		return imgPath;
  	}
    
}
