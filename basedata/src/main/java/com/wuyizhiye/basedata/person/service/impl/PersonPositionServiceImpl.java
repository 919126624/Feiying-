package com.wuyizhiye.basedata.person.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonPositionService;

/**
 * @ClassName PersonPositionServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositionService")
@Transactional
public class PersonPositionServiceImpl extends BaseServiceImpl<PersonPosition>
		implements PersonPositionService {
	@Autowired
	private PersonPositionDao personPositionDao;
	@Override
	public List<PersonPosition> getByPerson(String person) {
		return personPositionDao.getByPerson(person);
	}

	@Override
	protected BaseDao getDao() {
		return personPositionDao;
	}

	@Override
	public PersonPosition getPerPositionByCondition(Map map) {
		return personPositionDao.getPerPositionByCondition(map);
	}

	@Override
	public List<PersonPosition> getPerPositionListByCondition(Map map) {
		
		return personPositionDao.getPerPositionListByCondition(map);
	}

	@Override
	public void deleteByPersonId(String perosnId) {
		personPositionDao.deleteByPerson(perosnId);
		
	}

}
