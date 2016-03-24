package com.wuyizhiye.basedata.remind.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.remind.dao.RemindManageDao;
import com.wuyizhiye.basedata.remind.model.RemindManage;
import com.wuyizhiye.basedata.remind.service.RemindManageService;

/**
 * @ClassName RemindManageServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="remindManageService")
@Transactional
public class RemindManageServiceImpl extends BaseServiceImpl<RemindManage> implements RemindManageService{

	@Autowired
	private RemindManageDao remindManageDao;
	
	@Override
	protected BaseDao getDao() {
		return remindManageDao;
	}

}
