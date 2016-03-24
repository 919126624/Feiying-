package com.wuyizhiye.basedata.attachment.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName TemplateController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/template/*")
public class TemplateController extends BaseController {

	/**
	 * 
	 * @param path "/WEB-INF/template/xxx.xls"
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="downFile")
	public void downFile(@RequestParam(required=true,value="path")String path, HttpServletResponse response) throws IOException{
		if(!StringUtils.isEmpty(path)){
			String[] pathParts = path.split("/");
			String realPath = this.getRequest().getSession().getServletContext().getRealPath("");
			for(int i = 0 ; i < pathParts.length ; i ++){
				realPath += File.separator + pathParts[i] ;
			}
			File file = new File(realPath);
			
			// 取得文件名。
			String filename = path.substring(path.lastIndexOf("/")+1,path.length());
			
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"),"iso8859-1"));
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

}
