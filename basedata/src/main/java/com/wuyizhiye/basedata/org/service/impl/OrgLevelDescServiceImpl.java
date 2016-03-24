package com.wuyizhiye.basedata.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.dao.OrgLevelDescDao;
import com.wuyizhiye.basedata.org.model.OrgLevelDesc;
import com.wuyizhiye.basedata.org.service.OrgLevelDescService;

/**
 * @ClassName OrgLevelDescServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="orgLevelDescService")
@Transactional
public class OrgLevelDescServiceImpl extends BaseServiceImpl<OrgLevelDesc> implements OrgLevelDescService {
	@Autowired
	private OrgLevelDescDao orgLevelDescDao;
	@Override
	protected BaseDao getDao() {
		return orgLevelDescDao;
	}	
}