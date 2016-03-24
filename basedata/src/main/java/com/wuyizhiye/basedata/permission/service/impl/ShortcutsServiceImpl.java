package com.wuyizhiye.basedata.permission.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.permission.dao.ShortcutsDao;
import com.wuyizhiye.basedata.permission.model.Shortcuts;
import com.wuyizhiye.basedata.permission.service.ShortcutsService;

/**
 * @ClassName ShortcutsServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="shortcutsService")
@Transactional
public class ShortcutsServiceImpl extends BaseServiceImpl<Shortcuts> implements
		ShortcutsService {
	@Autowired
	private ShortcutsDao shortcutsDao;
	@Override
	protected BaseDao getDao() {
		return shortcutsDao;
	}
	@Override
	public int getMaxIndex(Map<String, Object> param) {
		return shortcutsDao.getMaxIndex(param);
	}
}
