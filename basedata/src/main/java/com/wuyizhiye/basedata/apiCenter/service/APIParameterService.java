package com.wuyizhiye.basedata.apiCenter.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;


/**
 * @ClassName APIParameterService
 * @Description 接口参数Service接口
 * @author li.biao
 * @date 2015-4-2
 */
public interface APIParameterService extends BaseService<APIParameter>{ 
	public List<APIParameter> getList(Map<String, Object> param);
}
