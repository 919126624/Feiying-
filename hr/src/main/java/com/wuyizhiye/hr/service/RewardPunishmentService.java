package com.wuyizhiye.hr.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.model.RewardPunishment;

/**
 * 奖惩记录service
 * @author taking.wang
 * @since 2013-01-18
 */
public interface RewardPunishmentService extends BaseService<RewardPunishment> {
	
	List<RewardPunishment> getByPersonId(String personId);
}
