package com.wuyizhiye.basedata.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.dao.PrintConfigDao;
import com.wuyizhiye.basedata.code.model.PrintConfig;
import com.wuyizhiye.basedata.code.service.PrintConfigService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PrintConfigServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="printConfigService")
@Transactional
public class PrintConfigServiceImpl extends DataEntityService<PrintConfig> implements PrintConfigService {
	@Autowired
	private PrintConfigDao printConfigDao;
	@Override
	protected BaseDao getDao() {
		return printConfigDao;
	}	
	
	@Override
	public List<PrintConfig> getList(Map<String, Object> param) {
		return printConfigDao.getList(param);
	}
	
}