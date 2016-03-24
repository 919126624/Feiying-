package com.wuyizhiye.basedata.basic.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.BaseConfig;

/**
 * @ClassName BaseConfigService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BaseConfigService extends BaseService<BaseConfig> {
	
	void updateList(List<BaseConfig> list);
	
}
