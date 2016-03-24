package com.wuyizhiye.basedata.person.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryLogDao;
import com.wuyizhiye.basedata.person.model.PersonPositionHistoryLog;
import com.wuyizhiye.basedata.person.service.PersonPositionHistoryLogService;
import com.wuyizhiye.basedata.util.OrgUtils;

/**
 * @ClassName PersonPositionHistoryLogServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositionHistoryLogService")
@Transactional
public class PersonPositionHistoryLogServiceImpl extends BaseServiceImpl<PersonPositionHistoryLog> implements PersonPositionHistoryLogService {
	@Autowired
	private PersonPositionHistoryLogDao personPositionHistoryLogDao;
	@Override
	protected BaseDao getDao() {
		return personPositionHistoryLogDao;
	}
	@Override
	public void addEntity(PersonPositionHistoryLog entity) {
		if(!StringUtils.isEmpty(entity.getPersonOrgId())){
			 entity.setControlUnit(OrgUtils.getCUByOrg(entity.getPersonOrgId()));
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(PersonPositionHistoryLog entity) {
		if(!StringUtils.isEmpty(entity.getPersonOrgId())){
			 entity.setControlUnit(OrgUtils.getCUByOrg(entity.getPersonOrgId()));
		}
		super.updateEntity(entity);
	}	
}