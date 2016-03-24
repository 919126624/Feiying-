package com.wuyizhiye.basedata.person.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.PersonPosition;

/**
 * @ClassName PersonPositionService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPositionService extends BaseService<PersonPosition> {
	/**
	 * 根据人员得到所有的任职信息,包括主要职位 兼职
	 * @param person
	 * @return
	 */
	List<PersonPosition> getByPerson(String person);
	
	/**
	 * 根据条件得到人员任职信息,可查到人员所在组织,职位名称
	 * @param map 接收参数  人员ID 职位ID  
	 * @return
	 */
	PersonPosition getPerPositionByCondition(Map map);
	
	/**
	 * 根据条件得到人员相关任职信息,可查到人员所在组织,职位名称,组织下所有职位,组织下所有人员。
	 * @param map 接收参数  人员ID 职位ID  
	 * @return
	 */
	List<PersonPosition> getPerPositionListByCondition(Map map);
	
	void deleteByPersonId(String perosnId);
}
