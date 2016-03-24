package com.wuyizhiye.cmct.phone.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.ApiCallBack;

/**
 * @ClassName ApiCallBackService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface ApiCallBackService extends BaseService<ApiCallBack> {
	
	public void executeClean() ;
	
}
