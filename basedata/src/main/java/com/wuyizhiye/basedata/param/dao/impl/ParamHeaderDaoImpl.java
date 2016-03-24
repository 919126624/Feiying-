package com.wuyizhiye.basedata.param.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.param.dao.ParamHeaderDao;
import com.wuyizhiye.basedata.param.model.ParamHeader;

/**
 * @ClassName ParamHeaderDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="paramHeaderDao")
public class ParamHeaderDaoImpl extends BaseDaoImpl implements ParamHeaderDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.param.ParamHeaderDao";
	}

	@Override
	public List<ParamHeader> getParamHeaderList(Map<String, Object> map) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", map);
	}
}
