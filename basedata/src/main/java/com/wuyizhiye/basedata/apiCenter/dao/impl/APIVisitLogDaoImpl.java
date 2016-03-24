package com.wuyizhiye.basedata.apiCenter.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.apiCenter.dao.APIVisitLogDao;
import com.wuyizhiye.basedata.apiCenter.model.APIVisitLog;

/**
 * @ClassName APIVisitLogDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="aPIVisitLogDao")
public class APIVisitLogDaoImpl extends BaseDaoImpl implements APIVisitLogDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.apiCenter.dao.APIVisitLogDao";
	}
	
	@Override
	public <T> int addEntity(T entity) {
		APIVisitLog log = (APIVisitLog) entity ;
		if(log != null ){
			log.setSuffix(StringUtils.getSuffixStr(log.getVisitTime() == null ? (new Date()) : log.getVisitTime()));
		}
		if(StringUtils.isEmpty(log.getId())){
			log.setId(UUID.randomUUID().toString());
		}
		return getSqlSession().insert(getNameSpace()+".insert", log);
	}
	@Override
	public <T> int updateEntity(T entity) {
		APIVisitLog log = (APIVisitLog) entity ;
		if(log != null ){
			log.setSuffix(StringUtils.getSuffixStr(log.getVisitTime() == null ? (new Date()) : log.getVisitTime()));
		}
		return getSqlSession().update(getNameSpace()+".update", log);
	}
	@Override
	public <T> int deleteEntity(T entity) {
		APIVisitLog log = (APIVisitLog) entity ;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", log.getId());
		param.put("suffix", StringUtils.getSuffixStr(log.getVisitTime() == null ? (new Date()) : log.getVisitTime()));
		return getSqlSession().delete(getNameSpace()+".delete", param);
	}
	
}
