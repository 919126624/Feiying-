package com.wuyizhiye.cmct.phone.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCostPayDao;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;
import com.wuyizhiye.cmct.phone.service.PhoneCostPayService;

/**
 * @ClassName PhoneCostPayServiceImpl
 * @Description 话费充值，业务实现
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCostPayService")
public class PhoneCostPayServiceImpl extends BaseServiceImpl<PhoneCostPay>
		implements PhoneCostPayService {

	@Autowired
	private PhoneCostPayDao phoneCostPayDao;
	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return phoneCostPayDao;
	}
	@Override
	public Double getPaycostByPeriod(Map<String, Object> pararm) {
		Double total = queryExecutor.execOneEntity(PhoneCostPay.MAPPER+".selectPaycostByPeriod", pararm, Double.class);
		return total;
	}
}
