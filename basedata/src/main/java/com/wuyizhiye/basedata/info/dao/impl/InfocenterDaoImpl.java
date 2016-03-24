package com.wuyizhiye.basedata.info.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.info.dao.InfocenterDao;
import com.wuyizhiye.basedata.info.model.Remind;

/**
 * @ClassName InfocenterDaoImpl
 * @Description 消息提醒
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="infocenterDao")
public class InfocenterDaoImpl extends BaseDaoImpl implements InfocenterDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.info.dao.InfocenterDao";
	}

	@Override
	public int saveRemind(Remind remind) {
		return getSqlSession().insert(getNameSpace()+".insertRemind", remind);
	}

	@Override
	public int deleteRemindById(String id) {
		return getSqlSession().delete(getNameSpace()+".deleteRemind", id);
	}
}
