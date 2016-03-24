package com.wuyizhiye.basedata.person.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PersonDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonDao extends BaseDao {

	/**
	 * 根据人员id得到部门
	 * added by taking.wang
	 * @since 2013-01-19
	 */
	Org getPersonOrg(String personId);

	Person selectPersonLead(Map<String, Object> param);
	
	List<Person> getPersonList(Map<String,Object> params);
	
	public List<Person> getPersonLeaderWithJobLevel(Map<String,Object> params);
}
