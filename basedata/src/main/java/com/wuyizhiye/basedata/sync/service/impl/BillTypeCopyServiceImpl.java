package com.wuyizhiye.basedata.sync.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.sync.dao.BillTypeCopyDao;
import com.wuyizhiye.basedata.sync.model.BillTypeCopy;
import com.wuyizhiye.basedata.sync.service.BillTypeCopyService;

/**
 * @ClassName BillTypeCopyServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="billTypeCopyService")
public class BillTypeCopyServiceImpl extends BaseServiceImpl<BillTypeCopy>
		implements BillTypeCopyService {

	@Autowired
	private BillTypeCopyDao billTypeCopyDao;
	
	@Override
	protected BaseDao getDao() {
		
		return billTypeCopyDao;
		
	}

	@Override
	public List<BillTypeCopy> getAllBillTypeList() {
		return this.billTypeCopyDao.getBillTypeList(null);
		
	}

	

}
