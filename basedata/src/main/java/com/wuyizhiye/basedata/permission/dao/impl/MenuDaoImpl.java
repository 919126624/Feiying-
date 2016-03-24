package com.wuyizhiye.basedata.permission.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.MenuDao;
import com.wuyizhiye.basedata.permission.model.Menu;

/**
 * @ClassName MenuDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="menuDao")
public class MenuDaoImpl extends BaseDaoImpl implements MenuDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.MenuDao";
	}

	@Override
	public List<Menu> getMenuList(Map<String, Object> map) {
		
		return this.getSqlSession().selectList(getNameSpace()+".getMenuNoJoin", map);
	}
}
