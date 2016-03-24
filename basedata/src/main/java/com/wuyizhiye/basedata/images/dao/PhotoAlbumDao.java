package com.wuyizhiye.basedata.images.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;

/**
 * @ClassName PhotoAlbumDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PhotoAlbumDao extends BaseDao {
	/**
	 * 根据条件查相册
	 * @param param
	 * @return
	 */
	List<PhotoAlbum> selectByCondition(Map param);
	
	/**
	 * 根据belogId删除相册
	 * @param belogId
	 */
	void deleteByBelogId(String belogId);
	
}
