package com.wuyizhiye.basedata.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.bank.dao.BankDao;
import com.wuyizhiye.basedata.bank.model.Bank;
import com.wuyizhiye.basedata.bank.service.BankService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BankServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="bankService")
@Transactional
public class BankServiceImpl extends DataEntityService<Bank> implements BankService {
	@Autowired
	private BankDao bankDao;
	@Override
	protected BaseDao getDao() {
		return bankDao;
	}

}
