package com.wuyizhiye.basedata.code.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.code.model.Area;

/**
 * @ClassName AreaService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface AreaService extends BaseService<Area>{
	public List<Area> getListByCollection(Map<String,Object> params);
	public List<Area> getAreaList(String areaType,String parentId);
}
