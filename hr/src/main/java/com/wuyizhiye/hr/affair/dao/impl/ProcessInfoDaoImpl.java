package com.wuyizhiye.hr.affair.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.ProcessInfoDao;
import com.wuyizhiye.hr.affair.model.ProcessInfo;

/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
@Component(value="processInfoDao")
public class ProcessInfoDaoImpl extends BaseDaoImpl implements ProcessInfoDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.affair.dao.ProcessInfoDao";
	}

	@Override
	public void insertProcessInfo(ProcessInfo processInfo) {
		addEntity(processInfo);
	}

}
