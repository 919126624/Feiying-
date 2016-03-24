package com.wuyizhiye.cmct.ucs.dao;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName UcsPhoneMemberDao
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface UcsPhoneMemberDao extends BaseDao {
	
	/**
	 * 删除企业的时候把关联的坐席删掉
	 */
	public void deleteByAgentId(String id);
}
