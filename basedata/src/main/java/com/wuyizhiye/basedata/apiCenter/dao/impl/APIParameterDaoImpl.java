package com.wuyizhiye.basedata.apiCenter.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao;
import com.wuyizhiye.basedata.apiCenter.model.APIParameter;

/**
 * @ClassName APIParameterDaoImpl
 * @Description 接口参数Dao实现类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="apiParameterDao")
public class APIParameterDaoImpl extends BaseDaoImpl implements APIParameterDao{
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.apiCenter.dao.APIParameterDao";
	}
	@Override
	public List<APIParameter> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
}
