package com.wuyizhiye.basedata.param.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.param.dao.ParamLinesDao;
import com.wuyizhiye.basedata.param.model.ParamLines;

/**
 * @ClassName ParamLinesDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="paramLinesDao")
public class ParamLinesDaoImpl extends BaseDaoImpl implements ParamLinesDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.param.dao.ParamLinesDao";
	}

	@Override
	public List<ParamLines> getParamLines(String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("headerid", id);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}

	@Override
	public ParamLines getOneParamLines(Map<String, Object> map) {
		List<ParamLines> plist = getSqlSession().selectList(getNameSpace() + ".select", map);
		if(plist.size()>0) return plist.get(0);
		return null;
	}
}
