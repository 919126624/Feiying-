package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.JobCategoryDao;

/**
 * @ClassName JobCategoryDaoImpl
 * @Description 岗位大类Dao impl
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="JobCategoryDao")
public class JobCategoryDaoImpl extends BaseDaoImpl implements JobCategoryDao{
	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.org.dao.JobCategoryDao";
	}

}