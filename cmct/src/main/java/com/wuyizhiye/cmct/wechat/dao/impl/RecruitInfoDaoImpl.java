package com.wuyizhiye.cmct.wechat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.wechat.dao.RecruitInfoDao;

/**
 * @ClassName RecruitInfoDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="recruitInfoDao")
public class RecruitInfoDaoImpl extends BaseDaoImpl implements RecruitInfoDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.wechat.dao.RecruitInfoDao";
	}
}
