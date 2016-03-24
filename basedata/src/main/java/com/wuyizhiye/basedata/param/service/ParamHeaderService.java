package com.wuyizhiye.basedata.param.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.param.model.ParamHeader;

/**
 * @ClassName ParamHeaderService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ParamHeaderService extends BaseService<ParamHeader> {
	List<ParamHeader> getParamHeaderList(Map<String,Object> map);
	public void updateStatus(ParamHeader entity);
}
