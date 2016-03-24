package com.wuyizhiye.basedata.sync.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.sync.model.BillTypeCopy;

/**
 * @ClassName BillTypeCopyDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface BillTypeCopyDao extends BaseDao{
	List<BillTypeCopy> getBillTypeList(Map<String,Object> param);
}
