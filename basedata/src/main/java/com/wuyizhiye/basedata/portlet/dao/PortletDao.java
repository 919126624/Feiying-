package com.wuyizhiye.basedata.portlet.dao;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.model.Portlet;

/**
 * @ClassName PortletDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PortletDao extends BaseDao {
	void updateStatus(Portlet portal);
	void updateSelective(Portlet portal);
	public List<Portlet> getList(Map<String, Object> param);
	public List<Portlet> getListForSync(Map<String, Object> param);
}
