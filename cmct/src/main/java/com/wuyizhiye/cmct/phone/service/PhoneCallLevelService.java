package com.wuyizhiye.cmct.phone.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCallLevel;

/**
 * @ClassName PhoneCallLevelService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneCallLevelService extends BaseService<PhoneCallLevel> {
	public void taskAutoSyncPhoneCall();
	
}
