package com.wuyizhiye.basedata.basic.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.basic.dao.BasicDataDao;
import com.wuyizhiye.basedata.basic.model.BasicData;

/**
 * @ClassName BasicDataDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basicDataDao")
public class BasicDataDaoImpl extends BaseDaoImpl implements BasicDataDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.BasicDataDao";
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicData> getBasicDataList(Map<String, Object> param) {
		return getSqlSession().selectList(getNameSpace()+".select", param);
	}

	@Override
	public BasicData getEntityByNumber(String number) {
		// TODO Auto-generated method stub
		return (BasicData) this.getSqlSession().selectOne(this.getNameSpace()+".getByNumber", number);
	}
	

}
