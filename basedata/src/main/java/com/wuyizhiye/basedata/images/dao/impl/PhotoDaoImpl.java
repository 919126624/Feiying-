package com.wuyizhiye.basedata.images.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.images.dao.PhotoDao;
import com.wuyizhiye.basedata.images.model.Photo;

/**
 * @ClassName PhotoDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="photoDao")
public class PhotoDaoImpl extends BaseDaoImpl implements PhotoDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.images.dao.PhotoDao";
	}

	@Override
	public <T> void updateEntitySelective(T entity) {
		getSqlSession().update(getNameSpace()+".updateSelective", entity);
		
	}

	@Override
	public <T> void updateBatchSelective(List<T> entities) {
		for(T entity : entities){
			updateEntitySelective(entity);
		}		
	}

	@Override
	public List<Photo> selectByCondition(Map param) {
		
		return this.getSqlSession().selectList("com.wuyizhiye.basedata.images.dao.PhotoDao.select", param);
	}

	@Override
	public void deletePhotoByAlbumId(String albumId) {
		// TODO Auto-generated method stub
		this.getSqlSession().delete(this.getNameSpace()+".deletePhotoByAlbumId", albumId);
	}
	
	

}
