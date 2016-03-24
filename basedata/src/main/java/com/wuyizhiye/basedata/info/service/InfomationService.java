package com.wuyizhiye.basedata.info.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.info.model.Infomation;

/**
 * @ClassName InfomationService
 * @Description 消息提醒
 * @author li.biao
 * @date 2015-4-2
 */
public interface InfomationService extends BaseService<Infomation> {
	public void sendInfo(Map<String,Object> map);
}
