package com.wuyizhiye.cmct.phone.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneRight;

/**
 * @ClassName PhoneRightService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneRightService extends BaseService<PhoneRight> {
	
	void saveRight(PhoneRight p) throws Exception;
	
	void delRight(PhoneRight p) throws Exception;
	
	/**
	 * 更新核算组织的默认权限设置
	 * @param pr
	 * @param type setDefault setDefaultNone
	 * @throws Exception
	 */
	void updateOrgDefaultRight(PhoneRight pr , String type) throws Exception;
}
