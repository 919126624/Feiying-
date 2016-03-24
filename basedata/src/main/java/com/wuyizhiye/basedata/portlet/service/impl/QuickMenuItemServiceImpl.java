package com.wuyizhiye.basedata.portlet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao;
import com.wuyizhiye.basedata.portlet.model.QuickMenuItem;
import com.wuyizhiye.basedata.portlet.service.QuickMenuItemService;

/**
 * @ClassName QuickMenuItemServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickMenuItemService")
@Transactional
public class QuickMenuItemServiceImpl extends BaseServiceImpl<QuickMenuItem> implements QuickMenuItemService {
	@Autowired
	private QuickMenuItemDao quickMenuItemDao;
	@Override
	protected BaseDao getDao() {
		return quickMenuItemDao;
	}	
}