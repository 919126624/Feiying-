package com.wuyizhiye.basedata.apiCenter.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;
import com.wuyizhiye.basedata.apiCenter.service.APIParameterService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName APIParameterServiceImpl
 * @Description 接口参数service实现类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "apiParameterService")
@Transactional
public class APIParameterServiceImpl extends DataEntityService<APIParameter>
		implements APIParameterService {
	
	@Autowired
	private APIParameterDao apiParameterDao;
	
	@Override
	protected BaseDao getDao() {
		return apiParameterDao;
	}
	@Override
	public List<APIParameter> getList(Map<String, Object> param) {
		return apiParameterDao.getList(param);
	}
}
