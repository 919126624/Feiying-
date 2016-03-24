package com.wuyizhiye.base.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName BaseService
 * @Description Service顶层接口
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
@Transactional
public interface BaseService<T extends CoreEntity> {
	/**
	 * 持久化新实体
	 * 
	 * @param entity
	 */
	void addEntity(T entity);

	/**
	 * 批量持久化新实体
	 * 
	 * @param entity
	 *            add by hlf 2013-1-22
	 */
	void addBatch(List<T> entities);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 */
	void updateEntity(T entity);

	/**
	 * 删除实体
	 * 
	 * @param entity
	 */
	void deleteEntity(T entity);

	/**
	 * 按id删除实体
	 * 
	 * @param id
	 */
	void deleteById(String id);

	/**
	 * 根据id批量删除
	 * 
	 * @param ids
	 */
	void deleteBatch(List<String> ids);

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	T getEntityById(String id);

	/**
	 * 批量更新实体
	 * 
	 * @param entities
	 */
	void updateBatch(List<T> entities);

}
