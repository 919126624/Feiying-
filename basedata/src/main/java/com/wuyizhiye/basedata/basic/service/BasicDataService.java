package com.wuyizhiye.basedata.basic.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName BasicDataService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface BasicDataService extends BaseService<BasicData> {
 List<BasicData> getBasicDataList(Map<String,Object> param);
 
 //根据编码找到值
 BasicData getEntityByNumber(String number);
}
