package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;

/**
 * @ClassName PhoneDialDetailService
 * @Description 话费明细service
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneDialDetailService extends BaseService<PhoneDialDetail> {

	/**
	 * 同步话单（一次性同步所有）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> syncCallList(Map<String,Object> param) throws Exception;
	
	/**
	 * 同步话单，每天同步（一次性同步所有）
	 * @param param （syncTime 同步日期）
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> syncCallListDay(Map<String,Object> param) throws Exception;
	
	/**
	 * 定时任务：每天同步话单
	 * @return
	 * @throws Exception
	 */
	public void taskAutoSyncCallList() throws Exception;
	
	/**
	 * 根据组织类型 找到 级别最高的一个
	 * @param number
	 * @return
	 */
	Org getOrgByType(String number);
	
	/**
	 * 同步电信的话单(每天同步)
	 */
	public Map<String,Object> syncDxCallListDay(Map<String,Object> param) throws Exception;
}
