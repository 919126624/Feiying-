package com.wuyizhiye.basedata.sync.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.sync.model.BillTypeCopy;

/**
 * @ClassName BillTypeCopyService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface BillTypeCopyService extends BaseService<BillTypeCopy> {
	
	List<BillTypeCopy> getAllBillTypeList();
	
}
