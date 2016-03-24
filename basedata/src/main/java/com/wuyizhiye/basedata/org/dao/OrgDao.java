package com.wuyizhiye.basedata.org.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName OrgDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface OrgDao extends BaseDao {
	
	/**
	 *查找子节点
	 * @param param
	 * @return
	 */
	List<Org> getOrgByOrg(Map<String,Object> param);
	
	List<Org> autoGetOrgByName(Map<String, Object> param);

	Org getOrgByTypeOrLevel(Map<String, Object> param);

	Org getParent(String orgId);
	
	List<Org> getOrgByType(Map<String,Object> param);
}
