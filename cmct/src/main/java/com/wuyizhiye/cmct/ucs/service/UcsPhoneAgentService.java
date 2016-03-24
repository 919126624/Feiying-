package com.wuyizhiye.cmct.ucs.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneAgent;

/**
 * @ClassName UcsPhoneAgentService
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
public interface UcsPhoneAgentService extends BaseService<UcsPhoneAgent> {
	
	/**
	 * 删除企业或则经销商是用到,根据dealer找到上级的agent对象
	 */
	public UcsPhoneAgent getDealerAgent(Map<String, Object>param);
}
