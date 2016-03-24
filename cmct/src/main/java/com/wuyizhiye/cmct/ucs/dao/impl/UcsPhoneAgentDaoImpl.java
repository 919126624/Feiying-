package com.wuyizhiye.cmct.ucs.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneAgentDao;

/**
 * @ClassName UcsPhoneAgentDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneAgentDao")
public class UcsPhoneAgentDaoImpl extends BaseDaoImpl implements UcsPhoneAgentDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneAgentDao";
	}
}
