package com.wuyizhiye.basedata.apiCenter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.apiCenter.dao.APIVisitLogDao;
import com.wuyizhiye.basedata.apiCenter.model.APIVisitLog;
import com.wuyizhiye.basedata.apiCenter.service.APIVisitLogService;

/**
 * @ClassName APIVisitLogServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="aPIVisitLogService")
@Transactional
public class APIVisitLogServiceImpl extends BaseServiceImpl<APIVisitLog> implements APIVisitLogService {
	@Autowired
	private APIVisitLogDao aPIVisitLogDao;
	@Override
	protected BaseDao getDao() {
		return aPIVisitLogDao;
	}	
}