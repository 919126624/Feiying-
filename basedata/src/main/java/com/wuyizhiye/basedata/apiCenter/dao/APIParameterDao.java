package com.wuyizhiye.basedata.apiCenter.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;

/**
 * @ClassName APIParameterDao
 * @Description 接口参数DAO接口
 * @author li.biao
 * @date 2015-4-2
 */
public interface APIParameterDao extends BaseDao{
	public List<APIParameter> getList(Map<String, Object> param);
}
