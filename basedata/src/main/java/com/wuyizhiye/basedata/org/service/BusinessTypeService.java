package com.wuyizhiye.basedata.org.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.BusinessType;

/**
 * @ClassName BusinessTypeService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BusinessTypeService extends BaseService<BusinessType> {
	/**
	 * 获取所有业务类型
	 * @return
	 */
	@Cacheable(value = "allBusinessTypes")
	public List<BusinessType> getAllBusinessTypes();
	
	public List<BusinessType> getAllBusinessTypes(Map<String, Object> map);
}
