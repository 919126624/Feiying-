package com.wuyizhiye.basedata.attachment.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.attachment.model.Attachment;
import com.wuyizhiye.basedata.attachment.service.AttachmentService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName AttachmentEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/attach/*")
public class AttachmentEditController extends EditController {

	@Autowired
	private AttachmentService attachmentService;
	
	@Override
	protected Class<Attachment> getSubmitClass() {
		return Attachment.class;
	}

	@Override
	protected BaseService<Attachment> getService() {
		
		return attachmentService;
	}

	/**
	 * 上传附件
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
			dir = SystemUtil.getCurrentDataCenter() + dir;
			File path = new File(SystemConfig.getParameter("attachment_path") + dir);
			if(!path.exists()){
				path.mkdirs();
			}
			String fileName = file.getOriginalFilename();
			
			String fileNameBack = fileName.substring(0,fileName.lastIndexOf("."));
			String fileSuffix = fileName.substring(fileName.lastIndexOf(".")+1);
			
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
				Attachment attach = new Attachment();
				attach.setFileType(fileSuffix);
				attach.setFileLength(String.valueOf(img.length()));
				attach.setName(file.getOriginalFilename());
				attach.setPath(dir + "/" + fileName);
				String belong = getString("belong",null);
				attach.setBelong(belong);
				attachmentService.addEntity(attach);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				getOutputMsg().put("path", dir + "/" + fileName);
				getOutputMsg().put("id", attach.getId());
				getOutputMsg().put("fileName", fileNameBack);
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
