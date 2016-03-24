package com.wuyizhiye.cmct.phone.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.cmct.phone.dao.PhoneCosDetailDao;
import com.wuyizhiye.cmct.phone.model.PhoneCosDetail;
import com.wuyizhiye.cmct.phone.service.PhoneCosDetailService;

/**
 * @ClassName PhoneCosDetailServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="phoneCosDetailService")
@Transactional
public class PhoneCosDetailServiceImpl extends BaseServiceImpl<PhoneCosDetail> implements PhoneCosDetailService {
	@Autowired
	private PhoneCosDetailDao phoneCosDetailDao;
	@Override
	protected BaseDao getDao() {
		return phoneCosDetailDao;
	}	
}