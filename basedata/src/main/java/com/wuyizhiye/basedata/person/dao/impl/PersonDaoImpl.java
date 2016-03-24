package com.wuyizhiye.basedata.person.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName PersonDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personDao")
public class PersonDaoImpl extends BaseDaoImpl implements PersonDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.PersonDao";
	}
	/**
	 * 根据人员id得到部门
	 * added by taking.wang
	 * @since 2013-01-19
	 */

	@Override
	public Org getPersonOrg(String personId) {
		// TODO Auto-generated method stub
		List<Org> orgList = this.getSqlSession().selectList(this.getNameSpace()+".getOrgByPersonId", personId);
		if((null != orgList) && (orgList.size() > 0)){
			return orgList.get(0);
		}
		return null;
	}
	@Override
	public Person selectPersonLead(Map<String, Object> param) {
		List<Person> leadList = this.getSqlSession().selectList(this.getNameSpace()+".selectPersonLead", param);
		Person person = null;
		if(leadList.size()>0){
			person = (Person)leadList.get(0);
		}
		return person;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getPersonList(Map<String, Object> params) {
		return (List<Person>)getSqlSession().selectList(getNameSpace()+".getPersonList", params);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getPersonLeaderWithJobLevel(Map<String, Object> params) {
		return (List<Person>)getSqlSession().selectList(getNameSpace()+".selectLeaderAndJobLevel", params);
	}
	
	
}
