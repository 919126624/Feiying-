package com.wuyizhiye.cmct.sms.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.sms.dao.SMSTemplateDao;

/**
 * @ClassName SMSTemplateDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="sMSTemplateDao")
public class SMSTemplateDaoImpl extends BaseDaoImpl implements SMSTemplateDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.sms.dao.SMSTemplateDao";
	}
}
