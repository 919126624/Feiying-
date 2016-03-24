package com.wuyizhiye.base.dao;

import java.util.List;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName BaseDao
 * @Description Dao顶层接口
 * @author li.biao
 * @date 2015-4-1
 */
public interface BaseDao {

	/**
	 * @Title addEntity
	 * @Description 持久化新实体
	 * @param @param <T>
	 * @param @param entity
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int addEntity(T entity);

	/**
	 * @Title addBatch
	 * @Description 批量持久化新实体
	 * @param @param <T>
	 * @param @param entities
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int addBatch(List<T> entities);

	/**
	 * @Title updateEntity
	 * @Description 更新实体
	 * @param @param <T>
	 * @param @param entity
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int updateEntity(T entity);

	/**
	 * @Title updateBatch
	 * @Description 批量更新实体
	 * @param @param <T>
	 * @param @param entities
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int updateBatch(List<T> entities);

	/**
	 * @Title deleteEntity
	 * @Description 删除实体
	 * @param @param <T>
	 * @param @param entity
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int deleteEntity(T entity);

	/**
	 * @Title deleteBatch
	 * @Description 批量删除实体
	 * @param @param <T>
	 * @param @param ids
	 * @param @return
	 * @return int
	 * @throws
	 */
	<T> int deleteBatch(List<String> ids);

	/**
	 * @Title deleteById
	 * @Description 按id删除实体
	 * @param @param id
	 * @param @return
	 * @return int
	 * @throws
	 */
	int deleteById(String id);

	/**
	 * @Title getEntityById
	 * @Description 根据id获取实体
	 * @param @param <T>
	 * @param @param id
	 * @param @return
	 * @return T
	 * @throws
	 */
	<T extends CoreEntity> T getEntityById(String id);

}
