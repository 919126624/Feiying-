package com.wuyizhiye.basedata.cchat.dao;

import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;


/**
 * @ClassName PrivateLetterDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PrivateLetterDao extends BaseDao {
	public int selectLetterCount(Map<String, Object> map);
}
