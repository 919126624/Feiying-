package com.wuyizhiye.basedata.param.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.param.dao.ParamLinesDao;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.param.service.ParamLinesService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName ParamLinesServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="paramLinesService")
@Transactional
public class ParamLinesServiceImpl extends DataEntityService<ParamLines> implements ParamLinesService {
	@Autowired
	private ParamLinesDao paramLinesDao;
	@Override
	protected BaseDao getDao() {
		return paramLinesDao;
	}	
	
	
	@Override
	public void addEntity(ParamLines entity) {
		super.addEntity(entity);
		
		
	}
	
	@Override
	public void updateEntity(ParamLines entity) {
		super.updateEntity(entity);
	}


	@Override
	public ParamLines getOneParamLines(Map<String, Object> map) {
		return paramLinesDao.getOneParamLines(map);
	}
}