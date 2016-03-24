package com.wuyizhiye.hr.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.dao.RewardPunishmentDao;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.service.RewardPunishmentService;

/**
 * 奖惩记录serviceImpl
 * @author taking.wang
 * @since 2013-01-18
 */
@Component(value="rewardPunishmentService")
@Transactional
public class RewardPunishmentServiceImpl extends
		BaseServiceImpl<RewardPunishment> implements RewardPunishmentService {

	@Autowired
	private RewardPunishmentDao rewardPunishmentDao;
	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return rewardPunishmentDao;
	}

	@Override
	public List<RewardPunishment> getByPersonId(String personId) {
		// TODO Auto-generated method stub
		return rewardPunishmentDao.getByPersonId(personId);
	}


}
