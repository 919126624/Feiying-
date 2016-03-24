package com.wuyizhiye.hr.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.model.RewardPunishment;

/**
 * 奖惩记录Dao
 * @author taking.wang
 * @since 2013-01-18
 */
public interface RewardPunishmentDao extends BaseDao {
	List<RewardPunishment> getByPersonId(String personId);
	
	void deleteByPersonId(String personId);
}
