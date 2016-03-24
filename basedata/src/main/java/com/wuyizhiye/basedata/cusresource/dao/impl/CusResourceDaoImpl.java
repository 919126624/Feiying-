package com.wuyizhiye.basedata.cusresource.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.cusresource.dao.CusResourceDao;
import com.wuyizhiye.basedata.cusresource.model.CusResource;

/**
 * @ClassName CusResourceDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="cusResourceDao")
public class CusResourceDaoImpl extends BaseDaoImpl implements CusResourceDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.cusresource.dao.CusResourceDao";
	}

	@Override
	public List<CusResource> getCusResourceList(Map<String, Object> param) {
		return getSqlSession().selectList(getNameSpace()+".getChildrenData", param);
	}

	@Override
	public CusResource getEntityByNumber(String number) {
		// TODO Auto-generated method stub
		return (CusResource) this.getSqlSession().selectOne(this.getNameSpace()+".getByNumber", number);
	}

}
