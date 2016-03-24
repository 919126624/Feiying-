package com.wuyizhiye.basedata.org.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.dao.JobCategoryDao;
import com.wuyizhiye.basedata.org.model.JobCategory;
import com.wuyizhiye.basedata.org.service.JobCategoryService;

/**
 * @ClassName JobCategoryServiceImpl
 * @Description 票据类型大类Service impl
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobCategoryService")
@Transactional
public class JobCategoryServiceImpl extends BaseServiceImpl<JobCategory> implements JobCategoryService{
	@Autowired
	private JobCategoryDao jobCategoryDao;
	
	@Override
	protected BaseDao getDao() {
		return this.jobCategoryDao;
	}
	
	@Override
	public List<JobCategory> getJobCategoryList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobCategoryDao.select", param, JobCategory.class);
	}

	@Override
	public List<JobCategory> getParentJobCategoryList() {	
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent",true);
		return this.getJobCategoryList(param);
	}


}
