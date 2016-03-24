package com.wuyizhiye.basedata.code.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.dao.BillTypeDao;
import com.wuyizhiye.basedata.code.model.BillType;
import com.wuyizhiye.basedata.code.service.BillTypeService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName BillTypeServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="billTypeService")
@Transactional
public class BillTypeServiceImpl extends DataEntityService<BillType> implements BillTypeService {
	@Autowired
	private BillTypeDao billTypeDao;
	@Override
	protected BaseDao getDao() {
		return billTypeDao;
	}	
	
	@Override
	public List<BillType> getAllBillTypeList(Map<String,Object> map) {
		return this.billTypeDao.getBillTypeList(map);
		
	}
}