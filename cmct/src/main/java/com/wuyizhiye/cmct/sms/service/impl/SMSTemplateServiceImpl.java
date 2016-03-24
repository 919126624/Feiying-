package com.wuyizhiye.cmct.sms.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.dao.SMSTemplateDao;
import com.wuyizhiye.cmct.sms.enums.SMSTemplateEnum;
import com.wuyizhiye.cmct.sms.model.SMSTemplate;
import com.wuyizhiye.cmct.sms.service.SMSTemplateService;

/**
 * @ClassName SMSTemplateServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="sMSTemplateService")
@Transactional
public class SMSTemplateServiceImpl extends BaseServiceImpl<SMSTemplate> implements SMSTemplateService {
	@Autowired
	private SMSTemplateDao sMSTemplateDao;
	@Override
	protected BaseDao getDao() {
		return sMSTemplateDao;
	}
	@Override
	public void addEntity(SMSTemplate entity) {
		entity.setCreateTime(new Date());
		entity.setPerson(SystemUtil.getCurrentUser());
		entity.setTemplateStatus(SMSTemplateEnum.ENABLED);
		super.addEntity(entity);
	}	
}