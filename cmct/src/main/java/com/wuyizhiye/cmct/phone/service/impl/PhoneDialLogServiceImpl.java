package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneDialLogDao;
import com.wuyizhiye.cmct.phone.model.PhoneDialLog;
import com.wuyizhiye.cmct.phone.service.PhoneDialLogService;

/**
 * @ClassName PhoneDialLogServiceImpl
 * @Description 内网平台 呼叫中心日志记录
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialLogService")
public class PhoneDialLogServiceImpl extends DataEntityService<PhoneDialLog> implements PhoneDialLogService {

	@Autowired
	private PhoneDialLogDao phoneDialLogDao ;
	
	@Override
	protected BaseDao getDao() {
		return phoneDialLogDao;
	}
}
