package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.OrgLevelDescDao;

/**
 * @ClassName OrgLevelDescDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="orgLevelDescDao")
public class OrgLevelDescDaoImpl extends BaseDaoImpl implements OrgLevelDescDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.OrgLevelDescDao";
	}
}
