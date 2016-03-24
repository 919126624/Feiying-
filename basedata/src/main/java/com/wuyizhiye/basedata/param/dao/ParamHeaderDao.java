package com.wuyizhiye.basedata.param.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.param.model.ParamHeader;

/**
 * @ClassName ParamHeaderDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface ParamHeaderDao extends BaseDao {
	List<ParamHeader> getParamHeaderList(Map<String,Object> map);
}
