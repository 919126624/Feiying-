package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao;

/**
 * @ClassName PhoneDialRecordDaoImpl
 * @Description 呼叫中心 呼叫记录 数据访问接口实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialRecordDao")
public class PhoneDialRecordDaoImpl extends BaseDaoImpl implements PhoneDialRecordDao {
	
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDialRecordDao";
	}

}
