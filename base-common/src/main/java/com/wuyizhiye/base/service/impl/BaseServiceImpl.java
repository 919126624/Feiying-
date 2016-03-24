package com.wuyizhiye.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.BaseService;

/**
 * @ClassName BaseServiceImpl
 * @Description service基础实现
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public abstract class BaseServiceImpl<T extends CoreEntity> implements
		BaseService<T> {
	@Autowired
	protected QueryExecutor queryExecutor;

	@Override
	public void addEntity(T entity) {
		getDao().addEntity(entity);
	}

	@Override
	public void addBatch(List<T> entities) {
		getDao().addBatch(entities);
	}

	@Override
	public void updateEntity(T entity) {
		getDao().updateEntity(entity);
	}

	@Override
	public void updateBatch(List<T> entities) {
		getDao().updateBatch(entities);
	}

	@Override
	public void deleteEntity(T entity) {
		getDao().deleteEntity(entity);
	}

	@Override
	public void deleteById(String id) {
		getDao().deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getEntityById(String id) {
		return (T) getDao().getEntityById(id);
	}

	/**
	 * 抽象方法,实现类必须实现该方法,即返回具体业务的dao类
	 * @return
	 */
	protected abstract BaseDao getDao();

	@Override
	public void deleteBatch(List<String> ids) {
		getDao().deleteBatch(ids);
	}
}
