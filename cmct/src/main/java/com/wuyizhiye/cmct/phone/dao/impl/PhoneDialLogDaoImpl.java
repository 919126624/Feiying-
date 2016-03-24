package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDialLogDao;

/**
 * @ClassName PhoneDialLogDaoImpl
 * @Description 呼叫中心人员数据访问接口实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialLogDao")
public class PhoneDialLogDaoImpl extends BaseDaoImpl implements PhoneDialLogDao {
	
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDialLogDao";
	}

}
