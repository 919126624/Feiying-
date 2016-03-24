package com.wuyizhiye.basedata.code.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.BillType;

/**
 * @ClassName BillTypeService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BillTypeService extends BaseService<BillType> {
	
	public List<BillType> getAllBillTypeList(Map<String,Object> map);
}
