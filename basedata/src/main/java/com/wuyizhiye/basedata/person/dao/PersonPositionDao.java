package com.wuyizhiye.basedata.person.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.person.model.PersonPosition;

/**
 * @ClassName PersonPositionDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPositionDao extends BaseDao {
	
	/**
	 * 根据人员得到所有的任职信息,包括主要职位 兼职
	 * @param person
	 * @return
	 */
	List<PersonPosition> getByPerson(String person);
	
	void deleteByPerson(String person);
	
	/**
	 * 根据条件得到人员主要任职信息,可查到人员所在组织,职位名称。最多只有一条记录
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
	
	/**
	 * 根据条件删除 人员任职信息
	 * @param params
	 */
	public void deleteByCondition(Map<String,Object> params) ;
	
}
