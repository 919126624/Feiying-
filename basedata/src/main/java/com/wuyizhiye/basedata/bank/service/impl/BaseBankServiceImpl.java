package com.wuyizhiye.basedata.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.bank.dao.BaseBankDao;
import com.wuyizhiye.basedata.bank.model.BaseBank;
import com.wuyizhiye.basedata.bank.service.BaseBankService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName BaseBankServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="baseBankService")
public class BaseBankServiceImpl extends BaseServiceImpl<BaseBank> implements BaseBankService {

	@Autowired
	private BaseBankDao baseBankDao ;
	
	@Override
	protected BaseDao getDao() {
		return baseBankDao;
	}

	@Override
	public void addEntity(BaseBank entity) {
		entity.setControlUnit(SystemUtil.getCurrentControlUnit());
		super.addEntity(entity);
	}

	@Override
	public void updateEntity(BaseBank entity) {
		entity.setControlUnit(SystemUtil.getCurrentControlUnit());
		super.updateEntity(entity);
	}
}
