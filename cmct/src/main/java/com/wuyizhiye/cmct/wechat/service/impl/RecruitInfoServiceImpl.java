package com.wuyizhiye.cmct.wechat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.wechat.dao.RecruitInfoDao;
import com.wuyizhiye.cmct.wechat.model.RecruitInfo;
import com.wuyizhiye.cmct.wechat.service.RecruitInfoService;

/**
 * @ClassName RecruitInfoServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="recruitInfoService")
@Transactional
public class RecruitInfoServiceImpl extends BaseServiceImpl<RecruitInfo> implements RecruitInfoService {
	@Autowired
	private RecruitInfoDao recruitInfoDao;
	@Override
	protected BaseDao getDao() {
		return recruitInfoDao;
	}	
}