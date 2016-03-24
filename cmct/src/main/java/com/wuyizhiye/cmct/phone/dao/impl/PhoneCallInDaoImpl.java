package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCallInDao;

/**
 * @ClassName PhoneCallInDaoImpl
 * @Description 呼叫中心-话伴参数配置 
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCallInDao")
public class PhoneCallInDaoImpl extends BaseDaoImpl implements PhoneCallInDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneCallInDao";
	}
}
