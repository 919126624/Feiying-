package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCallRecorDing;

/**
 * @ClassName PhoneCallRecorDingService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneCallRecorDingService extends BaseService<PhoneCallRecorDing> {
	
	/**
	 * 批量转存录音文件
	 */
	public Map<String, Object> batchTurnSave(String []ids,String year,String month);
}
