package com.wuyizhiye.basedata.portlet.dao;


import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;

/**
 * @ClassName PositionPortletDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PositionPortletDao extends BaseDao {
	PositionPortlet getEntityByPositionId(String position);
}
