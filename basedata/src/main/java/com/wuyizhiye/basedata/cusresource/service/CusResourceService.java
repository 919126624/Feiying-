package com.wuyizhiye.basedata.cusresource.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.cusresource.model.CusResource;

/**
 * @ClassName CusResourceService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface CusResourceService extends BaseService<CusResource> {
 List<CusResource> getCusResourceList(Map<String,Object> param);
 
 //根据编码找到值
 CusResource getEntityByNumber(String number);
}
