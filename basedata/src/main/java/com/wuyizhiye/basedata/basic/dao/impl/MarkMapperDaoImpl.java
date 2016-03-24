package com.wuyizhiye.basedata.basic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.basic.dao.MarkMapperDao;

/**
 * @ClassName MarkMapperDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="markMapperDao")
public class MarkMapperDaoImpl extends BaseDaoImpl implements MarkMapperDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.MarkMapperDao";
	}
}
