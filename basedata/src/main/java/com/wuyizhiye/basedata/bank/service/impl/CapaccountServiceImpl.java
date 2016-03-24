package com.wuyizhiye.basedata.bank.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.bank.dao.CapaccountDao;
import com.wuyizhiye.basedata.bank.model.Capaccount;
import com.wuyizhiye.basedata.bank.service.CapaccountService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.OrgUtils;

/**
 * @ClassName CapaccountServiceImpl
 * @Description 资金账户
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="capaccountService")
@Transactional
public class CapaccountServiceImpl extends DataEntityService<Capaccount> implements CapaccountService {
	@Autowired
	private CapaccountDao capaccountDao;
	@Override
	protected BaseDao getDao() {
		return capaccountDao;
	}
	
	
	@Override
	public void addEntity(Capaccount entity) {
		entity.setIsEnable(1);
		if(entity.getIsBranchAccount()==null){
			entity.setIsBranchAccount(0);
		}
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.addEntity(entity);
	}
	
	
	
	@Override
	public void updateEntity(Capaccount entity) {
		if(null!=entity.getOrg()){
			entity.setControlUnit(OrgUtils.getCUByOrg(entity.getOrg().getId()));
		}
		super.updateEntity(entity);
	}


	/**
	 * 得到总部账户
	 * @return
	 */
	@Override
	public List<Capaccount> getGroupCapaccountList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("isEnable", "enable");
		param.put("isBranchAccount", 0);
		param.put("notCashCap", true);
		param.put("inAccountType","BANK");
		param.put("isPaymentsRec",1);
		List<Capaccount> list = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.CapaccountDao.selectIncomePayCapAccount", param,Capaccount.class);
		return list;
	}
	
	@Override
	public List<Capaccount> getGroupAccountList(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("isEnable", "enable");
		param.put("isBranchAccount", 0);
//		param.put("notCashCap", true);
//		param.put("inAccountType","BANK");
		param.put("isPaymentsRec",1);
		List<Capaccount> list = queryExecutor.execQuery("com.wuyizhiye.basedata.bank.CapaccountDao.selectIncomePayCapAccount", param,Capaccount.class);
		return list;
	}
}
