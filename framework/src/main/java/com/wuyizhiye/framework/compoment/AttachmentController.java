package com.wuyizhiye.framework.compoment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName AttachmentController
 * @Description 附件上传
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="framework/attachment/*")
public class AttachmentController extends BaseController {
	
	/**
	 * 附件上传必须指定图片存放相对路径direct(如:person/head - > /usr/local/appData/attachment/person/head)
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public void upload(@RequestParam(value="attachment")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			HttpServletResponse response) throws IOException{
		if(!file.isEmpty()){
			dir = SystemUtil.getCurrentDataCenter() + dir;
			File path = new File(SystemConfig.getParameter("attachment_path") + dir);
			if(!path.exists()){
				path.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			String fileNameBack = fileName.substring(0,fileName.lastIndexOf("."));
			
			fileName = (StringUtils.isEmpty(ordingName)?UUID.randomUUID().toString():ordingName) + fileName.substring(fileName.lastIndexOf("."));
			File img = new File(path.getPath() + "/" + fileName);
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
			getOutputMsg().put("fileName", fileNameBack);
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
}
