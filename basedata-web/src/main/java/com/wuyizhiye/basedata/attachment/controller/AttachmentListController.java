package com.wuyizhiye.basedata.attachment.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.attachment.model.Attachment;
import com.wuyizhiye.basedata.attachment.service.AttachmentService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName AttachmentListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/attach/*")
public class AttachmentListController extends ListController {

	@Autowired
	private AttachmentService attachmentService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.attachment.dao.AttachmentDao.select";
	}

	@Override
	public void delete(String id, HttpServletResponse response) {
		Attachment attach = getService().getEntityById(id);
		if(attach!=null && !StringUtils.isEmpty(attach.getPath())){
			File file = new File(SystemConfig.getParameter("attachment_path") + attach.getPath());
			if(file.exists()){
				file.delete();
			}
		}
		super.delete(id, response);
	}
	
	@Override
	protected BaseService<Attachment> getService() {
		
		return attachmentService;
	}
	@RequestMapping(value="downFile")
	public void downFile(@RequestParam(required=true,value="id")String id, HttpServletResponse response) throws IOException{
		Attachment attach = getService().getEntityById(id);
		if(attach!=null && !StringUtils.isEmpty(attach.getPath())){
			File file = new File(SystemConfig.getParameter("attachment_path") + attach.getPath());
			// 取得文件名。
			String filename = attach.getName();		
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"),"iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			
			   FileInputStream fis=new FileInputStream(file);
		        BufferedInputStream buff=new BufferedInputStream(fis);

		        byte [] b=new byte[1024];//相当于我们的缓存

		        long k=0;//该值用于计算当前实际下载了多少字节

		        //从response对象中得到输出流,准备下载

		        OutputStream myout=response.getOutputStream();

		        //开始循环下载

		        while(k<file.length()){

		            int j=buff.read(b,0,1024);
		            k+=j;

		            //将b中的数据写到客户端的内存
		            myout.write(b,0,j);

		        }

		        //将写入到客户端的内存的数据,刷新到磁盘
		        myout.flush();

		}

	}
	
	/**
	 * 下载指定对象的全部附件
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "downAllFile")
	public void downAllFile(
			@RequestParam(value = "belong", required = true) String belong,
			HttpServletResponse response) throws IOException {

		Map<String, Object> attParam = new HashMap<String, Object>();
		attParam.put("belong", belong);
		List<Attachment> attList = queryExecutor.execQuery("com.wuyizhiye.basedata.attachment.dao.AttachmentDao.select", attParam, Attachment.class);
		
		List<Attachment> newAttList = new ArrayList<Attachment>();
		if(attList!=null && attList.size() > 0){
			for(Attachment att : attList){
				if(!StringUtils.isEmpty(att.getPath()) && !"undefined".equals(att.getPath())){
					newAttList.add(att);
				}
			}
		}
		
		String zipName=getString("zipName");
		response.addHeader("Content-Disposition",
				"attachment;filename="+new String(this.getZipFilename(zipName).getBytes("gb2312"),"iso8859-1"));
		
		// 从response对象中得到输出流,准备下载
		OutputStream myout = response.getOutputStream();
		ZipOutputStream zos = new ZipOutputStream(myout);
		
		if (newAttList != null && newAttList.size() > 0) {
			File[] files = new File[newAttList.size()];
			String[] names = new String[newAttList.size()];
			for(int i = 0 ; i < newAttList.size() ; i ++ ){
				try{
					Attachment att = newAttList.get(i);
					
					String originPath = att.getPath() == null ? "" : att
							.getPath().replace("size", "origin");
					files[i] = new File(SystemConfig.getParameter("attachment_path")
							+ originPath);
					names[i] = att.getName();
				}catch (Exception e) {
					names[i] = "undefined.jpg";
					continue ;
				}
			}
			zipFile(files, names, zos);     
		    zos.flush();     
		    zos.close(); 
		}
	}
	private String getZipFilename(String zipName) {
		Date date = new Date();
		String s = (StringUtils.isEmpty(zipName)?date.getTime():zipName) + ".zip";
		return s;
	}
	private void zipFile(File[] subs, String[] names, ZipOutputStream zos)
		throws IOException {
			for (int i = 0; i < subs.length; i++) {
				File f = subs[i];
				zos.putNextEntry(new ZipEntry(names[i]));
				zos.setEncoding("GB2312");  
				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[1024];
				int r = 0;
				while ((r = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, r);
				}
				fis.close();
			}
	}

}
