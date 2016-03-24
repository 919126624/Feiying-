package com.wuyizhiye.basedata.shortcut.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.shortcut.dao.ShortcutDao;

/**
 * @ClassName ShortcutDaoImpl
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="shortcutDao")
public class ShortcutDaoImpl extends BaseDaoImpl implements ShortcutDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.shortcut.dao.ShortcutDao";
	}

}
