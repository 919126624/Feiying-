package com.wuyizhiye.basedata.bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.bank.dao.BranchDao;
import com.wuyizhiye.basedata.bank.model.Bank;
import com.wuyizhiye.basedata.bank.model.Branch;
import com.wuyizhiye.basedata.bank.service.BankService;
import com.wuyizhiye.basedata.bank.service.BranchService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BranchServiceImpl
 * @Description 网点银行service
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="branchService")
@Transactional
public class BranchServiceImpl extends DataEntityService<Branch> implements BranchService {
	@Autowired
	private BranchDao branchDao;
	@Override
	protected BaseDao getDao() {
		return branchDao;
	}
	
	@Autowired
	private BankService bankService;
	
	@Override
	public void addEntity(Branch entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setDr(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null && !StringUtils.isEmpty(entity.getParent().getId())){
			Branch parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel()+1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.addEntity(entity);
		if(entity.getLevel()==1){
			entity.setId(entity.getId());
			bankService.addEntity(setBank(entity));
		}
	}
	
	@Override
	public void updateEntity(Branch entity) {
		entity.setLongNumber(entity.getNumber());
		if(entity.getParent()!=null){
			Branch parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			super.updateEntity(parent);
		}
		super.updateEntity(entity);
		
		if(entity.getLevel()==1){
			bankService.updateEntity(setBank(entity));
		}
	}
	
	private Bank setBank(Branch entity){
		Bank bank = new Bank();
		if(entity!=null){
			bank.setId(entity.getId());
			bank.setName(entity.getName());
			bank.setDescription(entity.getRemark());
			bank.setNumber(entity.getNumber());
			bank.setSimplePinyin(entity.getNumber());
		}
		return bank;
	}
}
