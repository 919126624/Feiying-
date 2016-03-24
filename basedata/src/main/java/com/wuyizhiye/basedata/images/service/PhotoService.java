package com.wuyizhiye.basedata.images.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.images.model.Photo;

/**
 * @ClassName PhotoService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PhotoService extends BaseService<Photo> {
	/**
	 * 按条件查找图片
	 * @param param
	 * @return
	 */
	List<Photo> selectByCondition(Map param);
	/**
	 * 根据相册id删除下面的所有图片
	 * @author taking.wang
	 * @since 2013-01-29 16:34
	 * @param albumId  相册id
	 */
	void deletePhotoByAlbumId(String albumId);
}
