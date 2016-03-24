package com.wuyizhiye.cmct.ucs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneMemberService;

/**
 * @ClassName UcsPhoneMemberServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneMemberService")
@Transactional
public class UcsPhoneMemberServiceImpl extends BaseServiceImpl<UcsPhoneMember> implements UcsPhoneMemberService {
	@Autowired
	private UcsPhoneMemberDao ucsPhoneMemberDao;
	@Override
	protected BaseDao getDao() {
		return ucsPhoneMemberDao;
	}
}