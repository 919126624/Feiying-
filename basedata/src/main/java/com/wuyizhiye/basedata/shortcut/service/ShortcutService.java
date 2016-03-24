package com.wuyizhiye.basedata.shortcut.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;

/**
 * @ClassName ShortcutService
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-3
 */
public interface ShortcutService extends BaseService<Shortcut>{

	void setAccredit(String shortcutId, String personId);

	void delAccredit(String shortcutId, String personId);

}
