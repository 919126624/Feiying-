package com.wuyizhiye.basedata.person.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;

/**
 * @ClassName PersonService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonService extends BaseService<Person> {
	void addPerson(Person person,List<PersonPosition> personPosition);
	void updatePerson(Person person,List<PersonPosition> personPosition);
	
	//根据人员id得到组织  added by taking.wang
	Org getPersonOrg(String personId);
	//根据组织ID获取组织的主要领导人
	Person selectPersonLead(Map<String, Object> param);
	/**
	 * 新增或修改人员信息   同步到微信企业号
	 * @param person
	 * @param personPosition
	 * @param function  新增修改   add/update
	 */
	public void synWeiXinPerson(Person person, List<PersonPosition> personPosition,String function);
	
}
