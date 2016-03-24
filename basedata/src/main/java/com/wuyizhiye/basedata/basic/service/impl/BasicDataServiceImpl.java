package com.wuyizhiye.basedata.basic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.basic.dao.BasicDataDao;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.basic.service.BasicDataService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BasicDataServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basicDataService")
@Transactional
public class BasicDataServiceImpl extends DataEntityService<BasicData>
		implements BasicDataService {
	@Autowired
	private BasicDataDao basicDataDao;
	@Override
	protected BaseDao getDao() {
		return basicDataDao;
	}
	@Override
	public List<BasicData> getBasicDataList(Map<String, Object> param) {
		return basicDataDao.getBasicDataList(param);
	}
	@Override
	public BasicData getEntityByNumber(String number) {
		// TODO Auto-generated method stub
		return this.basicDataDao.getEntityByNumber(number);
	}

}
