package com.wuyizhiye.basedata.code.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.code.model.PrintConfig;

/**
 * @ClassName PrintConfigDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PrintConfigDao extends BaseDao {
	public List<PrintConfig> getList(Map<String, Object> param);
}
