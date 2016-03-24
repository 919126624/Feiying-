package com.wuyizhiye.basedata.sync.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.sync.dao.BillTypeCopyDao;
import com.wuyizhiye.basedata.sync.model.BillTypeCopy;

/**
 * @ClassName BillTypeCopyDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="billTypeCopyDao")
public class BillTypeCopyDaoImpl extends BaseDaoImpl implements BillTypeCopyDao {

	@Override
	protected String getNameSpace() {
		
		return "com.wuyizhiye.basedata.sync.dao.BillTypeCopyDao";
	}

	@Override
	public List<BillTypeCopy> getBillTypeList(Map<String, Object> param) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
	

}
