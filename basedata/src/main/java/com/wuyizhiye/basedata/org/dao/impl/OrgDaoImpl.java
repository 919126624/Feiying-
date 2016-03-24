package com.wuyizhiye.basedata.org.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName OrgDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="orgDao")
public class OrgDaoImpl extends BaseDaoImpl implements OrgDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao";
	}

	@Override
	public List<Org> getOrgByOrg(Map<String,Object> param) {
		return  getSqlSession().selectList(getNameSpace()+".getChild", param);
	}

	@Override
	public List<Org> autoGetOrgByName(Map<String, Object> param) {
		return  getSqlSession().selectList(getNameSpace()+".autoGetOrgByName", param);
	}
	
	@Override
	public Org getOrgByTypeOrLevel(Map<String, Object> param) {
		List<Org> orgList = getSqlSession().selectList(getNameSpace()+".autoGetOrgByName", param);
		if(orgList.size() > 0 ){
			return orgList.get(0);
		}
		return  null;
	}

	@Override
	public Org getParent(String orgId) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgId", orgId);
		List<Org> orgList = getSqlSession().selectList(getNameSpace()+".getParent", param);
		if(orgList.size() > 0 ){
			return orgList.get(0);
		}
		return  null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Org> getOrgByType(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return this.getSqlSession().selectList(getNameSpace()+".getOrgByBussType", param);
	}
}

