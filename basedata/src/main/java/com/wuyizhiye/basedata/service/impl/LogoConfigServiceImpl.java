package com.wuyizhiye.basedata.service.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.dao.LogoConfigDao;
import com.wuyizhiye.basedata.images.dao.PhotoAlbumDao;
import com.wuyizhiye.basedata.images.dao.PhotoDao;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.service.LogoConfigService;
import com.wuyizhiye.basedata.shortcut.dao.ShortcutDao;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;

/**
 * @ClassName LogoConfigServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="logoConfigService")
@Transactional
public class LogoConfigServiceImpl extends BaseServiceImpl<LogoConfig> implements
		LogoConfigService {

	@Autowired
	private LogoConfigDao logoConfigDao;
	@Autowired
	private PhotoDao photoDao;
	@Autowired
	private PhotoAlbumDao photoAlbumDao;
	@Autowired
	private ShortcutDao shortcutDao;
	

	@Override
	protected BaseDao getDao() {
		return logoConfigDao;
	}
	
	/**
	 * 根据表设计 先给公告创建相册,将图片关联到相册来关联到公告
	 */
	@Override
	public void addLogoConfig(LogoConfig logoConfig, List<Photo> photoList,List<Shortcut> shortcutList) {
		//ID为空则为新增
		PhotoAlbum palbum = null;
		boolean isnew = false;
		if(StringUtils.isEmpty(logoConfig.getId()))
		{	
			logoConfig.setUUID();
			isnew = true;
			
		}else{		
			
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("belong", logoConfig.getId());
			List<PhotoAlbum> pblist = this.photoAlbumDao.selectByCondition(param);
			if(pblist.size()>0) palbum = pblist.get(0);
		}		
			if(photoList!=null && photoList.size() > 0){
				if(null==palbum){
				palbum = new PhotoAlbum();
				palbum.setBelong(logoConfig.getId());
				palbum.setName("LOGO");
				palbum.setUUID();
				photoAlbumDao.addEntity(palbum);
				}
				for(Photo pp : photoList){
					pp.setAlbum(palbum);
				}				
				photoDao.updateBatchSelective(photoList);
			}
			if(shortcutList!=null && shortcutList.size() > 0){
				for(Shortcut s : shortcutList){
					Shortcut old=shortcutDao.getEntityById(s.getId());
					old.setIdx(s.getIdx());
					shortcutDao.updateEntity(old);
				}				
			}
			
			if(isnew){
				super.addEntity(logoConfig);
			}else{
				this.logoConfigDao.updateEntity(logoConfig);
			}
		
	}

	@Override
	public void deleteLogoConfig(String id) {
		
		this.logoConfigDao.deleteById(id);
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("belong", id);
		List<String> idList = new ArrayList<String>();
		List<String> filelist = new ArrayList<String>();
		
		
		
		List<Photo> plist = this.photoDao.selectByCondition(param);
		
		if(plist.size()>0){
		for(int i=0;i<plist.size();i++){
			idList.add(plist.get(i).getId());
			filelist.add(plist.get(i).getPath());
		}
		this.photoDao.deleteBatch(idList);
		for(int i=0;i<filelist.size();i++){
			File f = new File(SystemConfig.getParameter("image_path")+filelist.get(i));
			if(f.exists()) f.delete();
		}
		}
		
		List<PhotoAlbum> pblist = this.photoAlbumDao.selectByCondition(param);
		idList = new ArrayList<String>();
		if(pblist.size()>0){
			for(int i=0;i<pblist.size();i++){
				idList.add(pblist.get(i).getId());			
			}
			this.photoAlbumDao.deleteBatch(idList);
		}
		 
	}

}
