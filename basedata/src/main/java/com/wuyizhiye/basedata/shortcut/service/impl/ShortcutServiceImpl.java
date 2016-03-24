package com.wuyizhiye.basedata.shortcut.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.shortcut.dao.ShortcutDao;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;
import com.wuyizhiye.basedata.shortcut.model.ShortcutAssociate;
import com.wuyizhiye.basedata.shortcut.service.ShortcutService;

/**
 * @ClassName ShortcutServiceImpl
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="shortcutService")
@Transactional
public class ShortcutServiceImpl extends BaseServiceImpl<Shortcut> implements ShortcutService{

	@Autowired
	private ShortcutDao shortcutDao;
	
	@Override
	protected BaseDao getDao() {
		return shortcutDao;
	}

	@Override
	public void setAccredit(String shortcutId, String personId) {
		ShortcutAssociate shortcutAssociate = new ShortcutAssociate();
		shortcutAssociate.setId(StringUtils.getUUID());
		shortcutAssociate.setPersonId(personId);
		shortcutAssociate.setShoutcutId(shortcutId);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("shortcutId", shortcutId);
		param.put("personId", personId);
		queryExecutor.executeInsert("com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.setAccredit", shortcutAssociate);
	}

	@Override
	public void delAccredit(String shortcutId, String personId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("shortcutId", shortcutId);
		param.put("personId", personId);
		queryExecutor.executeDelete("com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.delAccredit", param);
	}

}
