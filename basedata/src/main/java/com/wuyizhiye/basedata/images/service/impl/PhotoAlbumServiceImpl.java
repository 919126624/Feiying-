package com.wuyizhiye.basedata.images.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.images.dao.PhotoAlbumDao;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.basedata.images.service.PhotoAlbumService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PhotoAlbumServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="photoAlbumService")
@Transactional
public class PhotoAlbumServiceImpl extends DataEntityService<PhotoAlbum>
		implements PhotoAlbumService {
	@Autowired
	private PhotoAlbumDao photoAlbumDao;

	@Override
	protected BaseDao getDao() {
		return photoAlbumDao;
	}

}
