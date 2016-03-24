package com.wuyizhiye.basedata.permission.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.Shortcuts;

/**
 * @ClassName ShortcutsService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ShortcutsService extends BaseService<Shortcuts> {
	int getMaxIndex(Map<String,Object> param);
}
