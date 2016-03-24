package com.wuyizhiye.basedata.person.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao;
import com.wuyizhiye.basedata.person.model.BdPositionHistoryBill;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryService;

/**
 * @ClassName PersonPositionHistoryServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositionHistoryService")
public class PersonPositionHistoryServiceImpl extends BaseServiceImpl<PersonPositionHistory> implements PersonPositionHistoryService{

	 @Resource
	 private PersonPositionHistoryDao personPositionHistoryDao;
	 
	public void insertPositionHistory(PersonPositionHistory pstHis){
		personPositionHistoryDao.insertPositionHistory(pstHis);
	}
	
	public void updatePositionHistorySelective(PersonPositionHistory pstHis,PersonPositionHistory pstHisParam){
		personPositionHistoryDao.updatePositionHistorySelective(pstHis, pstHisParam);
	}
	
	public void deletePositionHistoryById(String personPositionHistoryId){
		 
		personPositionHistoryDao.deletePositionHistoryById(personPositionHistoryId);
	}
	
	public void deletePositionHistorySelective(PersonPositionHistory pstHisParam){
		personPositionHistoryDao.deletePositionHistorySelective(pstHisParam);
	}
	
	public void updateByPrimaryKeySelective(PersonPositionHistory pstHis){
		personPositionHistoryDao.updateByPrimaryKeySelective(pstHis);
	}
	
	public void updateByPrimaryKey(PersonPositionHistory pstHis){
		personPositionHistoryDao.updateByPrimaryKey(pstHis);
	}

	@Override
	protected BaseDao getDao() {
		return personPositionHistoryDao;
	}

	@Override
	public List<PersonPositionHistory> getPersonOnDutyRecord(String personId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("personId", personId);
		params.put("orderByClause", "his.feffectdate,(case when his.FEXPIRYDATE is null then 1 else 0 end),FEXPIRYDATE ");
		return this.personPositionHistoryDao.getPositionHistory(params);
	}
	
	/**
	 * 根据员工ID 查询所有任职历史记录
	 * @param personId
	 * @return
	 */
	public List<PersonPositionHistory> getAllPersonPositionHistory(String personId){
		Map<String,Object> params  = new HashMap<String, Object>();
		params.put("personId", personId);
		params.put("orderByClause", "FEFFECTDATE asc ,(case when FEXPIRYDATE is null then 1 else 0 end) asc ,FEXPIRYDATE asc");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		return phisList;
	}
	
	/**
	 * 根据员工ID 查询最后一条任职历史记录
	 * @param personId
	 * @param primary
	 * @return
	 */
	public PersonPositionHistory getLastPersonPositionHistory(String personId,boolean primary){
		Map<String,Object> params  = new HashMap<String, Object>();
		if(primary){
		 params.put("primary", 1);
		}
		params.put("personId", personId);
		params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc ,FEXPIRYDATE desc ");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		if(phisList==null || phisList.size()<1){
			return null ;
		}
		return phisList.get(0);
	}
	
	/**
	 * 根据调职申请单 维护任职历史失效日期
	 * @param bill
	 * @return
	 */
	public String updatePositionHistoryByBill(BdPositionHistoryBill bill){
		String resultMsg = "SUCC";
		Map<String,Object> params  = new HashMap<String, Object>();
		params.put("primary", bill.getPrimary());
		params.put("personId", bill.getApplyPerson().getId());
		params.put("orderByClause", "FEFFECTDATE desc ,(case when FEXPIRYDATE is null then 1 else 0 end) desc,FEXPIRYDATE desc ");
		List<PersonPositionHistory> phisList =  personPositionHistoryDao.getPositionHistory(params);
		if(phisList==null || phisList.size()<1){
			return resultMsg ;
		}
		PersonPositionHistory param = new PersonPositionHistory();
		param.setId(phisList.get(0).getId());
		param.setExpirydate(bill.getEffectdate());//把调职申请单的生效日期 更新到上一次调职的失效日期
		param.setLastupdateTime(new Date());
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
			param.setUpdator(loginUser.getId());
		}
		Org loginOrg = bill.getOrg();
		if(loginOrg!=null){
			param.setOrgId(loginOrg.getId());
		}
		param.setPrimary(true);//主要职位
		personPositionHistoryDao.updateByPrimaryKeySelective(param);
		return resultMsg ;
	}
	
	/**
	 * 根据调职申请单 写任职历史记录
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String addPositionHistoryByBill(BdPositionHistoryBill bill){
		String resultMsg = "SUCC";
		PersonPositionHistory phis = new PersonPositionHistory();
		Date now = new Date();
		phis.setCreateTime(now);
		Person loginUser = bill.getUpdator();
		if(loginUser!=null){
		 phis.setCreatorId(loginUser.getId());
		 phis.setUpdator(loginUser.getId());
		}
		Org loginOrg = bill.getOrg();
		if(loginOrg!=null){
		  phis.setOrgId(loginOrg.getId());
		}
		phis.setLastupdateTime(now);
		phis.setIsdisable("N");
		phis.setGivePersonId(bill.getHandOverPerson()!=null?bill.getHandOverPerson().getId():null);//离职交接人 by hlz
		phis.setPersonId(bill.getApplyPerson().getId());
		phis.setJobStatus(bill.getJobStatus());
		phis.setPrimary(bill.getPrimary());
		phis.setOldOrgId(bill.getApplyOrg().getId());
		phis.setOldPositionId(bill.getApplyPosition().getId());
		phis.setOldJobLevel(bill.getApplyJoblevel().getId());
		if(bill.getApplyChangeOrg()!=null){
			phis.setChangeOrgId(bill.getApplyChangeOrg().getId());
			phis.setChangePositionId(bill.getApplyChangePosition().getId());
			phis.setChangeJobLevel(bill.getApplyChangeJoblevel().getId());
		}
		phis.setEffectdate(bill.getEffectdate());
		phis.setChangeType(bill.getChangeType());
		
		personPositionHistoryDao.addEntity(phis);
		return resultMsg ;
	}

}
