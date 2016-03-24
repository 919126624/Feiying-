package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.ApiCallBackDao;

/**
 * @ClassName ApiCallBackDaoImpl
 * @Description 呼叫中心-话伴参数配置 
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="apiCallBackDao")
public class ApiCallBackDaoImpl extends BaseDaoImpl implements ApiCallBackDao {
	
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.ApiCallBackDao";
	}
}
