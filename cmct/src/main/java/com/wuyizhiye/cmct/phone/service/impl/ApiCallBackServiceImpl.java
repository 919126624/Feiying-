package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.ApiCallBackDao;
import com.wuyizhiye.cmct.phone.model.ApiCallBack;
import com.wuyizhiye.cmct.phone.service.ApiCallBackService;
import com.wuyizhiye.cmct.phone.util.CtPhoneSession;

/**
 * @ClassName ApiCallBackServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="apiCallBackService")
@Transactional
public class ApiCallBackServiceImpl extends DataEntityService<ApiCallBack> implements ApiCallBackService {
	
	@Autowired
	private ApiCallBackDao apiCallBackDao;
	
	@Override
	protected BaseDao getDao() {
		return apiCallBackDao;
	}

	@Override
	public void executeClean() {
		CtPhoneSession.clearTimeOut() ;
	}	
}