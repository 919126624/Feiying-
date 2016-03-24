package com.wuyizhiye.basedata.permission.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.permission.dao.ShortcutsDao;

/**
 * @ClassName ShortcutsDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="shortcutsDao")
public class ShortcutsDaoImpl extends BaseDaoImpl implements ShortcutsDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.permission.dao.ShortcutsDao";
	}

	@Override
	public int getMaxIndex(Map<String,Object> param) {
		@SuppressWarnings("unchecked")
		List<Integer> result = getSqlSession().selectList(getNameSpace() + ".getMaxIndex",param);
		return (result==null || result.size()==0)?-1:(result.get(0)==null)?0:result.get(0);
	}
}
