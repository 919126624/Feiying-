package com.wuyizhiye.basedata.topic.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.topic.enums.TopicStatusEnum;
import com.wuyizhiye.basedata.topic.model.Topic;
import com.wuyizhiye.basedata.topic.service.TopicService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName TopicNewEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/topicNew/*")
public class TopicNewEditController extends EditController {
	@Autowired
	private TopicService topicService;
	@Autowired
	private PhotoAlbumService photoAlbumService;
	@Autowired
	private PhotoService photoService;
	
	@Override
	protected Class<Topic> getSubmitClass() {
		return Topic.class;
	}

	@Override
	protected BaseService<Topic> getService() {
		return topicService;
	}
	
	@RequestMapping(value="updateTopic")
	public void updateTopicStatus(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		Topic data = this.topicService.getEntityById(getString("id"));
		data.setSaq(new Integer(getString("saq")));
		data.setStatus(TopicStatusEnum.valueOf(getString("status")));
		if(validate(data)){
			if(data instanceof CoreEntity){
				getService().updateEntity(data);
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String edit_viewStatus = this.getString("edit_viewStatus");
		Topic data = (Topic)getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId()) || !"EDIT".equalsIgnoreCase(edit_viewStatus)){
					data.setStatus(TopicStatusEnum.STATE_1);
					data.setDataType("NEW");
					getService().addEntity(data);
				}else{
					getService().updateEntity(data);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected Object getSubmitEntity() {
		Topic topic = (Topic) super.getSubmitEntity();
		if(StringUtils.isEmpty(topic.getId())){
			topic.setStatus(TopicStatusEnum.STATE_1);
		}
		return topic;
	}
	
	@Override
	protected boolean validate(Object data) {
//		Topic topic = (Topic) data;
//		if(topic.getBelongFunction()==null || StringUtils.isEmpty(topic.getBelongFunction().getId())){
//			getOutputMsg().put("MSG", "功能不能为空");
//			return false;
//		}
//		if(topic.getDealPerson()==null || StringUtils.isEmpty(topic.getDealPerson().getId())){
//			getOutputMsg().put("MSG", "处理人不能为空");
//			return false;
//		}
//		return super.validate(data);
		return true;
	}
	
	/**
	 * 上传图片
	 * @param file
	 * @param dir
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value="uploadImage",method=RequestMethod.POST)
	public void upload(@RequestParam(value="image")MultipartFile file,
			@RequestParam(value="fileName",required=false)String ordingName,
			@RequestParam(value="direct",required=true)String dir,
			@RequestParam(value="belong",required=false)String belong,
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
				if(!StringUtils.isEmpty(belong)){
					Photo photo = addPhotoToAlbum(belong, file.getOriginalFilename(), dir + "/" + fileName);
					getOutputMsg().put("id", photo.getId());
				}
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "上传文件成功");
				getOutputMsg().put("path", dir + "/" + fileName);
				
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "上传文件为空");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	private Photo addPhotoToAlbum(String topicId,String photoName,String path){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("belong", topicId);
		param.put("name", "附图");
		List<PhotoAlbum> albums = queryExecutor.execQuery("com.wuyizhiye.basedata.images.dao.PhotoAlbumDao.select", param , PhotoAlbum.class);
		PhotoAlbum album;
		if(albums.size()>0){
			album = albums.get(0);
		}else{
			album = new PhotoAlbum();
			album.setId(UUID.randomUUID().toString());
			album.setName("附图");
			album.setBelong(topicId);
			photoAlbumService.addEntity(album);
		}
		Photo photo = new Photo();
		photo.setAlbum(album);
		photo.setName(photoName);
		photo.setPath(path);
		photoService.addEntity(photo);
		return photo;
	}
}
