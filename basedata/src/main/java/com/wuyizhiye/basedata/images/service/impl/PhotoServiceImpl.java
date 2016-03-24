package com.wuyizhiye.basedata.images.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.images.dao.PhotoDao;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PhotoServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="photoService")
@Transactional
public class PhotoServiceImpl extends DataEntityService<Photo> implements
		PhotoService {
	@Autowired
	private PhotoDao photoDao;
	@Override
	protected BaseDao getDao() {
		return photoDao;
	}
	@Override
	public List<Photo> selectByCondition(Map param) {
		return this.photoDao.selectByCondition(param);
	}
	@Override
	public void deletePhotoByAlbumId(String albumId) {
		// TODO Auto-generated method stub
		this.photoDao.deletePhotoByAlbumId(albumId);
	}

}
