package com.wuyizhiye.basedata.images.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.images.dao.PhotoAlbumDao;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;

/**
 * @ClassName PhotoAlbumDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="photoAlbumDao")
public class PhotoAlbumDaoImpl extends BaseDaoImpl implements PhotoAlbumDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.images.dao.PhotoAlbumDao";
	}

	@Override
	public List<PhotoAlbum> selectByCondition(Map param) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
	
	/**
	 * 根据belogId删除相册
	 * @param belogId
	 */
   public void deleteByBelogId(String belogId){
		this.getSqlSession().delete(getNameSpace()+".deleteByBelogId", belogId);
   }

}
