package com.wuyizhiye.basedata.sql.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.sql.dao.ConnectParamBeanDao;
import com.wuyizhiye.basedata.sql.model.ConnectParamBean;
import com.wuyizhiye.basedata.sql.service.ConnectParamBeanService;

/**
 * @ClassName ConnectParamBeanServiceImpl
 * @Description 职员service
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="connectParamBeanService")
@Transactional
public class ConnectParamBeanServiceImpl extends DataEntityService<ConnectParamBean> implements ConnectParamBeanService {

	@Autowired
	ConnectParamBeanDao connectParamBeanDao;
	
	@Override
	protected BaseDao getDao() {
		return connectParamBeanDao;
	}
	
}
