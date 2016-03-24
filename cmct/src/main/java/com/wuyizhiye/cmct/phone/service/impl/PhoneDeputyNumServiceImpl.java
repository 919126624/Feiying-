package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.cmct.phone.dao.PhoneDeputyNumDao;
import com.wuyizhiye.cmct.phone.model.PhoneDeputyNum;
import com.wuyizhiye.cmct.phone.service.PhoneDeputyNumService;

/**
 * @ClassName PhoneDeputyNumServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDeputyNumService")
@Transactional
public class PhoneDeputyNumServiceImpl extends DataEntityService<PhoneDeputyNum> implements PhoneDeputyNumService {
	@Autowired
	private PhoneDeputyNumDao phoneDeputyNumDao;
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	protected BaseDao getDao() {
		return phoneDeputyNumDao;
	}
	@Override
	public void addEntity(PhoneDeputyNum entity) {
		if(null!=entity.getBindPerson()){
			Person person=this.personDao.getEntityById(entity.getBindPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.addEntity(entity);
	}
	@Override
	public void updateEntity(PhoneDeputyNum entity) {
		if(null!=entity.getBindPerson()){
			Person person=this.personDao.getEntityById(entity.getBindPerson().getId());
			entity.setControlUnit(person.getControlUnit());
		}
		super.updateEntity(entity);
	}	
}