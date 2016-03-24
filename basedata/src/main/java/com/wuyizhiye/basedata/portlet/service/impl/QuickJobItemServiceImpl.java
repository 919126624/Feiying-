package com.wuyizhiye.basedata.portlet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao;
import com.wuyizhiye.basedata.portlet.model.QuickJobItem;
import com.wuyizhiye.basedata.portlet.service.QuickJobItemService;

/**
 * @ClassName QuickJobItemServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickJobItemService")
@Transactional
public class QuickJobItemServiceImpl extends BaseServiceImpl<QuickJobItem> implements QuickJobItemService {
	@Autowired
	private QuickJobItemDao quickJobItemDao;
	@Override
	protected BaseDao getDao() {
		return quickJobItemDao;
	}	
}