package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;

/**
 * @ClassName PhoneCostPayService
 * @Description 话费充值业务接口
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneCostPayService extends BaseService<PhoneCostPay> {

	/**
	 * 根据月份，得到每个月的充值记录总额
	 */
	public Double getPaycostByPeriod(Map<String, Object> pararm);
	
}
