package com.wuyizhiye.basedata.remind.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.remind.dao.RemindDao;

/**
 * @ClassName RemindDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="remindDao")
public class RemindDaoImpl extends BaseDaoImpl implements RemindDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.remind.dao.RemindDao";
	}

}
