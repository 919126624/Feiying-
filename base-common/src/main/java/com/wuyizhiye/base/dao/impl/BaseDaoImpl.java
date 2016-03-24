package com.wuyizhiye.base.dao.impl;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName BaseDaoImpl
 * @Description Dao基础实现
 * @author li.biao
 * @date 2015-4-1
 */
public abstract class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao {
	
	@Autowired
	protected SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	/**
	 * @Description 抽象方法,返回具体业务对应的mybatis 命名空间
	 * @return
	 */
	protected abstract String getNameSpace();
	
	@Override
	public <T> int addEntity(T entity) {
		if(entity instanceof CoreEntity && StringUtils.isEmpty(((CoreEntity)entity).getId())){
			((CoreEntity)entity).setId(UUID.randomUUID().toString());
		}
		return getSqlSession().insert(getNameSpace()+".insert", entity);
	}
	
	@Override
	public <T> int updateEntity(T entity) {
		return getSqlSession().update(getNameSpace()+".update", entity);
	}
	
	@Override
	public <T> int deleteEntity(T entity) {
		if(entity instanceof CoreEntity){
			return getSqlSession().delete(getNameSpace()+".delete", ((CoreEntity)entity).getId());
		}else{
			return getSqlSession().delete(getNameSpace()+".delete", entity);
		}
	}
	
	@Override
	public int deleteById(String id) {
		return deleteEntity(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends CoreEntity> T getEntityById(String id) {
		return (T) getSqlSession().selectOne(getNameSpace()+".getById", id);
	}
	
	@Override
	public <T> int addBatch(List<T> entities) {
		int count = 0;
		for(T entity : entities){
			count = count + addEntity(entity);
		}
		return count;
	}
	
	@Override
	public <T> int updateBatch(List<T> entities) {
		int count = 0;
		for(T entity : entities){
			count = count + updateEntity(entity);
		}
		return count;
	}
	
	@Override
	public <T> int deleteBatch(List<String> ids) {
		int count = 0;
		for(String id : ids){
			count = count + deleteById(id);
		}
		return count;
	}
	
}
