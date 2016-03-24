package com.wuyizhiye.basedata.code.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.code.dao.BillTypeDao;
import com.wuyizhiye.basedata.code.model.BillType;

/**
 * @ClassName BillTypeDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="billTypeDao")
public class BillTypeDaoImpl extends BaseDaoImpl implements BillTypeDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.code.BillTypeDao";
	}
	@Override
	public List<BillType> getBillTypeList(Map<String, Object> param) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
}
