package com.wuyizhiye.basedata.code.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.PrintConfig;

/**
 * @ClassName PrintConfigService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PrintConfigService extends BaseService<PrintConfig> {

	public List<PrintConfig> getList(Map<String, Object> param);
}
