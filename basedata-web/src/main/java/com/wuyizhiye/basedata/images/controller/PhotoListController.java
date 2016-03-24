package com.wuyizhiye.basedata.images.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.ImageUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhotoListController
 * @Description Photo列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/photo/*")
public class PhotoListController extends ListController {
	
	private static final Log LOG = LogFactory.getLog(PhotoListController.class);
	
	@Autowired
	private PhotoService photoService;

	@Override
	protected CoreEntity createNewEntity() {
		return new Photo();
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.images.dao.PhotoDao.select";
	}

	@Override
	protected BaseService<Photo> getService() {
		return photoService;
	}
	
	@RequestMapping(value="view")
	public String view(@RequestParam(value="id",required=true)String id,ModelMap model){
		Photo photo = getService().getEntityById(id);
		model.put("data", photo);
		return "basedata/images/photoView";
	}
	
	@Override
	public void delete(String id, HttpServletResponse response) {
		Photo photo = getService().getEntityById(id);
		if(photo!=null && !StringUtils.isEmpty(photo.getPath())){
			String path = photo.getPath();
			List<String> list = new ArrayList<String>();
			if(path.indexOf("size")>-1){
			
				list.add(SystemConfig.getParameter("image_path") + path.replace("size", "origin"));
				
				if(path.indexOf("news")>-1){						
					list.add(SystemConfig.getParameter("image_path") + path.replace("size", "500X300"));			
				}
				//精英风采图片
				else if(path.indexOf("grace")>-1){
					list.add(SystemConfig.getParameter("image_path") + path.replace("size", "500X300"));
				}
			
			}else{
				list.add(SystemConfig.getParameter("image_path") + path);
			}

			try {
				ImageUtil.delImage(list);
			} catch (IOException e) {
				LOG.debug(e.getMessage());
			}
		}
		super.delete(id, response);
	}
	
	@RequestMapping(value="downFile")
	public void downFile(@RequestParam(required=true,value="id")String id, HttpServletResponse response) throws IOException{
		Photo photo = getService().getEntityById(id);
		if (photo != null && !StringUtils.isEmpty(photo.getPath())) {
			String path = photo.getPath();
			if (path.indexOf("size") > -1) {
				path = path.replace("size", "origin");
			}
			File file = new File(SystemConfig.getParameter("image_path") + path);
			// 取得文件名。
			String filename = photo.getName();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(filename.getBytes("gb2312"), "iso8859-1"));
			response.addHeader("Content-Length", "" + file.length());

			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream buff = new BufferedInputStream(fis);
			byte[] b = new byte[1024];// 相当于我们的缓存
			long k = 0;// 该值用于计算当前实际下载了多少字节
			// 从response对象中得到输出流,准备下载
			OutputStream myout = response.getOutputStream();
			// 开始循环下载
			while (k < file.length()) {

				int j = buff.read(b, 0, 1024);
				k += j;

				// 将b中的数据写到客户端的内存
				myout.write(b, 0, j);

			}
			// 将写入到客户端的内存的数据,刷新到磁盘
			myout.flush();
		}
	}
}
