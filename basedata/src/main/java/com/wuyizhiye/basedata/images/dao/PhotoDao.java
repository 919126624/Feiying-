package com.wuyizhiye.basedata.images.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.images.model.Photo;

/**
 * @ClassName PhotoDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PhotoDao extends BaseDao {

	/**
	 * 选择性更新实体
	 * @param entity
	 */
	<T> void updateEntitySelective(T entity);
	
	/**
	 * 选择性更新实体
	 * @param entity
	 */
	<T> void updateBatchSelective(List<T> entities);
	
	/**
	 * 按条件查找图片
	 * @param param
	 * @return
	 */
	List<Photo> selectByCondition(Map param);
	
	/**
	 * 按相册删除下面的相片
	 * @author added by taking.wang
	 * @since 2013-01-29 16:35
	 * @param albumId
	 */
	void deletePhotoByAlbumId(String albumId);
}
