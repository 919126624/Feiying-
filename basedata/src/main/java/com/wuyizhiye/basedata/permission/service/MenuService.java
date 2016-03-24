package com.wuyizhiye.basedata.permission.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.Menu;

/**
 * @ClassName MenuService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface MenuService extends BaseService<Menu> {
	List<Menu> getMenuList(Map<String,Object> map);
}
