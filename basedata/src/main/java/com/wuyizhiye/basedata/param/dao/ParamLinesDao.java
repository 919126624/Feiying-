package com.wuyizhiye.basedata.param.dao;


import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.param.model.ParamLines;

/**
 * @ClassName ParamLinesDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ParamLinesDao extends BaseDao {
	
	List<ParamLines> getParamLines(String id);
	
	ParamLines getOneParamLines(Map<String,Object> map);
}
