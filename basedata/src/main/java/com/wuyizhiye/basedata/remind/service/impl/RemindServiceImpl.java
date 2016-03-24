package com.wuyizhiye.basedata.remind.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.remind.dao.RemindDao;
import com.wuyizhiye.basedata.remind.model.Remind;
import com.wuyizhiye.basedata.remind.service.RemindService;

/**
 * @ClassName RemindServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="remindService")
@Transactional
public class RemindServiceImpl extends BaseServiceImpl<Remind> implements RemindService{

	@Autowired
	private RemindDao remindDao;
	
	@Override
	protected BaseDao getDao() {
		return remindDao;
	}

}
