package com.wuyizhiye.basedata.param.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.param.model.ParamLines;

/**
 * @ClassName ParamLinesService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ParamLinesService extends BaseService<ParamLines> {
	
	/**
	 * 针对于只有一个分录的系统参数方法
	 * @param map
	 * @return
	 */
	ParamLines getOneParamLines(Map<String,Object> map);
	
}
