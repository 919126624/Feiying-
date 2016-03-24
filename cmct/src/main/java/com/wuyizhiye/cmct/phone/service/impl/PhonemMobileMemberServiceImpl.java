package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhonemMobileMemberDao;
import com.wuyizhiye.cmct.phone.model.PhonemMobileMember;
import com.wuyizhiye.cmct.phone.service.PhonemMobileMemberService;
import com.wuyizhiye.cmct.phone.util.PhoneMobileUtil;

/**
 * @ClassName PhonemMobileMemberServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phonemMobileMemberService")
@Transactional
public class PhonemMobileMemberServiceImpl extends DataEntityService<PhonemMobileMember> implements PhonemMobileMemberService {
	@Autowired
	private PhonemMobileMemberDao phonemMobileMemberDao;
	@Override
	protected BaseDao getDao() {
		return phonemMobileMemberDao;
	}
	@Override
	public void deleteEntity(PhonemMobileMember entity) {
		PhoneMobileUtil.removeMobileMember(entity);
		super.deleteEntity(entity);
	}	
}