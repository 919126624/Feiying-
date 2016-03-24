package com.wuyizhiye.cmct.phone.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhoneMainShow;

/**
 * @ClassName PhoneMainShowService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface PhoneMainShowService extends BaseService<PhoneMainShow> {
	
	public Map<String, Object>matchDisplayNbr(String ids,String orgId);
	
	public Map<String, Object>deleteDisplayNbr(String id);
}
