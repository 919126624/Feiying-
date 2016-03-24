package com.wuyizhiye.basedata.person.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryLogDao;

/**
 * @ClassName PersonPositionHistoryLogDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositionHistoryLogDao")
public class PersonPositionHistoryLogDaoImpl extends BaseDaoImpl implements PersonPositionHistoryLogDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.PersonPositionHistoryLogDao";
	}
}
