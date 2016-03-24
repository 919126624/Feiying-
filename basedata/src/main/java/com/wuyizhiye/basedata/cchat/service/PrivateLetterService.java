package com.wuyizhiye.basedata.cchat.service;

import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.cchat.model.CChat;

/**
 * @ClassName PrivateLetterService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PrivateLetterService extends BaseService<CChat> {

	public int selectLetterCount(Map<String, Object> map);
}
