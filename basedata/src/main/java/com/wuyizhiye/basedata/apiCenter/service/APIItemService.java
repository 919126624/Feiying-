package com.wuyizhiye.basedata.apiCenter.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;

/**
 * @ClassName APIItemService
 * @Description 接口管理Service接口
 * @author li.biao
 * @date 2015-4-2
 */
public interface APIItemService extends BaseService<APIItem>{ 
	
	public Map apiTest(String id,int idx);
	public List<APIItem> getList(Map<String, Object> param);
	public Map<String,Object> getVersion(String version);
}
