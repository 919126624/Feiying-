package com.wuyizhiye.basedata.attachment.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.attachment.model.Attachment;

/**
 * @ClassName AttachmentDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface AttachmentDao extends BaseDao {
	 
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
	 * 按条件查找附件
	 * @param param
	 * @return
	 */
	List<Attachment> selectByCondition(Map param);
	
}
