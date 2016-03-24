package com.wuyizhiye.basedata.images.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.common.ImageAttribute;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.ImageUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhotoEditController
 * @Description photo 上传 编辑controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/photo/*")
public class PhotoEditController extends EditController {
	
	Logger logger=Logger.getLogger(PhotoEditController.class);
	
	@Autowired
	private PhotoService photoService;
	@Override
	protected Class<Photo> getSubmitClass() {
		return Photo.class;
	}

	@Override
	protected BaseService<Photo> getService() {
		return photoService;
	}

	/**
	 * 上传图片
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public void upload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="album",required=false)String album,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			dir = SystemUtil.getCurrentDataCenter() + dir;
			final byte[] bytes = file.getBytes();
			final String fileName = file.getOriginalFilename();
			final String path = SystemConfig.getParameter("image_path") + dir;
			final String suffix = fileName.substring(fileName.lastIndexOf("."));
			boolean ispass = true ;
			
			//限制大小  update by li.biao since 2013-12-20
			long fileSize = file.getSize();
			String allowSizeStr = ParamUtils.getParamValue("ALLOW_PHOTO_SIZE") ;
			double allowSize = StringUtils.isEmpty(allowSizeStr) ? 0 : Double.valueOf(allowSizeStr);// 0 无限制 单位M
			if(allowSize != 0d && fileSize > allowSize * 1024 * 1024){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "上传文件超出限制，最大允许"+allowSize+"M");
				ispass = false ;
			}
			
			//允许通过
			if(ispass){
				
				File folder = new File(SystemConfig.getParameter("image_path") + dir);
				if(!folder.exists()){
					folder.mkdirs();
				}
				
				//String fileName = file.getOriginalFilename();
				boolean isAddSuffix = true;//用于判断返回链接时是否增加_size文本,人员时不增加
				String fileNameBack = fileName.substring(0,fileName.lastIndexOf("."));
					
				List<ImageAttribute> list = new ArrayList<ImageAttribute>();
				//width 或  height 属性为空则原图保存		
				ImageAttribute imgattr = new ImageAttribute(null,null,true);
				list.add(imgattr);
				//公告图片
				if(dir.indexOf("news")>-1){						
					imgattr = new ImageAttribute(500,300,true);
					list.add(imgattr);
					imgattr = new ImageAttribute(300,200,true);
					list.add(imgattr);
					imgattr = new ImageAttribute(100,75,true);
					list.add(imgattr);
				}
				//人员图片
				else if(dir.indexOf("person")>-1){
					imgattr = new ImageAttribute(150,150,true,true);
					list.add(imgattr);
					isAddSuffix = false;
				}
				//精英风采图片
				else if(dir.indexOf("grace")>-1){
					imgattr = new ImageAttribute(500,300,true);
					list.add(imgattr);
				}else if(dir.indexOf("know")>-1){
					imgattr = new ImageAttribute(500,300,true);
					list.add(imgattr);
				//银行logo
				}else if(dir.indexOf("bankLogo")>-1){
					imgattr = new ImageAttribute(142,40,true);
					list.add(imgattr);
					isAddSuffix=false;
				}else if(dir.indexOf("blame")>-1){
					imgattr = new ImageAttribute(400,300,true);
					list.add(imgattr);
					imgattr = new ImageAttribute(80,70,true);
					list.add(imgattr);
					imgattr = new ImageAttribute(150,150,true);
					list.add(imgattr);
					imgattr = new ImageAttribute(200,130,true);
					list.add(imgattr);
					isAddSuffix=false;
				}else if(dir.indexOf("signbill")>-1){ //会签单图片
					imgattr = new ImageAttribute(67,98,true);
					list.add(imgattr);
				}
				
				
				String url = ImageUtil.compressSave(bytes,path,suffix,list);
				
				Photo photo = new Photo();
				if(!StringUtils.isEmpty(album)){
					PhotoAlbum pa = new PhotoAlbum();
					pa.setId(album);
					photo.setAlbum(pa);
				}
				photo.setName(file.getOriginalFilename());
				
				photo.setPath(dir + "/"+url+"_size"+suffix);
				
				photoService.addEntity(photo);
				
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				getOutputMsg().put("path", dir + "/"+url+(isAddSuffix?"_size":"")+suffix);
				getOutputMsg().put("id", photo.getId());
				getOutputMsg().put("fileName", fileNameBack);
			}
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * app上传图片
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="appUpload",method=RequestMethod.POST)
	public void appUpload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="size",required=false)String size,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			//定义默认值；
			Integer defaultWidth=300;
			int defaultHeight=300;
			if(StringUtils.isNotNull(size)){//可以为空值的，如果为空值则为默认值；
				try {
					//解析宽度
					String []sizes=size.split("X");
					//这里简单处理；如果尺寸不符合要求，直接使用默认值；
					defaultWidth=Integer.parseInt(sizes[0]);
					defaultHeight=Integer.parseInt(sizes[1]);
				} catch (Exception e) {
					throw new  BusinessException("图片尺寸参数有误请检查！");
				}
			}
			
			boolean ispass = true ;
			
			//限制大小  update by li.biao since 2013-12-20
			long fileSize = file.getSize();
			String allowSizeStr = ParamUtils.getParamValue("ALLOW_PHOTO_SIZE") ;
			double allowSize = StringUtils.isEmpty(allowSizeStr) ? 0 : Double.valueOf(allowSizeStr);// 0 无限制 单位M
			if(allowSize != 0d && fileSize > allowSize * 1024 * 1024){
				ispass = false ;
				throw new BusinessException("上传文件超出限制，最大允许"+allowSize+"M");
			}
			
			//允许通过
			if(ispass){

				final String fileName = file.getOriginalFilename();
				dir = SystemUtil.getCurrentDataCenter()+dir;
				final String path = SystemConfig.getParameter("image_path") + dir;
				final String suffix = fileName.substring(fileName.lastIndexOf("."));
				File folder = new File(SystemConfig.getParameter("image_path") + dir);
				if(!folder.exists()){
					folder.mkdirs();
				}
				String url="";//上传返回路径
				try {
					final byte[] bytes = file.getBytes();
					List<ImageAttribute> list = new ArrayList<ImageAttribute>();	
					//width 或  height 属性为空则原图保存
					ImageAttribute imgattr = new ImageAttribute(null,null,true);
					list.add(imgattr);
					//项目代理-我的项目图片
					if(dir.indexOf("person/head")>-1){
						imgattr = new ImageAttribute(defaultWidth,defaultHeight,true);
						list.add(imgattr);
					}else{
						imgattr = new ImageAttribute(defaultWidth,defaultHeight,true);
						list.add(imgattr);
					}
					url = ImageUtil.compressSave(bytes,path,suffix,list);
				} catch (Exception e) {
					logger.info("上传出事了!清联系管理员");
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				//文件路径跟你设置的图片压缩高宽度有关
				getOutputMsg().put("PATH", dir+ "/"+url+"_size"+suffix);
				//getOutputMsg().put("TARGET_EL", getString("TARGET_EL"));
				response.setContentType("text/html");
				outPrint(response, JSONObject.fromObject(getOutputMsg()));
			}
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 获取项目根目录
	 * @return
	 */
	public String getBasePaths(){
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath()+"/images";
		return path;
	}
	
}
