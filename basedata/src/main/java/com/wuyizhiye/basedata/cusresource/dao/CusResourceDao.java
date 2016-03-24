package com.wuyizhiye.basedata.cusresource.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.cusresource.model.CusResource;

/**
 * @ClassName CusResourceDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface CusResourceDao extends BaseDao {
	 List<CusResource> getCusResourceList(Map<String,Object> param);
	 CusResource getEntityByNumber(String number);
}
