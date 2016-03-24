package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.model.PhoneRight;

/**
 * @ClassName PhoneMemberService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneMemberService extends BaseService<PhoneMember> {
	
	void updatePhoneRight(PhoneMember pm,PhoneRight pr,String type) throws Exception;
	
	/**
	 * 分配设置
	 */
	Map<String, Object> batchMatchPhone(PhoneMember pm);
	/**
	 * 定时任务：清空被占用的电话的使用人
	 * @return
	 * @throws Exception
	 */
	public void taskAutoCleanCurrUser() throws Exception;
}

