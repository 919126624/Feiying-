package com.wuyizhiye.cmct.phone.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCallIn;

/**
 * @ClassName PhoneCallInService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneCallInService extends BaseService<PhoneCallIn> {
	
	/**
	 * 每天自动向鼎尖平台同步副号账单数据
	 */
	public void autoTaskSyncBill();
}
