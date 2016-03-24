package com.wuyizhiye.basedata.person.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.DayPerson;

/**
 * @ClassName DayPersonService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface DayPersonService extends BaseService<DayPerson> {
	
	/**
	 * 同步人员数据
	 */
	void execute(); 
}
