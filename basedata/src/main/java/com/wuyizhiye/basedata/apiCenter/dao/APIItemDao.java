package com.wuyizhiye.basedata.apiCenter.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;

/**
 * @ClassName APIItemDao
 * @Description 接口管理DAO接口
 * @author li.biao
 * @date 2015-4-2
 */
public interface APIItemDao extends BaseDao{
	public List<APIItem> getList(Map<String, Object> param);
}
