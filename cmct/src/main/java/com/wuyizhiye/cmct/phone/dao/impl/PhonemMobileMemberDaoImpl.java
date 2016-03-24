package com.wuyizhiye.cmct.phone.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.phone.dao.PhonemMobileMemberDao;

/**
 * @ClassName PhonemMobileMemberDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phonemMobileMemberDao")
public class PhonemMobileMemberDaoImpl extends BaseDaoImpl implements PhonemMobileMemberDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhonemMobileMemberDao";
	}
}
