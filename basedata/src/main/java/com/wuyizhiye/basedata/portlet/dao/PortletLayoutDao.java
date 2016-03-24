package com.wuyizhiye.basedata.portlet.dao;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;

/**
 * @ClassName PortletLayoutDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PortletLayoutDao extends BaseDao {
	void updateStatus(PortletLayout portal);
	void updateSelective(PortletLayout portal);
	void deleteByParent(String parentId);
	List<PortletLayout> getlist(Map<String,Object> map);
}
