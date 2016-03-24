package com.wuyizhiye.basedata.code.dao;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName RecordDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface RecordDao extends BaseDao{
	/**
	 * 根据规则主表ID,删除明细信息
	 * @param id
	 */
	void deleteByRule(String id);
}
