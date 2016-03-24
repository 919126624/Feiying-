package com.wuyizhiye.basedata.code.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.model.BillType;

/**
 * @ClassName BillTypeDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BillTypeDao extends BaseDao {
	public List<BillType> getBillTypeList(Map<String, Object> param);
}
