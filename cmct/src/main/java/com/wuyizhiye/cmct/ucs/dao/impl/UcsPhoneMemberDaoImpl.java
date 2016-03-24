package com.wuyizhiye.cmct.ucs.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao;

/**
 * @ClassName UcsPhoneMemberDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneMemberDao")
public class UcsPhoneMemberDaoImpl extends BaseDaoImpl implements UcsPhoneMemberDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao";
	}

	@Override
	public void deleteByAgentId(String id) {
		getSqlSession().delete(getNameSpace()+".deleteByAgentId",id);
	}
}
