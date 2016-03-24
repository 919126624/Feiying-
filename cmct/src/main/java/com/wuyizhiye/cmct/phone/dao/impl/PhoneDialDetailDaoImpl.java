package com.wuyizhiye.cmct.phone.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.cmct.phone.dao.PhoneDialDetailDao;

/**
 * @ClassName PhoneDialDetailDaoImpl
 * @Description 话费明细接口实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneDialDetailDao")
public class PhoneDialDetailDaoImpl extends BaseDaoImpl implements PhoneDialDetailDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneDialDetailDao";
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Org> getOrgByType(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList(getNameSpace()+".getFastOrg", param);
	}


	@Override
	public int deleteBySyncId(String syncid) {
		return this.getSqlSession().delete(getNameSpace()+".deleteBySyncId",syncid);
	}
}
