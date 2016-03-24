package com.wuyizhiye.hr.affair.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.service.JobLevelService;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.hr.affair.dao.ProcessInfoDao;
import com.wuyizhiye.hr.affair.model.ProcessInfo;
import com.wuyizhiye.hr.affair.service.ProcessInfoService;
import com.wuyizhiye.hr.enums.BillEnum;
/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
@Component(value="processInfoService")
@Transactional
public class ProcessInfoServiceImpl extends BaseServiceImpl<ProcessInfo> implements ProcessInfoService {

	@Autowired
	private ProcessInfoDao processInfoDao;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PositionService positionService;
	
	@Autowired
	private JobLevelService jobLevelService;
	
	@Override
	protected BaseDao getDao() {
		return processInfoDao;
	}

	@Override
	public void insertProcessInfo(PersonPositionHistory pph) {
		ProcessInfo pi = new ProcessInfo();
		pi.setApply(personService.getEntityById(pph.getPersonId()));
		pi.setAuditDate(pph.getEffectdate());
		pi.setBillType(BillEnum.valueOf(pph.getChangeType()));
		if(StringUtils.isNotNull(pph.getOldOrgId())){
			pi.setOldOrg(orgService.getEntityById(pph.getOldOrgId()));
		}
		if(StringUtils.isNotNull(pph.getChangeOrgId())){
			pi.setChangeOrg(orgService.getEntityById(pph.getChangeOrgId()));
		}
		if(StringUtils.isNotNull(pph.getOldPositionId())){
			pi.setOldPosition(positionService.getEntityById(pph.getOldPositionId()));
		}
		
		if(StringUtils.isNotNull(pph.getChangePositionId())){
			pi.setChangePosition(positionService.getEntityById(pph.getChangePositionId()));
		}
		if(StringUtils.isNotNull(pph.getOldJobLevel())){
			pi.setOldJobLevel(jobLevelService.getEntityById(pph.getOldJobLevel()));
		}
		
		if(StringUtils.isNotNull(pph.getChangeJobLevel())){
			pi.setChangeJobLevel(jobLevelService.getEntityById(pph.getChangeJobLevel()));
		}
		
		if(StringUtils.isNotNull(pph.getGivePersonId())){
			pi.setGivePerson(personService.getEntityById(pph.getGivePersonId()));
		}
		
		processInfoDao.addEntity(pi);
	}

}
