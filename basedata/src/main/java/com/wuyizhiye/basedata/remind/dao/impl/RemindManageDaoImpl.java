package com.wuyizhiye.basedata.remind.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.remind.dao.RemindManageDao;

/**
 * @ClassName RemindManageDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="remindManageDao")
public class RemindManageDaoImpl extends BaseDaoImpl implements RemindManageDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.remind.dao.RemindManageDao";
	}

	@Override
	public int deleteById(String id) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", id);
		return getSqlSession().delete("com.wuyizhiye.basedata.remind.dao.RemindManageDao.deleteById", param);
	}

}
