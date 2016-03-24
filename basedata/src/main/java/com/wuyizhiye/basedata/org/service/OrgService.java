package com.wuyizhiye.basedata.org.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName OrgService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface OrgService extends BaseService<Org> {

	List<Org> autoGetOrgByName(Map<String, Object> param);

	Org getOrgByTypeOrLevel(Map<String, Object> param);
	
	Org getParent(String orgId);
	
	/**
	 * 根据组织类型 找到 级别最高的一个
	 * @param number
	 * @return
	 */
	Org getOrgByType(String number);

	void updateBatchChildren(Org porg, List<Org> updateChildrens);
}
